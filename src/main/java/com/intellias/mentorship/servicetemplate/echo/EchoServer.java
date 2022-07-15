package com.intellias.mentorship.servicetemplate.echo;

import com.intellias.mentorship.servicetemplate.server.Server;

public class EchoServer implements Echo {

  private Server server;

  public EchoServer(Server server) {
    this.server = server;
  }

  @Override
  public boolean start() {
    server.start();
      byte[] data = server.handle();
      String dataString = new String(data);
      String answer = "echo - ".concat(new String(data));
      server.send(answer.getBytes());
    return true;
  }

  @Override
  public boolean stop() {
    return server.stop();
  }
}


