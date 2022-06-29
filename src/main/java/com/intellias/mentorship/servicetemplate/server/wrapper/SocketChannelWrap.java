package com.intellias.mentorship.servicetemplate.server.wrapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

public interface SocketChannelWrap {

  SelectionKeyWrap register(SelectorWrap sel, int ops) throws ClosedChannelException;

  int read(ByteBuffer var1) throws IOException;

  int write(ByteBuffer var1) throws IOException;

  void configureBlocking(boolean var1) throws IOException;

}
