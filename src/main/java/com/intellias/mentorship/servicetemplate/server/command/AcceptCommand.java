package com.intellias.mentorship.servicetemplate.server.command;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcceptCommand implements Command {

  private static final Logger LOG = Logger.getLogger(AcceptCommand.class.getName());

  private Selector selector;
  private ServerSocketChannel serverSocket;
  private Integer ops;

  public AcceptCommand(Selector selector, ServerSocketChannel serverSocket, Integer ops) {
    this.selector = selector;
    this.serverSocket = serverSocket;
    this.ops = ops;
  }

  @Override
  public void execute(SelectionKey key) {
    try {
      SocketChannel channelRead = serverSocket.accept();
      channelRead.configureBlocking(false);
      channelRead.register(selector, ops);
      LOG.log(Level.INFO, "selected mode: READE");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
