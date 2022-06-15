package com.intellias.mentorship.servicetemplate.server.command;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WriteCommand implements Command {

  private static final Logger LOG = Logger.getLogger(WriteCommand.class.getName());

  private Selector selector;
  private ServerSocketChannel serverSocket;
  private ByteBuffer buffer;
  private BlockingQueue<byte[]> queueForWrite;

  public WriteCommand(Selector selector, ServerSocketChannel serverSocket,
      ByteBuffer buffer, BlockingQueue<byte[]> queueForWrite) {
    this.buffer = buffer;
    this.queueForWrite = queueForWrite;
    this.serverSocket = serverSocket;
    this.selector = selector;
  }

  @Override
  public void execute(SelectionKey key) {
    try {
      SocketChannel socketChannel = (SocketChannel) key.channel();
      buffer.put(queueForWrite.take());
      buffer.flip();
      socketChannel.write(buffer);

//      SocketChannel channel = serverSocket.accept();
      socketChannel.configureBlocking(false);
      socketChannel.register(selector, SelectionKey.OP_READ);
    } catch (Exception e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
