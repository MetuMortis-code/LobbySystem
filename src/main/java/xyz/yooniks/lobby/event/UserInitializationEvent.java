package xyz.yooniks.lobby.event;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import xyz.yooniks.lobby.user.User;

public class UserInitializationEvent extends PlayerEvent {

  private final User user;

  private static final HandlerList handlerList = new HandlerList();

  public UserInitializationEvent(Player who, User user) {
    super(who);
    this.user = user;
  }

  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }

  public User getUser() {
    return user;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

}
