package com.intellias.mentorship.servicetemplate.server.command;

import com.intellias.mentorship.servicetemplate.server.TcpServer;
import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcceptCommand implements Command {

  private static final Logger LOG = Logger.getLogger(AcceptCommand.class.getName());

  private Selector selector;
  private Integer ops;

  public AcceptCommand(Selector selector, Integer ops) {
    this.selector = selector;
    this.ops = ops;
  }

  @Override
  public void execute(SocketChannel socketChannel) {
    try {
      socketChannel.configureBlocking(false);
      socketChannel.register(selector, ops);
    } catch (IOException e) {
      LOG.log(Level.CONFIG, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
