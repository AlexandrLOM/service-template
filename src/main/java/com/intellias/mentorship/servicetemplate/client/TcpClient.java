package com.intellias.mentorship.servicetemplate.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TcpClient implements Client {

  private static final int CAPACITY = 256;

  private SocketChannel client;
  private ByteBuffer buffer;

  public TcpClient(TcpClientConfig config) {
    client = config.getClient();
    buffer = config.getBuffer();
  }

  @Override
  public boolean send(byte[] msg) {
    try {
      ByteBuffer buffer = ByteBuffer.wrap(msg);
      client.write(buffer);
      buffer.clear();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public byte[] receive() {
    try {
      client.read(buffer);
      return buffer.array();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void stop() {
    try {
      client.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
