package com.intellias.mentorship.servicetemplate.server.command;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadCommand implements Command {

  private static final Logger LOG = Logger.getLogger(ReadCommand.class.getName());

  private Selector selector;
  private ServerSocketChannel serverSocket;
  private ByteBuffer buffer;
  private BlockingQueue<byte[]> queueForRead;
  private BlockingQueue<byte[]> queueForWrite;

  public ReadCommand(Selector selector, ServerSocketChannel serverSocket,
      ByteBuffer buffer, BlockingQueue<byte[]> queueForRead, BlockingQueue<byte[]> queueForWrite) {
    this.buffer = buffer;
    this.queueForRead = queueForRead;
    this.selector = selector;
    this.serverSocket = serverSocket;
    this.queueForWrite = queueForWrite;
  }

  @Override
  public void execute(SelectionKey key) {
    try {
      SocketChannel socketChannel = (SocketChannel) key.channel();
      int readCount = socketChannel.read(buffer);
      if (readCount > 0) {
        queueForRead.put(buffer.array());
        buffer.flip();
        LOG.log(Level.INFO, "Get and put message..");

        if(!queueForWrite.isEmpty()){
//          SocketChannel channel = serverSocket.accept();
          socketChannel.configureBlocking(false);
          socketChannel.register(selector, SelectionKey.OP_WRITE);
          LOG.log(Level.INFO, "Switched to WRITE mode..");
        }
      }
    } catch (Exception e) {
      LOG.log(Level.WARNING, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
