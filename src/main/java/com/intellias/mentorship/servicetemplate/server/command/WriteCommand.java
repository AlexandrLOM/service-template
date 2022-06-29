package com.intellias.mentorship.servicetemplate.server.command;

import com.intellias.mentorship.servicetemplate.server.wrapper.SelectionKeyWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SocketChannelWrap;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteCommand implements Command {

  private static final Logger LOG = Logger.getLogger(WriteCommand.class.getName());

  private ByteBuffer buffer;
  private BlockingQueue<byte[]> queueForWrite;
  private List<Command> commands;

  public WriteCommand(
      ByteBuffer buffer, BlockingQueue<byte[]> queueForWrite, List<Command> commands) {
    this.buffer = buffer;
    this.queueForWrite = queueForWrite;
    this.commands = commands;
  }

  @Override
  public void execute(SelectionKeyWrap key) {
    try {
      SocketChannelWrap socketChannel = key.channel();
      buffer.put(queueForWrite.take());
      buffer.flip();
      socketChannel.write(buffer);

      commands.forEach(command -> command.execute(key));

    } catch (Exception e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
