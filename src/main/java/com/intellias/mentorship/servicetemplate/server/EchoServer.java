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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EchoServer {

  private static final int BUFFER_SIZE = 256;

  @Value("${app.echo.server.port}")
  private Integer port;

  public void start() throws IOException {
    ServerSocketChannel server = ServerSocketChannel.open();
    server.socket().bind(new InetSocketAddress(port));
    server.socket().setReuseAddress(true);
    server.configureBlocking(false);

    Selector selector = Selector.open();
    server.register(selector, SelectionKey.OP_ACCEPT);

    ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    while (true) {
      int channelCount = selector.select();
      if (channelCount > 0) {
        Set<SelectionKey> keys = selector.selectedKeys();
        Iterator<SelectionKey> iterator = keys.iterator();
        while (iterator.hasNext()) {
          SelectionKey key = iterator.next();
          iterator.remove();
          System.out.println("*********");
          if (key.isAcceptable()) {
            SocketChannel client = server.accept();
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ, client.socket().getPort());
          } else if (key.isReadable()) {
            SocketChannel client = (SocketChannel) key.channel();
            System.out.println("port: " + key.attachment());

            if (client.read(buffer) < 0) {
              System.out.println(buffer.get());
              key.cancel();
              client.close();
            } else {
              buffer.flip();
              System.out.println(buffer.get());
              client.write(buffer);
              buffer.clear();
            }
          }
        }
      }
    }
  }
}