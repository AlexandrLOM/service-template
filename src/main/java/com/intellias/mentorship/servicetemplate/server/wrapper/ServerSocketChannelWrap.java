package com.intellias.mentorship.servicetemplate.server.wrapper;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;

public interface ServerSocketChannelWrap {

  SocketChannelWrap accept() throws IOException;

  ServerSocketChannelWrap bind(SocketAddress local) throws IOException;

  ServerSocketChannelWrap configureBlocking(boolean block) throws IOException;

  SelectionKey register(SelectorWrap sel, int ops) throws ClosedChannelException;

  void close() throws IOException;

}
