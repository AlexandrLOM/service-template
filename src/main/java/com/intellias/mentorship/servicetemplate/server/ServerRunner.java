package com.intellias.mentorship.servicetemplate.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ServerRunner implements ApplicationRunner {

  @Autowired
  private EchoServer server;

  @Override
  public void run(ApplicationArguments args) throws Exception {
//    var server = new EchoServer();
//    server.init();
    server.startServer();
  }

}
