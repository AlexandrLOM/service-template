package com.intellias.mentorship.servicetemplate.server.command;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import com.intellias.mentorship.servicetemplate.server.wrapper.SelectionKeyWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SocketChannelWrap;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

class ReadCommandTest {

  private SelectionKeyWrap key = Mockito.mock(SelectionKeyWrap.class);
  private ModeSwitchToWriteCommand modeSwitchToWriteCommand = Mockito.mock(ModeSwitchToWriteCommand.class);
  private SocketChannelWrap socketChannel = Mockito.mock(SocketChannelWrap.class);

  private BlockingQueue<byte[]> queueForRead;
  private List<Command> commands;

  private Command command;

  @BeforeEach
  void init() throws IOException {
    queueForRead =  new LinkedBlockingQueue<>();
    commands = List.of(modeSwitchToWriteCommand);
    command = new ReadCommand(ByteBuffer.allocate(256), queueForRead, commands);
  }

  @Test
  void execute() throws IOException, InterruptedException {
    Mockito.when(key.channel()).thenReturn(socketChannel);
    Mockito.when(socketChannel.read(any())).thenReturn(15);

    command.execute(key);

    Mockito.verify(key, Mockito.times(1)).channel();
    Mockito.verify(socketChannel, Mockito.atMostOnce()).read(any());
//    Mockito.verify(queueForRead, Mockito.atMostOnce()).put(any());
//    Mockito.verify(command, Mockito.atMostOnce()).execute(any());

  }
}