package com.intellias.mentorship.servicetemplate.server.config;

import com.intellias.mentorship.servicetemplate.server.command.AcceptCommand;
import com.intellias.mentorship.servicetemplate.server.command.Command;
import com.intellias.mentorship.servicetemplate.server.command.ReadCommand;
import com.intellias.mentorship.servicetemplate.server.command.WriteCommand;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

public class TcpServerConfig {

  private static final Logger LOG = Logger.getLogger(TcpServerConfig.class.getName());

  private final Selector selector;
  private final ServerSocketChannel serverSocket;
  private BlockingQueue<byte[]> queueForRead;
  private BlockingQueue<byte[]> queueForWrite;
  private Map<Integer, Command> commands;
  private String host;
  private int port;
  private ExecutorService executorService;

  public TcpServerConfig(Selector selector,
      ServerSocketChannel serverSocket,
      BlockingQueue<byte[]> queueForRead,
      BlockingQueue<byte[]> queueForWrite,
      Map<Integer, Command> commands,
      String host,
      int port,
      ExecutorService executorService) {
    this.selector = selector;
    this.serverSocket = serverSocket;
    this.queueForRead = queueForRead;
    this.queueForWrite = queueForWrite;
    this.commands = commands;
    this.host = host;
    this.port = port;
    this.executorService = executorService;
  }

  private TcpServerConfig(String host, int port) {
    try {
      selector = Selector.open();
      serverSocket = ServerSocketChannel.open();
      queueForRead = new LinkedBlockingQueue<>();
      queueForWrite = new LinkedBlockingQueue<>();

      commands = new HashMap<>();
      commands.put(SelectionKey.OP_ACCEPT, new AcceptCommand(selector, serverSocket, SelectionKey.OP_READ));
      commands.put(SelectionKey.OP_READ, new ReadCommand(ByteBuffer.allocate(256), queueForRead, queueForWrite));
      commands.put(SelectionKey.OP_WRITE, new WriteCommand(ByteBuffer.allocate(256), queueForWrite));

      this.host = host;
      this.port = port;
      this.executorService = Executors.newFixedThreadPool(3);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static TcpServerConfig getBaseConfig(String host, int port) {
    return new TcpServerConfig(host, port);
  }

  public Selector getSelector() {
    return selector;
  }

  public ServerSocketChannel getServerSocket() {
    return serverSocket;
  }

  public BlockingQueue<byte[]> getQueueForRead() {
    return queueForRead;
  }

  public BlockingQueue<byte[]> getQueueForWrite() {
    return queueForWrite;
  }

  public Map<Integer, Command> getCommands() {
    return commands;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public ExecutorService getExecutorService() {
    return executorService;
  }
}
