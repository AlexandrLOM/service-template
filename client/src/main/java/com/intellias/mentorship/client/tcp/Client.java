package com.intellias.mentorship.client.tcp;

public interface Client {

  boolean send(byte[] msg);

  byte[] receive();

  void stop();

}
