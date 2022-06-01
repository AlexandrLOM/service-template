package com.intellias.mentorship.servicetemplate.server;

@FunctionalInterface
public interface Encoder<T> {

  byte[] encode(T data);

}
