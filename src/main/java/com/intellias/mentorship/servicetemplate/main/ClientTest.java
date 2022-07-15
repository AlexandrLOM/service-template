package com.intellias.mentorship.servicetemplate.main;

import com.intellias.mentorship.servicetemplate.client.Client;
import com.intellias.mentorship.servicetemplate.client.TcpClient;
import com.intellias.mentorship.servicetemplate.client.TcpClientConfig;
import java.io.IOException;

public class ClientTest {

  public static void main(String[] args) throws IOException, InterruptedException {

    Client tcpClient = new TcpClient(TcpClientConfig.getBaseConfig("localhost", 8098));

    String message =  "Hello world !!";

    System.out.println("request: ".concat(message));

    tcpClient.send(message.getBytes());
    byte[] answer = tcpClient.receive();

    System.out.println("response:".concat(new String(answer).trim()));

//    tcpClient.send(message.getBytes());
//    answer = tcpClient.receive();
//
//    System.out.println("response:".concat(new String(answer).trim()));

    tcpClient.stop();
  }

}
