package com.intellias.mentorship.servicetemplate.server.config;

import com.intellias.mentorship.servicetemplate.server.builder.ConfigServerBuilder;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigBuilder {

  private static final Logger LOG = Logger.getLogger(ConfigBuilder.class.getName());

  public ConfigServer getBaseConfig(String host, int port) {
    try {
      StorageServer storageServer = new QueueStorage();
      return new ConfigServerBuilder()
          .setSelector(Selector.open())
          .setServerSocketChannel(ServerSocketChannel.open())
          .setStorage(storageServer)
          .setHost(host)
          .setPort(port)
          .setAllocate(256)
          .setStartFromAction(SelectionKey.OP_READ)
          .build();
    } catch (IOException e) {
      LOG.log(Level.CONFIG, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
