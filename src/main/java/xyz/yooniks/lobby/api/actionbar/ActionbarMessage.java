package xyz.yooniks.lobby.api.actionbar;

import java.util.UUID;
import xyz.yooniks.lobby.user.User;

public interface ActionbarMessage {

  String getId();

  boolean hasEnded();

  String getMessage();

  User getUser();

}
