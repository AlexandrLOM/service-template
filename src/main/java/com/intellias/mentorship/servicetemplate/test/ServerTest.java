package com.intellias.mentorship.servicetemplate.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerTest {

  public static void main(String[] args) throws IOException {

    Selector selector = Selector.open();
    ServerSocketChannel serverSocket = ServerSocketChannel.open();
    InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 8089);
    serverSocket.bind(inetSocketAddress);
    serverSocket.configureBlocking(false);
    serverSocket.register(selector, SelectionKey.OP_ACCEPT);

//    SocketChannel socketChannel = serverSocket.accept();
//    socketChannel.configureBlocking(false);
//    socketChannel.register(selector, SelectionKey.OP_READ);

    ByteBuffer buffer = ByteBuffer.allocate(256);

//    socketChannel.write(buffer);

//    Optional<SelectableChannel> channel = selector.keys().stream().filter(SelectionKey::isReadable)
//        .map(SelectionKey::channel).findFirst();
//    if (channel.isPresent()) {
//      SocketChannel socketChannel = (SocketChannel) channel.get();
//      socketChannel.write(buffer);
//    }

    while (true) {
      selector.select();
      Set<SelectionKey> keys = selector.selectedKeys();
      Iterator<SelectionKey> iterator = keys.iterator();
      while (iterator.hasNext()) {
        SelectionKey key = iterator.next();
        if (key.isAcceptable()) {
          System.out.println("Acceptable");
          SocketChannel socketChannel = serverSocket.accept();
          socketChannel.configureBlocking(false);
          socketChannel.register(selector, SelectionKey.OP_READ);
        } else if (key.isReadable()) {
          SocketChannel socketChannel = (SocketChannel) key.channel();
          socketChannel.read(buffer);
          byte[] result = buffer.array();
          buffer = ByteBuffer.allocate(256);
          String resultString = new String(result).trim();
          if (!resultString.isEmpty()) {
            System.out.println("...");
            System.out.println(resultString);
            System.out.println("...");
          }
        }
        iterator.remove();
      }
    }
//    selector.close();
//    byte[] result = buffer.array();
//    String resultString = new String(result);
//    System.out.println(resultString);

  }

}
