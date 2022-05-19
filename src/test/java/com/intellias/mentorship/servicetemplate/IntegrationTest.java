package com.intellias.mentorship.servicetemplate;

import com.intellias.mentorship.servicetemplate.client.Client;
import com.intellias.mentorship.servicetemplate.client.TcpClient;
import com.intellias.mentorship.servicetemplate.server.Encoder;
import com.intellias.mentorship.servicetemplate.server.Server;
import com.intellias.mentorship.servicetemplate.server.TcpServer;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class IntegrationTest {

  private final String host = "localhost";

  private Server server;
  private Client client;

  @BeforeEach
  public void setup() {
    server = new TcpServer();
    client = new TcpClient();
  }

  @Test
  public void expectedSendMessageFromClientToServer() {
    server.init(host, 5454, 256);
    client.init(host, 5454);

    var message = "Hello world";
    server.registerSocket(SelectionKey.OP_ACCEPT);

    Runnable task = () -> {
      try {
        Thread.sleep(1L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      client.send(message.getBytes());
      client.stop();
    };

    task.run();
    var result = server.receive();
    server.close();

    var resultString = new String(result).trim();
    Assert.isTrue(message.equals(resultString), "received message");
  }

  @Test
  public void expectedSendMessageFromServerToClient() {
    server.init(host, 5455, 256);
    client.init(host, 5455);

    var message = "Hello world";
    server.registerSocket(SelectionKey.OP_ACCEPT);

    Runnable task = () -> {
      try {
        Thread.sleep(1L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      server.send(message.getBytes());
    };

    task.run();
    var result = client.receive();
    client.stop();
    server.close();

    var resultString = new String(result).trim();
    Assert.isTrue(message.equals(resultString), "received message");
  }

  @Test
  public void expectedSendMessageFromServerToClientWithUseEncoder() {
    server.init(host, 5456, 256);
    client.init(host, 5456);

    int messageInt = 100000000;
    server.registerSocket(SelectionKey.OP_ACCEPT);

    Runnable task = () -> {
      try {
        Thread.sleep(1L);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      server.sendEncoder(() -> BigInteger.valueOf(messageInt).toByteArray());
    };

    task.run();
    var result = client.receive();
    client.stop();
    server.close();

    var resultInt = ByteBuffer.wrap(result).getInt();
    Assert.isTrue(messageInt == resultInt, "received message");
  }

}
