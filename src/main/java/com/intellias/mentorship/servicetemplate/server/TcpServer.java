package com.intellias.mentorship.servicetemplate.server;

import com.intellias.mentorship.servicetemplate.server.config.ConfigServer;
import com.intellias.mentorship.servicetemplate.server.config.StorageServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpServer implements Server{

  private static final Logger LOG = Logger.getLogger(TcpServer.class.getName());

  private final Selector selector;
  private final ServerSocketChannel serverSocket;
  private final StorageServer storage;
  private final InetSocketAddress inetSocketAddress;
  private int startFromAction; // read-1, write-4
  private int allocate;

  private boolean readyToWork = false;

  public TcpServer(ConfigServer configServer) {
    selector = configServer.getSelector();
    serverSocket = configServer.getServerSocket();
    storage = configServer.getStorage();
    inetSocketAddress = new InetSocketAddress(configServer.getHost(), configServer.getPort());
    startFromAction = configServer.getStartFromAction();
    allocate = configServer.getAllocate();
  }

  public void init() {
    try {
      serverSocket.bind(inetSocketAddress);
      serverSocket.configureBlocking(false);
      serverSocket.register(selector, SelectionKey.OP_ACCEPT);
      LOG.log(Level.INFO, "init address, host:" + inetSocketAddress.getHostName()
          + ", port:" + inetSocketAddress.getPort());
      readyToWork = true;
    } catch (IOException e) {
      LOG.log(Level.CONFIG, "Bad configuration: ".concat(e.getMessage()));
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean start() {
    Runnable receiveTask = this::process;
    Thread t = new Thread(receiveTask);
    t.start();
    return true;
  }

  @Override
  public boolean stop() {
    readyToWork = false;
    return false;
  }

  @Override
  public boolean send(byte[] data) {
    storage.put(data);
    return true;
  }

  @Override
  public byte[] receive() {
    return storage.take();
  }

  private void process() {
    LOG.log(Level.INFO, "start process receive..");
    try {
      while (readyToWork) {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          if (key.isAcceptable()) {
            registrationChanel(selector, serverSocket, startFromAction);
          } else if (key.isReadable()) {
            byte[] result = readFromChanel((SocketChannel) key.channel(), allocate);
            save(result);
          } else if (key.isWritable()) {
            byte[] data = storage.take();
            sendToChanel(data, (SocketChannel) key.channel(), allocate);
          }
          iterator.remove();
        }
      }
      serverSocket.close();
      selector.close();
      LOG.log(Level.INFO, "server stopped..");
    } catch (IOException e) {
      LOG.log(Level.WARNING, e.getMessage());
    }
  }

  private void save(byte[] data) {
    if (!(new String(data).trim().isEmpty())) {
      storage.put(data);
      LOG.log(Level.INFO, "saved data..");
    }
  }

  private SocketChannel registrationChanel(Selector selector, ServerSocketChannel serverSocket, int ops) {
    try {
      SocketChannel socketChannel = serverSocket.accept();
      socketChannel.configureBlocking(false);
      socketChannel.register(selector, ops);
      return socketChannel;
    } catch (IOException e) {
      LOG.log(Level.CONFIG, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private void sendToChanel(byte[] message, SocketChannel socketChannel, int bufferAllocate) {
    try {
      ByteBuffer buffer = ByteBuffer.allocate(bufferAllocate);
      buffer.put(message);
      buffer.flip();
      socketChannel.write(buffer);
      LOG.log(Level.INFO, "sent data..");
    } catch (IOException e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private byte[] readFromChanel(SocketChannel socketChannel, int bufferAllocate) {
    try {
      ByteBuffer buffer = ByteBuffer.allocate(bufferAllocate);
      socketChannel.read(buffer);
      return buffer.array();
    } catch (IOException e) {
      LOG.log(Level.CONFIG, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
