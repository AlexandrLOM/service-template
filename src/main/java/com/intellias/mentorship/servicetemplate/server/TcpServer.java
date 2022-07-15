package com.intellias.mentorship.servicetemplate.server;

import com.intellias.mentorship.servicetemplate.server.command.Command;
import com.intellias.mentorship.servicetemplate.server.config.TcpServerConfig;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpServer implements Server, Runnable {

  private static final Logger LOG = Logger.getLogger(TcpServer.class.getName());

  private Selector selector;
  private ServerSocketChannel serverSocket;
  private BlockingQueue<byte[]> queueForRead;
  private BlockingQueue<byte[]> queueForWrite;
  private InetSocketAddress inetSocketAddress;
  private Map<Integer, Command> commands;

  private ExecutorService executorService;
  private volatile boolean readyToWork = false;
  private volatile Mode mode;

  public TcpServer(TcpServerConfig config) {
    selector = config.getSelector();
    serverSocket = config.getServerSocket();
    queueForRead = config.getQueueForRead();
    queueForWrite = config.getQueueForWrite();
    inetSocketAddress = new InetSocketAddress(config.getHost(), config.getPort());
    executorService = config.getExecutorService();
    commands = config.getCommands();

    init();
  }

  private void init() {
    try {
      LOG.log(Level.INFO, "init address, host:" + inetSocketAddress.getHostName()
          + ", port:" + inetSocketAddress.getPort());
      serverSocket.bind(inetSocketAddress);
      serverSocket.configureBlocking(false);
      serverSocket.register(selector, SelectionKey.OP_ACCEPT);
      LOG.log(Level.INFO, "selected mode: ACCEPT");
      readyToWork = true;
    } catch (Exception e) {
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
    readyToWork = false;
    executorService.shutdown();
    return true;
  }

  @Override
  public boolean send(byte[] data) {
    try {
      queueForWrite.put(data);
      return true;
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void handle(Receiver receiver) {
    while (true) {
      try {
        byte[] take = queueForRead.take();
        receiver.receive(take);
      } catch (InterruptedException e) {
        LOG.log(Level.WARNING, e.getMessage());
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void run() {
    LOG.log(Level.INFO, "server started..");
    try {
      while (readyToWork) {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          Command command = commands.get(key.interestOps());
          command.execute(key);
          if (Objects.nonNull(mode)) {
            select(key, mode);
          }
          iterator.remove();
        }

      }
      serverSocket.close();
      selector.close();
      LOG.log(Level.INFO, "server stopped..");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public byte[] handle() {
    try {
      return queueForRead.take();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setMode(Mode mode) {
    this.mode = mode;
  }

  private boolean select(SelectionKey selectionKeyFrom, Mode mode) {
    try {
      SocketChannel socketChannel = (SocketChannel) selectionKeyFrom.channel();
      socketChannel.configureBlocking(false);
      socketChannel.register(selector, getModeCode(mode));
      this.mode = null;
      return true;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public int getModeCode(Mode mode) {
    switch (mode) {
      case READE:
        LOG.log(Level.INFO, "selected mode: {}", Mode.READE);
        return SelectionKey.OP_READ;
      case WRITE:
        LOG.log(Level.INFO, "selected mode: {}", Mode.WRITE);
        return SelectionKey.OP_WRITE;
      case ACCEPT:
        LOG.log(Level.INFO, "selected mode: {}", Mode.ACCEPT);
        return SelectionKey.OP_ACCEPT;
      default:
        throw new IllegalArgumentException();
    }
  }
}
