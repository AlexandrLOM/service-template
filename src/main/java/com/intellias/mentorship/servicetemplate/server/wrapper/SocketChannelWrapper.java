package com.intellias.mentorship.servicetemplate.server.wrapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;

public class SocketChannelWrapper implements SocketChannelWrap {

  private SocketChannel socketChannel;

  public SocketChannelWrapper(SocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  @Override
  public SelectionKeyWrap register(SelectorWrap sel, int ops) throws ClosedChannelException {
    return new SelectionKeyWrapper(socketChannel.register(sel.getSelector(), ops));
  }

  @Override
  public int read(ByteBuffer var1) throws IOException {
    return socketChannel.read(var1);
  }

  @Override
  public int write(ByteBuffer var1) throws IOException {
    return socketChannel.write(var1);
  }

  @Override
  public void configureBlocking(boolean var1) throws IOException {
    socketChannel.configureBlocking(false);
  }

}
