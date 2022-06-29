package com.intellias.mentorship.servicetemplate.server.config;

import com.intellias.mentorship.servicetemplate.server.command.AcceptCommand;
import com.intellias.mentorship.servicetemplate.server.command.ModeSwitchToReadCommand;
import com.intellias.mentorship.servicetemplate.server.command.ModeSwitchToWriteCommand;
import com.intellias.mentorship.servicetemplate.server.command.ReadCommand;
import com.intellias.mentorship.servicetemplate.server.command.WriteCommand;
import com.intellias.mentorship.servicetemplate.server.wrapper.SelectorWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SelectorWrapper;
import com.intellias.mentorship.servicetemplate.server.wrapper.ServerSocketChannelWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.ServerSocketChannelWrapper;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.List;
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
      SelectorWrap selector = new SelectorWrapper(Selector.open());
      ServerSocketChannelWrap serverSocket = new ServerSocketChannelWrapper(ServerSocketChannel.open());
      BlockingQueue<byte[]> queueForRead = new LinkedBlockingQueue<>();
      BlockingQueue<byte[]> queueForWrite = new LinkedBlockingQueue<>();
      return new ConfigServer(
          selector,
          serverSocket,
          queueForRead,
          queueForWrite,
          Map.of(SelectionKey.OP_ACCEPT, new AcceptCommand(selector, serverSocket, SelectionKey.OP_READ),
              SelectionKey.OP_READ, new ReadCommand(
                  ByteBuffer.allocate(256),
                  queueForRead,
                  List.of(new ModeSwitchToWriteCommand(selector, queueForWrite))),
              SelectionKey.OP_WRITE, new WriteCommand(
                  ByteBuffer.allocate(256),
                  queueForWrite,
                  List.of(new ModeSwitchToReadCommand(selector)))),
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
