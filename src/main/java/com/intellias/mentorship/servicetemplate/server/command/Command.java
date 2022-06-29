package com.intellias.mentorship.servicetemplate.server.command;

import com.intellias.mentorship.servicetemplate.server.wrapper.SelectionKeyWrap;

public interface Command {

  void execute(SelectionKeyWrap key);

}
