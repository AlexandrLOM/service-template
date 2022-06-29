package com.intellias.mentorship.servicetemplate.server.wrapper;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;

public interface SelectorWrap {

  Selector getSelector();

  int select() throws IOException;

  void close() throws IOException;

  Set<SelectionKey> selectedKeys();

}
