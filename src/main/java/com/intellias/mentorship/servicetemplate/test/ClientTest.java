package com.intellias.mentorship.servicetemplate.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientTest {

  public static void main(String[] args) throws IOException, InterruptedException {
    SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8088));
//    ByteBuffer buffer = ByteBuffer.wrap("Hello world new".getBytes());
//    ByteBuffer buffer = ByteBuffer.wrap("1234567890".getBytes());
    ByteBuffer buffer = ByteBuffer.wrap("qwer qwe qw q".getBytes());
    client.write(buffer);
//    Thread.sleep(100L);
//    buffer = ByteBuffer.wrap("Hello world 2 new".getBytes());
//    client.write(buffer);
    buffer.flip();
    client.read(buffer);
    System.out.println(new String(buffer.array()).trim());
    client.close();
  }

}
