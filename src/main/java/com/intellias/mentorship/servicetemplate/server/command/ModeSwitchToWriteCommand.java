package com.intellias.mentorship.servicetemplate.server.command;

import com.intellias.mentorship.servicetemplate.server.wrapper.SelectionKeyWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SelectorWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SocketChannelWrap;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ModeSwitchToWriteCommand implements Command {

  private static final Logger LOG = Logger.getLogger(ModeSwitchToWriteCommand.class.getName());

  private SelectorWrap selector;
  private BlockingQueue<byte[]> queueForWrite;
  private int selectionKey;

  public ModeSwitchToWriteCommand(SelectorWrap selector, BlockingQueue<byte[]> queueForWrite) {
    this.selector = selector;
    this.queueForWrite = queueForWrite;
    this.selectionKey = SelectionKey.OP_WRITE;
  }

  @Override
  public void execute(SelectionKeyWrap key) {
    if (queueForWrite.isEmpty()) {
      try {
        SocketChannelWrap socketChannel = key.channel();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, selectionKey);
        LOG.log(Level.INFO, "Switched mode to Writable..");
      } catch (IOException e) {
        LOG.log(Level.WARNING, e.getMessage());
        throw new RuntimeException(e);
      }
    }
  }
}
