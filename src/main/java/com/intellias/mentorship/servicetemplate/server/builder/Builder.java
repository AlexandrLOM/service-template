package com.intellias.mentorship.servicetemplate.server.builder;

import com.intellias.mentorship.servicetemplate.server.config.ConfigServer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

public interface Builder {

  ConfigServerBuilder setSelector(Selector selector);

  ConfigServerBuilder setServerSocketChannel(ServerSocketChannel socketChannel);

    ConfigServerBuilder setStartFromAction(int startFromAction);

  ConfigServerBuilder setAllocate(int allocate);

  ConfigServerBuilder setHost(String host);

  ConfigServerBuilder setPort(int port);

  ConfigServer build();
}
