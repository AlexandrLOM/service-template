package com.intellias.mentorship.servicetemplate.server.command;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteCommand implements Command {

  private static final Logger LOG = Logger.getLogger(WriteCommand.class.getName());

  private ByteBuffer buffer;
  private BlockingQueue<byte[]> queue;

  public WriteCommand(ByteBuffer buffer, BlockingQueue<byte[]> queue) {
    this.buffer = buffer;
    this.queue = queue;
  }

  @Override
  public void execute(SocketChannel socketChannel) {
    try {
      buffer.put(queue.take());
      buffer.flip();
      socketChannel.write(buffer);
    } catch (Exception e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
