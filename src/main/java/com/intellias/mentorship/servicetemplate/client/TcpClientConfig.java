package com.intellias.mentorship.servicetemplate.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TcpClientConfig {

  private SocketChannel client;
  private ByteBuffer buffer;

  public TcpClientConfig(SocketChannel client, int bufferAllocate) {
    this.client = client;
    this.buffer = ByteBuffer.allocate(bufferAllocate);
  }

  public TcpClientConfig(String host, int port, int bufferAllocate) {
    try {
      client = SocketChannel.open(new InetSocketAddress(host, port));
      buffer = ByteBuffer.allocate(bufferAllocate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static TcpClientConfig getBaseConfig(String host, int port){
    return new TcpClientConfig(host, port, 256);
  }

  public SocketChannel getClient() {
    return client;
  }

  public ByteBuffer getBuffer() {
    return buffer;
  }
}
