package com.intellias.mentorship.servicetemplate.client;

import java.io.IOException;

public interface Client {

  byte[] sendMessage(byte[] msg) throws IOException;

  void stop() throws IOException;

}
