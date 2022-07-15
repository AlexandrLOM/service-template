package com.intellias.mentorship.servicetemplate.server.command;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteCommand implements Command {

  private static final Logger LOG = Logger.getLogger(WriteCommand.class.getName());

  private ByteBuffer buffer;
  private BlockingQueue<byte[]> queueForWrite;

  public WriteCommand(
      ByteBuffer buffer, BlockingQueue<byte[]> queueForWrite) {
    this.buffer = buffer;
    this.queueForWrite = queueForWrite;
  }

  @Override
  public void execute(SelectionKey key) {
    try {
      if(queueForWrite.isEmpty()) {
        return;
      }
      SocketChannel socketChannel = (SocketChannel) key.channel();
      buffer.put(queueForWrite.take());
      buffer.flip();
      socketChannel.write(buffer);
      LOG.log(Level.INFO, "message sent..");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
