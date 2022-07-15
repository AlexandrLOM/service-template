package com.intellias.mentorship.servicetemplate.main;

import com.intellias.mentorship.servicetemplate.server.Server;
import com.intellias.mentorship.servicetemplate.server.TcpServer;
import com.intellias.mentorship.servicetemplate.server.config.TcpServerConfig;

public class TcpServerMain {

  public static void main(String[] args) throws InterruptedException {

    Server tcpServer = new TcpServer(TcpServerConfig.getBaseConfig("localhost", 8098));
    tcpServer.start();

    byte[] data = tcpServer.handle();
    tcpServer.send(data);
  }
}
