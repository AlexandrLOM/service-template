package com.intellias.mentorship.servicetemplate.server;

public interface Encoder <T>{

  byte[] encode(T date);

}
