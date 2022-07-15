package com.intellias.mentorship.servicetemplate.server;

public interface Server {

  boolean start();

  byte[] handle();

  void setMode(Mode mode);

  boolean stop();

  boolean send(byte[] data);

  void handle(Receiver receiver);

  enum Mode {
    READE, WRITE, ACCEPT
  }

}
