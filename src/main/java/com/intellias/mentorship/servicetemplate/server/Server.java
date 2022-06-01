package com.intellias.mentorship.servicetemplate.server;

public interface Server {

  void init();

  boolean start();

  boolean stop();

  boolean send(byte[] data);

  byte[] receive();

}
