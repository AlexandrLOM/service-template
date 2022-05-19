package com.intellias.mentorship.servicetemplate.server;

import java.nio.channels.SocketChannel;

public interface Server {

  boolean init(String host, int port, int bufferAllocate);

  boolean sendEncoder(Encoder encoder);

  boolean send(byte[] bytes);

  byte[] receive();

  boolean registerSocket(int selectionKey);

  SocketChannel registerChannel(int selectionKey);

  boolean close();
}
