package com.intellias.mentorship.servicetemplate.server.builder;

import com.intellias.mentorship.servicetemplate.server.config.ConfigServer;
import com.intellias.mentorship.servicetemplate.server.config.StorageServer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public class ConfigServerBuilder implements Builder {

  private Selector selector;
  private ServerSocketChannel socketChannel;
  private StorageServer storage;
  private int startFromAction; // read-1, write-4
  private int allocate;
  private String host;
  private int port;


  @Override
  public ConfigServerBuilder setSelector(Selector selector) {
   this.selector = selector;
    return this;
  }

  @Override
  public ConfigServerBuilder setServerSocketChannel(ServerSocketChannel socketChannel) {
    this.socketChannel = socketChannel;
    return this;
  }

  @Override
  public ConfigServerBuilder setStorage(StorageServer storage) {
    this.storage = storage;
    return this;
  }

  @Override
  public ConfigServerBuilder setStartFromAction(int startFromAction) {
    this.startFromAction = startFromAction;
    return this;
  }

  @Override
  public ConfigServerBuilder setAllocate(int allocate) {
    this.allocate = allocate;
    return this;
  }

  @Override
  public ConfigServerBuilder setHost(String host) {
    this.host = host;
    return this;
  }

  @Override
  public ConfigServerBuilder setPort(int port) {
    this.port = port;
    return this;
  }

  @Override
  public ConfigServer build() {
    return new ConfigServer(selector,
        socketChannel,
        storage,
        startFromAction,
        allocate,
        host,
        port);
  }

}
