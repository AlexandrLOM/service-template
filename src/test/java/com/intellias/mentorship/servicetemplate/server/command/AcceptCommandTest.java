package com.intellias.mentorship.servicetemplate.server.command;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AcceptCommandTest {

  Selector selector = Mockito.mock(Selector.class);
  ServerSocketChannel serverSocket = Mockito.mock(ServerSocketChannel.class);
  SocketChannel channel = Mockito.mock(SocketChannel.class);
  SelectableChannel selectableChannel = Mockito.mock(SelectableChannel.class);
  SelectionKey key = Mockito.mock(SelectionKey.class);
  Integer ops = SelectionKey.OP_READ;

  Command command;

  @BeforeEach
  void init() {
    command = new AcceptCommand(selector, serverSocket, ops);
  }

  @Test
  void execute() throws IOException {
    Mockito.when(serverSocket.accept()).thenReturn(channel);
    Mockito.when(channel.configureBlocking(false)).thenReturn(selectableChannel);
    Mockito.when(channel.register(selector, ops)).thenReturn(key);
    command.execute(null);

    Mockito.verify(serverSocket, Mockito.times(1)).accept();
    Mockito.verify(channel, Mockito.times(1)).configureBlocking(false);
    Mockito.verify(channel, Mockito.times(1)).register(selector, ops);
  }
}