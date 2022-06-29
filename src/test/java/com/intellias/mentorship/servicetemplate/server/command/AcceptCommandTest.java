package com.intellias.mentorship.servicetemplate.server.command;

import com.intellias.mentorship.servicetemplate.server.wrapper.SelectionKeyWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SelectorWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.ServerSocketChannelWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SocketChannelWrap;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class AcceptCommandTest {

  private SelectorWrap selector = Mockito.mock(SelectorWrap.class);
  private ServerSocketChannelWrap serverSocket = Mockito.mock(ServerSocketChannelWrap.class);
  private SocketChannelWrap channel = Mockito.mock(SocketChannelWrap.class);
  private SelectionKeyWrap key = Mockito.mock(SelectionKeyWrap.class);
  private Integer ops = SelectionKey.OP_READ;

  private Command command;

  @BeforeEach
  void init() throws IOException {
    command = new AcceptCommand(selector, serverSocket, ops);
  }

  @Test
  void execute() throws IOException {
    Mockito.when(serverSocket.accept()).thenReturn(channel);
    Mockito.when(channel.register(selector, ops)).thenReturn(key);

    command.execute(key);

    Mockito.verify(serverSocket, Mockito.times(1)).accept();
    Mockito.verify(channel, Mockito.times(1)).configureBlocking(false);
    Mockito.verify(channel, Mockito.times(1)).register(selector, ops);
  }
}