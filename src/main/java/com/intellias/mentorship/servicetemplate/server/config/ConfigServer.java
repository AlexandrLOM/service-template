package com.intellias.mentorship.servicetemplate.server.config;

import com.intellias.mentorship.servicetemplate.server.command.Command;
import com.intellias.mentorship.servicetemplate.server.wrapper.SelectorWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.ServerSocketChannelWrap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

public class ConfigServer {

  private static final Logger LOG = Logger.getLogger(ConfigServer.class.getName());

  private final SelectorWrap selector;
  private final ServerSocketChannelWrap serverSocket;
  private BlockingQueue<byte[]> queueForRead;
  private BlockingQueue<byte[]> queueForWrite;
  private Map<Integer, Command> commands;
  private String host;
  private int port;
  private ExecutorService executorService;

  public ConfigServer(SelectorWrap selector,
      ServerSocketChannelWrap serverSocket,
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

  public SelectorWrap getSelector() {
    return selector;
  }

  public ServerSocketChannelWrap getServerSocket() {
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
