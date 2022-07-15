package com.intellias.mentorship.servicetemplate.main;

import com.intellias.mentorship.servicetemplate.echo.Echo;
import com.intellias.mentorship.servicetemplate.echo.EchoServer;
import com.intellias.mentorship.servicetemplate.server.Server;
import com.intellias.mentorship.servicetemplate.server.TcpServer;
import com.intellias.mentorship.servicetemplate.server.config.TcpServerConfig;

public class EchoServerMain {


  public static void main(String[] args) throws InterruptedException {
    Server tcpServer = new TcpServer(TcpServerConfig.getBaseConfig("localhost", 8098));

    Echo echoServer = new EchoServer(tcpServer);

    echoServer.start();

    Thread.sleep(1000L);

    echoServer.stop();
  }

}
