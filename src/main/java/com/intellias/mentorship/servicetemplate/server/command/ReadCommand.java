package com.intellias.mentorship.servicetemplate.server.command;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadCommand implements Command {

  private static final Logger LOG = Logger.getLogger(ReadCommand.class.getName());

  private ByteBuffer buffer;
  private BlockingQueue<byte[]> queueForRead;
  private BlockingQueue<byte[]> queueForWrite;

  public ReadCommand(
      ByteBuffer buffer, BlockingQueue<byte[]> queueForRead,  BlockingQueue<byte[]> queueForWrite) {
    this.buffer = buffer;
    this.queueForRead = queueForRead;
    this.queueForWrite = queueForWrite;
  }

  @Override
  public void execute(SelectionKey key) {
    try {
      SocketChannel socketChannel = (SocketChannel) key.channel();
      int readCount = socketChannel.read(buffer);
      if (readCount > 0) {
        queueForRead.put(Arrays.copyOfRange(buffer.array(), 0, buffer.position()));
        buffer.flip();
        LOG.log(Level.INFO, "Get and put message..");
      }
      if (!queueForWrite.isEmpty()){
        buffer.flip();
        ByteBuffer data = ByteBuffer.wrap(queueForWrite.take());
        socketChannel.write(data);
        LOG.log(Level.INFO, "Send and put message..");
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
