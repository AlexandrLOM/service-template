package com.intellias.mentorship.servicetemplate.server.wrapper;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class ServerSocketChannelWrapper implements ServerSocketChannelWrap {

  private ServerSocketChannel socketChannel;

  public ServerSocketChannelWrapper(ServerSocketChannel socketChannel) {
    this.socketChannel = socketChannel;
  }

  public SocketChannelWrap accept() throws IOException {
    return new SocketChannelWrapper(socketChannel.accept());
  }

  @Override
  public ServerSocketChannelWrap bind(SocketAddress local) throws IOException {
    socketChannel.bind(local);
    return this;
  }

  @Override
  public ServerSocketChannelWrap configureBlocking(boolean block) throws IOException {
    socketChannel.configureBlocking(block);
    return this;
  }

  @Override
  public SelectionKey register(SelectorWrap sel, int ops) throws ClosedChannelException {
    return socketChannel.register(sel.getSelector(), ops);
  }

  @Override
  public void close() throws IOException {
    socketChannel.close();
  }
}
