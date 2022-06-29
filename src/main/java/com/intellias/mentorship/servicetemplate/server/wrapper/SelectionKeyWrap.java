package com.intellias.mentorship.servicetemplate.server.wrapper;

public interface SelectionKeyWrap {

  SocketChannelWrap channel();

  int interestOps();

  boolean isReadable();

}
