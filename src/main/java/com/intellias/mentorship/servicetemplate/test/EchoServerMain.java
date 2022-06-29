package com.intellias.mentorship.servicetemplate.test;

import com.intellias.mentorship.servicetemplate.echo.Echo;
import com.intellias.mentorship.servicetemplate.echo.EchoServer;
import com.intellias.mentorship.servicetemplate.server.config.ConfigBuilder;

public class EchoServerMain {


  public static void main(String[] args) {
    Echo echoServer = new EchoServer(ConfigBuilder.getBaseConfig("localhost", 8088));

    echoServer.start();
  }


}
