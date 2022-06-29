package com.intellias.mentorship.servicetemplate.server.command;

import com.intellias.mentorship.servicetemplate.server.wrapper.SelectionKeyWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SocketChannelWrap;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadCommand implements Command {

  private static final Logger LOG = Logger.getLogger(ReadCommand.class.getName());

  private ByteBuffer buffer;
  private BlockingQueue<byte[]> queueForRead;
  private List<Command> commands;

  public ReadCommand(
      ByteBuffer buffer, BlockingQueue<byte[]> queueForRead,
      List<Command> commands) {
    this.buffer = buffer;
    this.queueForRead = queueForRead;
    this.commands = commands;
  }

  @Override
  public void execute(SelectionKeyWrap key) {
    try {
      SocketChannelWrap socketChannel = key.channel();
      int readCount = socketChannel.read(buffer);
      if (readCount > 0) {
        queueForRead.put(Arrays.copyOfRange(buffer.array(), 0, buffer.position()));
        buffer.flip();
        LOG.log(Level.INFO, "Get and put message..");

        commands.forEach(command -> command.execute(key));
      }
    } catch (Exception e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
