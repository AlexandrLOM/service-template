package com.intellias.mentorship.servicetemplate.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientTest {

  public static void main(String[] args) throws IOException, InterruptedException {
    SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8088));
    ByteBuffer buffer = ByteBuffer.wrap("Hello world".getBytes());
    client.write(buffer);
    Thread.sleep(0L);
    buffer = ByteBuffer.wrap("Hello world 2".getBytes());
    client.write(buffer);
    buffer.clear();
    client.close();
  }

}
