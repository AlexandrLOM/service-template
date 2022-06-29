package com.intellias.mentorship.servicetemplate.server.command;

import com.intellias.mentorship.servicetemplate.server.wrapper.SelectionKeyWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SelectorWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SocketChannelWrap;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeSwitchToReadCommand implements Command {

  private static final Logger LOG = Logger.getLogger(ModeSwitchToReadCommand.class.getName());

  private SelectorWrap selector;
  private int selectionKey;

  public ModeSwitchToReadCommand(SelectorWrap selector) {
    this.selector = selector;
    this.selectionKey = SelectionKey.OP_READ;
  }

  @Override
  public void execute(SelectionKeyWrap key) {
    try {
      if (!key.isReadable()) {
        SocketChannelWrap socketChannel = key.channel();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, selectionKey);
        LOG.log(Level.INFO, "Switched mode Readable..");
      }
    } catch (IOException e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
