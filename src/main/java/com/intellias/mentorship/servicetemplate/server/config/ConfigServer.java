package com.intellias.mentorship.servicetemplate.server.config;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigServer {

  private static final Logger LOG = Logger.getLogger(ConfigServer.class.getName());

  private final Selector selector;
  private final ServerSocketChannel serverSocket;
  private final BlockingQueue<byte[]> queue;
  private final int startFromAction; // read-1, write-4
  private final int allocate;
  private final String host;
  private final int port;

  public ConfigServer(Selector selector,
      ServerSocketChannel serverSocket,
      int startFromAction,
      int allocate,
      String host,
      int port) {
    this.selector = selector;
    this.serverSocket = serverSocket;
    this.queue = new LinkedBlockingQueue<>();
    this.startFromAction = startFromAction;
    this.allocate = allocate;
    this.host = host;
    this.port = port;
  }

  public Selector getSelector() {
    return selector;
  }

  public ServerSocketChannel getServerSocket() {
    return serverSocket;
  }

  public BlockingQueue<byte[]> getQueue() {
    return queue;
  }
  public int getStartFromAction() {
    return startFromAction;
  }

  public int getAllocate() {
    return allocate;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public void init(String host, int port, int selectionKey) {
    InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
    try {
      serverSocket.bind(inetSocketAddress);
      serverSocket.configureBlocking(false);
      serverSocket.register(selector, selectionKey);
      LOG.log(Level.INFO, "init address, host:" + host + ", port:" + port);
    } catch (IOException e) {
      LOG.log(Level.CONFIG, "Bad configuration: ".concat(e.getMessage()));
      throw new RuntimeException(e);
    }
  }


}
