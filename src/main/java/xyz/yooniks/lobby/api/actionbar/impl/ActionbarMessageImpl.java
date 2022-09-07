package xyz.yooniks.lobby.api.actionbar.impl;

import xyz.yooniks.lobby.api.actionbar.ActionbarMessage;
import xyz.yooniks.lobby.user.User;

public abstract class ActionbarMessageImpl implements ActionbarMessage {

  protected final long time;

  private final String id;
  private final User user;

  public ActionbarMessageImpl(long time, String id, User user) {
    this.time = time;
    this.id = id;
    this.user = user;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public abstract String getMessage();

  @Override
  public boolean hasEnded() {
    return this.time < System.currentTimeMillis();
  }

  @Override
  public User getUser() {
    return user;
  }

}
