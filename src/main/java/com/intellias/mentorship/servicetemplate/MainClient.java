package com.intellias.mentorship.servicetemplate;

import com.intellias.mentorship.servicetemplate.client.Client;
import com.intellias.mentorship.servicetemplate.client.ClientImpl;
import java.io.IOException;
import java.net.InetSocketAddress;

public class MainClient {

  public static void main(String[] args) throws IOException {
    String message1 = "first message";
    String message2 = "second message";
    String messageStop = "stop serv";

    String host = "localhost";
    int port = 5454;
    InetSocketAddress socketAddress = new InetSocketAddress(host, port);

    Client client = new ClientImpl(socketAddress);

    byte[] answer;

    printRequest(message1);
    answer = client.sendMessage(message1.getBytes());
    printResponse(toString(answer));

    printRequest(message2);
    answer = client.sendMessage(message2.getBytes());
    printResponse(toString(answer));

    printRequest(messageStop);
    answer = client.sendMessage(messageStop.getBytes());
    printResponse(toString(answer));
    client.stop();

  }

  private static String toString(byte[] bytes) {
    return new String(bytes).trim();
  }

  private static void printRequest(String message) {
    System.out.println("Request: ".concat(message));
  }

  private static void printResponse(String message) {
    System.out.println("Response: ".concat(message));
  }

}
