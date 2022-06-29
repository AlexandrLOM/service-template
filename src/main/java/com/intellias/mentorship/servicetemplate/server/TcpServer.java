package com.intellias.mentorship.servicetemplate.server;

import com.intellias.mentorship.servicetemplate.server.command.Command;
import com.intellias.mentorship.servicetemplate.server.config.ConfigServer;
import com.intellias.mentorship.servicetemplate.server.wrapper.SelectionKeyWrapper;
import com.intellias.mentorship.servicetemplate.server.wrapper.SelectorWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.ServerSocketChannelWrap;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TcpServer implements Server, Runnable {

  private static final Logger LOG = Logger.getLogger(TcpServer.class.getName());

  private SelectorWrap selector;
  private ServerSocketChannelWrap serverSocket;
  private BlockingQueue<byte[]> queueForRead;
  private BlockingQueue<byte[]> queueForWrite;
  private InetSocketAddress inetSocketAddress;
  private Map<Integer, Command> commands;

  private ExecutorService executorService;
  private boolean readyToWork = false;

  public TcpServer(ConfigServer configServer) {
    selector = configServer.getSelector();
    serverSocket = configServer.getServerSocket();
    queueForRead = configServer.getQueueForRead();
    queueForWrite = configServer.getQueueForWrite();
    inetSocketAddress = new InetSocketAddress(configServer.getHost(), configServer.getPort());
    executorService = configServer.getExecutorService();
    commands = configServer.getCommands();

    init();
  }

  private void init() {
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
      LOG.log(Level.WARNING, e.getMessage());
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
          command.execute(new SelectionKeyWrapper(key));
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

  private void switchMode() {
    try {
      int mode = SelectionKey.OP_READ;
      while (readyToWork) {
        if (!queueForWrite.isEmpty()) {
          serverSocket.bind(inetSocketAddress);
          serverSocket.configureBlocking(false);
          serverSocket.register(selector, SelectionKey.OP_WRITE);

        } else {
          serverSocket.bind(inetSocketAddress);
          serverSocket.configureBlocking(false);
          serverSocket.register(selector, SelectionKey.OP_READ);
        }
      }
    } catch (Exception e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
