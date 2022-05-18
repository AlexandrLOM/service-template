package com.intellias.mentorship.servicetemplate.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TcpServer implements Server {

  private Selector selector;
  private ServerSocketChannel serverSocket;
  private Integer bufferAllocate;

  @Override
  public boolean init(String host, int port, int bufferAllocate) {
    try {
      selector = Selector.open();
      serverSocket = ServerSocketChannel.open();
      InetSocketAddress inetSocketAddress = new InetSocketAddress(host, port);
      serverSocket.bind(inetSocketAddress);
      serverSocket.configureBlocking(false);
      this.bufferAllocate = bufferAllocate;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public Selector getSelector() {
    return selector;
  }

  @Override
  public byte[] receive() {
    byte[] result = null;
    boolean isReceived = false;
    try {
      while (!isReceived) {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
          SelectionKey myKey = iterator.next();
          if (myKey.isAcceptable()) {
            registerChannel(SelectionKey.OP_READ);
          } else if (myKey.isReadable()) {
            SocketChannel socketChannel = (SocketChannel) myKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(bufferAllocate);
            socketChannel.read(buffer);
            result = buffer.array();
            isReceived = true;

          }
          iterator.remove();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public boolean registerSocket(int selectionKey) {
    try {
      serverSocket.register(selector, selectionKey);
    } catch (ClosedChannelException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean send(byte[] bytes) {
    boolean isSensed = false;
    try {
      while (!isSensed) {
        selector.select();
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
          SelectionKey myKey = iterator.next();
          if (myKey.isAcceptable()) {
            registerChannel(SelectionKey.OP_WRITE);
          } else if (myKey.isWritable()) {
            SocketChannel socketChannel = (SocketChannel) myKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(bufferAllocate);
            buffer.put(bytes);
            buffer.flip();
            socketChannel.write(buffer);
            isSensed = true;
          }
          iterator.remove();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public SocketChannel registerChannel(int selectionKey) {
    SocketChannel socketChannel = null;
    try {
      socketChannel = serverSocket.accept();
      socketChannel.configureBlocking(false);
      socketChannel.register(selector, selectionKey);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return socketChannel;
  }

  public boolean close() {
    try {
      selector.close();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

}
