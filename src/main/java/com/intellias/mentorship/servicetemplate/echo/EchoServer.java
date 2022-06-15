package com.intellias.mentorship.servicetemplate.echo;

import com.intellias.mentorship.servicetemplate.server.Server;
import com.intellias.mentorship.servicetemplate.server.TcpServer;
import com.intellias.mentorship.servicetemplate.server.config.ConfigServer;

public class EchoServer implements Echo {

  private Server server;

  public EchoServer(ConfigServer configServer) {
    this.server = new TcpServer(configServer);
  }

  @Override
  public boolean start() {
    server.start();
    server.handle(dada -> server.send(dada));
    return false;
  }

  @Override
  public boolean stop() {
    return false;
  }
}
