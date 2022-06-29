package com.intellias.mentorship.servicetemplate.server.command;

import static org.junit.jupiter.api.Assertions.*;

import com.intellias.mentorship.servicetemplate.server.wrapper.SelectionKeyWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.SocketChannelWrap;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class WriteCommandTest {

  private SelectionKeyWrap key = Mockito.mock(SelectionKeyWrap.class);
  private ModeSwitchToReadCommand modeSwitchToReadCommand = Mockito.mock(ModeSwitchToReadCommand.class);
  private SocketChannelWrap socketChannel = Mockito.mock(SocketChannelWrap.class);

  private BlockingQueue<byte[]> queueForRead;
  private List<Command> commands;

  private Command command;

  @BeforeEach
  void init() throws IOException {
    queueForRead =  new LinkedBlockingQueue<>();
    commands = List.of(modeSwitchToReadCommand);
    command = new ReadCommand(ByteBuffer.allocate(256), queueForRead, commands);
  }

  @Test
  void execute() {
  }
}