package com.intellias.mentorship.servicetemplate.server.command;

import com.intellias.mentorship.servicetemplate.server.wrapper.SelectionKeyWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SelectorWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.ServerSocketChannelWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SocketChannelWrap;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AcceptCommand implements Command {

  private static final Logger LOG = Logger.getLogger(AcceptCommand.class.getName());

  private SelectorWrap selector;
  private ServerSocketChannelWrap serverSocket;
  private Integer ops;

  public AcceptCommand(SelectorWrap selector, ServerSocketChannelWrap serverSocket, Integer ops) {
    this.selector = selector;
    this.serverSocket = serverSocket;
    this.ops = ops;
  }

  @Override
  public void execute(SelectionKeyWrap key) {
    try {
      SocketChannelWrap channel = serverSocket.accept();
      channel.configureBlocking(false);
      channel.register(selector, ops);
    } catch (IOException e) {
      LOG.log(Level.CONFIG, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
