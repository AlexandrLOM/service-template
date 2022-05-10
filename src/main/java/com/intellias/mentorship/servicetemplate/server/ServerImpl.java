package com.intellias.mentorship.servicetemplate.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerImpl implements Server {

  private static final String STOP_WORD = "stop serv";
  private static final int CAPACITY = 256;

  private Selector selector;
  private ServerSocketChannel serverSocket;
  private ByteBuffer buffer;

  public ServerImpl(InetSocketAddress inetSocketAddress) throws IOException {
    selector = Selector.open();
    openServerSocketChannel(inetSocketAddress, selector, SelectionKey.OP_ACCEPT);
    buffer = ByteBuffer.allocate(CAPACITY);
  }

  public void openServerSocketChannel(InetSocketAddress inetSocketAddress,
      Selector selector,
      Integer selectionKey) throws IOException {
    serverSocket = ServerSocketChannel.open();
    serverSocket.bind(inetSocketAddress);
    serverSocket.configureBlocking(false);
    serverSocket.register(selector, selectionKey);
  }

  public void start() throws IOException {
    while (true) {
      selector.select();
      Set<SelectionKey> selectedKeys = selector.selectedKeys();
      Iterator<SelectionKey> iter = selectedKeys.iterator();
      while (iter.hasNext()) {
        SelectionKey key = iter.next();
        if (key.isAcceptable()) {
          setSocketChannelForAccept(selector, serverSocket);
        }
        if (key.isReadable()) {
          sendAnswerOrStopServer(buffer, key);
          buffer.clear();
        }
        iter.remove();
      }
    }
  }

  private void sendAnswerOrStopServer(ByteBuffer buffer, SelectionKey key) throws IOException {
    SocketChannel client = (SocketChannel) key.channel();
    client.read(buffer);
    String q = new String(buffer.array()).trim();
    if (q.contains(STOP_WORD)) {
      client.close();
      System.out.println("Not accepting client messages anymore");
      System.exit(0);
    } else {
      System.out.println(new String(buffer.array()).trim());
      buffer.flip();
      client.write(buffer);
      buffer.rewind();
    }
  }

  private void setSocketChannelForAccept(Selector selector, ServerSocketChannel serverSocket)
      throws IOException {

    SocketChannel client = serverSocket.accept();
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_READ);
  }

}
