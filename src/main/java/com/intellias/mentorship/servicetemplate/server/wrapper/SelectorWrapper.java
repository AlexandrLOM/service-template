package com.intellias.mentorship.servicetemplate.server.wrapper;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectorWrapper implements SelectorWrap {

  private Selector selector;

  public SelectorWrapper(Selector selector) {
    this.selector = selector;
  }

  public Selector getSelector() {
    return selector;
  }

  @Override
  public int select() throws IOException {
    return selector.select();
  }

  @Override
  public void close() throws IOException {
    selector.close();
  }

  @Override
  public Set<SelectionKey> selectedKeys() {
    return selector.selectedKeys();
  }


}
