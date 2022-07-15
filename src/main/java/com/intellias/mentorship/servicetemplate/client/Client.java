package com.intellias.mentorship.servicetemplate.client;

public interface Client {

  boolean send(byte[] msg);

  byte[] receive();

  void stop();

}
