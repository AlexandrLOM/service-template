package com.intellias.mentorship.servicetemplate.server.wrapper;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class SelectionKeyWrapper implements SelectionKeyWrap {

  SelectionKey selectionKey;

  public SelectionKeyWrapper(SelectionKey selectionKey) {
    this.selectionKey = selectionKey;
  }

  @Override
  public SocketChannelWrap channel() {
    return new SocketChannelWrapper((SocketChannel)selectionKey.channel());
  }

  @Override
  public int interestOps() {
    return selectionKey.interestOps();
  }

  @Override
  public boolean isReadable() {
    return selectionKey.isReadable();
  }
}
