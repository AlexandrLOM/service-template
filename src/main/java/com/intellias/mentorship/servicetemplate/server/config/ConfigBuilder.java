package com.intellias.mentorship.servicetemplate.server.config;

import com.intellias.mentorship.servicetemplate.server.command.AcceptCommand;
import com.intellias.mentorship.servicetemplate.server.command.ReadCommand;
import com.intellias.mentorship.servicetemplate.server.command.WriteCommand;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigBuilder {

  private static final Logger LOG = Logger.getLogger(ConfigBuilder.class.getName());

  public static ConfigServer getBaseConfig(String host, int port) {
    try {
      Selector selector = Selector.open();
      ServerSocketChannel serverSocket = ServerSocketChannel.open();
      BlockingQueue<byte[]> queueForRead = new LinkedBlockingQueue<>();
      BlockingQueue<byte[]> queueForWrite = new LinkedBlockingQueue<>();
      return new ConfigServer(
          selector,
          serverSocket,
          queueForRead,
          queueForWrite,
          Map.of(SelectionKey.OP_ACCEPT, new AcceptCommand(selector, serverSocket, SelectionKey.OP_READ),
              SelectionKey.OP_READ, new ReadCommand(selector, serverSocket, ByteBuffer.allocate(256), queueForRead, queueForWrite),
              SelectionKey.OP_WRITE, new WriteCommand(selector, serverSocket, ByteBuffer.allocate(256), queueForWrite)),
          host,
          port,
          Executors.newFixedThreadPool(3)
      );
    } catch (IOException e) {
      LOG.log(Level.CONFIG, e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
