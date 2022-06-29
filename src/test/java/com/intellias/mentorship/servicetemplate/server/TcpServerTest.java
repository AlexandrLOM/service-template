package com.intellias.mentorship.servicetemplate.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import com.intellias.mentorship.servicetemplate.server.command.Command;
import com.intellias.mentorship.servicetemplate.server.command.ReadCommand;
import com.intellias.mentorship.servicetemplate.server.config.ConfigBuilder;
import com.intellias.mentorship.servicetemplate.server.config.ConfigServer;
import com.intellias.mentorship.servicetemplate.server.wrapper.SelectorWrap;
import com.intellias.mentorship.servicetemplate.server.wrapper.ServerSocketChannelWrap;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TcpServerTest {

  private SelectorWrap selector = mock(SelectorWrap.class);
  private ServerSocketChannelWrap serverSocket = mock(ServerSocketChannelWrap.class);
  private BlockingQueue<byte[]> queueForRead;
  private BlockingQueue<byte[]> queueForWrite;
  private InetSocketAddress inetSocketAddress;
  private Map<Integer, Command> commands;

  ConfigServer configServer = mock(ConfigServer.class);

  TcpServer tcpServer;

  @BeforeEach
  void init() throws IOException {
    ConfigServer configServer = ConfigBuilder.getBaseConfig("localhost", 8088);
    tcpServer = new TcpServer(configServer);
  }

  @Test
  void testStart() {
  }

  @Test
  void testStop() {
  }

  @Test
  void testSend() {
  }

  @Test
  void handle() {
    ByteBuffer buffer = ByteBuffer.allocate(256);
    buffer.put("qwe".getBytes(StandardCharsets.UTF_8));
    System.out.println(Arrays.toString(Arrays.copyOfRange(buffer.array(), 0, buffer.position())));
    buffer.put("asd".getBytes(StandardCharsets.UTF_8));
    System.out.println(Arrays.toString(Arrays.copyOfRange(buffer.array(), 0, buffer.position())));
    buffer.flip();
    buffer.clear();
    buffer.put("zxc".getBytes(StandardCharsets.UTF_8));
    System.out.println(Arrays.toString(Arrays.copyOfRange(buffer.array(), 0, buffer.position())));

  }

  @Test
  void testRun() {

    tcpServer.run();


    tcpServer.stop();
  }
}