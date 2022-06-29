package com.intellias.mentorship.servicetemplate.test;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.intellias.mentorship.servicetemplate.server.Server;
import com.intellias.mentorship.servicetemplate.server.TcpServer;
import com.intellias.mentorship.servicetemplate.server.config.ConfigBuilder;
import com.intellias.mentorship.servicetemplate.server.config.ConfigServer;

public class TcpServerMain {

  public static void main(String[] args) throws InterruptedException {
    Server tcpServer = new TcpServer(ConfigBuilder.getBaseConfig("localhost", 8088));
    tcpServer.start();



//    tcpServer.send("qewrdsfadfaf".getBytes());

    while (true) {
      Thread.sleep(10000L);
      tcpServer.handle((data -> System.out.println(new String(data, UTF_8).trim())));
//      tcpServer.stop();
    }

  }

  public static ConfigServer getConfig() {
    ConfigBuilder configBuilder = new ConfigBuilder();
    return ConfigBuilder.getBaseConfig("localhost", 8088);
  }
}
