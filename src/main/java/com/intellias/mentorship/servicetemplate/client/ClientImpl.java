package com.intellias.mentorship.servicetemplate.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientImpl implements Client{

  private static final int CAPACITY = 256;

  private SocketChannel client;
  private ByteBuffer buffer;

  public ClientImpl(InetSocketAddress inetSocketAddress) throws IOException {
    client = SocketChannel.open(inetSocketAddress);
    buffer = ByteBuffer.allocate(CAPACITY);
  }

  @Override
  public byte[] sendMessage(byte[] msg) throws IOException {
    buffer = ByteBuffer.wrap(msg);
    client.write(buffer);
    buffer.clear();
    client.read(buffer);
    byte[] answer = buffer.array();
    buffer.clear();
    return answer;
  }

  @Override
  public void stop() throws IOException {
    client.close();
  }

}
