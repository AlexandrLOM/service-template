package com.intellias.mentorship.servicetemplate.test;

import com.intellias.mentorship.servicetemplate.server.Server;
import com.intellias.mentorship.servicetemplate.server.TcpServer;
import com.intellias.mentorship.servicetemplate.server.config.ConfigBuilder;
import com.intellias.mentorship.servicetemplate.server.config.ConfigServer;

public class TcpServerMain {

  public static void main(String[] args) throws InterruptedException {
    Server tcpServer = new TcpServer(getConfig());
    tcpServer.init();

    tcpServer.start();

    Thread.sleep(10000L);

    tcpServer.stop();

  }

  public static ConfigServer getConfig() {
    ConfigBuilder configBuilder = new ConfigBuilder();
    return configBuilder.getBaseConfig("localhost", 8088);
  }
}
