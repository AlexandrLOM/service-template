package com.intellias.mentorship.servicetemplate;

import com.intellias.mentorship.servicetemplate.server.Server;
import com.intellias.mentorship.servicetemplate.server.ServerImpl;
import java.io.IOException;
import java.net.InetSocketAddress;

public class MainServer {

  public static void main(String[] args) throws IOException {
    String host = "localhost";
    int port = 5454;

    InetSocketAddress socketAddress = new InetSocketAddress(host, port);

    Server server = new ServerImpl(socketAddress);
    System.out.println("Server start!");
    server.start();

    System.out.println("Server stopped!");
  }

}
