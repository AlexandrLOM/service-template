package com.intellias.mentorship.servicetemplate.server;

import com.intellias.mentorship.servicetemplate.server.command.AcceptCommand;
import com.intellias.mentorship.servicetemplate.server.command.InternalServerCommands;
import com.intellias.mentorship.servicetemplate.server.command.ReadCommand;
import com.intellias.mentorship.servicetemplate.server.command.WriteCommand;
import com.intellias.mentorship.servicetemplate.server.config.ConfigServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpServer implements Server, Runnable {

  private static final Logger LOG = Logger.getLogger(TcpServer.class.getName());

  private final Selector selector;
  private final ServerSocketChannel serverSocket;
  private final BlockingQueue<byte[]> queue;
  private final InetSocketAddress inetSocketAddress;
  private int startFromAction; // read-1, write-4
  private int allocate;
  private ByteBuffer buffer;

  private ExecutorService executorService;

  private InternalServerCommands commands;

  private boolean readyToWork = false;

  public TcpServer(ConfigServer configServer) {
    selector = configServer.getSelector();
    serverSocket = configServer.getServerSocket();
    queue = configServer.getQueue();
    inetSocketAddress = new InetSocketAddress(configServer.getHost(), configServer.getPort());
    startFromAction = configServer.getStartFromAction();
    allocate = configServer.getAllocate();
    executorService = Executors.newFixedThreadPool(3);
    buffer = ByteBuffer.allocate(configServer.getAllocate());

    commands = new InternalServerCommands(
        new AcceptCommand(selector, startFromAction),
        new ReadCommand(ByteBuffer.allocate(configServer.getAllocate()), queue),
        new WriteCommand(ByteBuffer.allocate(configServer.getAllocate()), queue)
    );
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
    executorService.execute(this);
    return true;
  }

  @Override
  public boolean stop() {
    executorService.shutdown();
    readyToWork = false;
    return false;
  }

  @Override
  public boolean send(byte[] data) {
    try {
      queue.put(data);
      return true;
    } catch (InterruptedException e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  @Override
  public void receive(Receiver receiver) {
    while (true) {
      try {
        byte[] take = queue.take();
        receiver.receive(take);
      } catch (InterruptedException e) {
        LOG.log(Level.WARNING, e.getMessage());
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void run() {
    LOG.log(Level.INFO, "start process receive..");
    try {
      while (readyToWork) {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          if (key.isAcceptable()) {
            commands.accept(serverSocket.accept());
          } else if (key.isReadable()) {
            commands.read((SocketChannel) key.channel());
          } else if (key.isWritable()) {
            byte[] data = queue.take();
            commands.write((SocketChannel) key.channel());
          }
          iterator.remove();
        }
      }
      serverSocket.close();
      selector.close();
      LOG.log(Level.INFO, "server stopped..");
    } catch (Exception e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }

  private void save(byte[] data) {
    try {
      if (!(new String(data).trim().isEmpty())) {
        queue.put(data);
        LOG.log(Level.INFO, "saved data..");
      }
    } catch (Exception e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
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

//  private void sendToChanel(byte[] message, SocketChannel socketChannel, int bufferAllocate) {
//    try {
//      ByteBuffer buffer = ByteBuffer.allocate(bufferAllocate);
//      buffer.put(message);
//      buffer.flip();
//      socketChannel.write(buffer);
//      LOG.log(Level.INFO, "sent data..");
//    } catch (IOException e) {
//      LOG.log(Level.WARNING, e.getMessage());
//      throw new RuntimeException(e);
//    }
//  }

//  private byte[] readFromChanel(SocketChannel socketChannel, int bufferAllocate) {
//    try {
//      ByteBuffer buffer = ByteBuffer.allocate(bufferAllocate);
//      socketChannel.read(buffer);
//      return buffer.array();
//    } catch (IOException e) {
//      LOG.log(Level.CONFIG, e.getMessage());
//      throw new RuntimeException(e);
//    }
//  }
}
