package xyz.yooniks.lobby.queue;

import org.bukkit.entity.Player;

public class Queue {

  private final Player player;
  private final String targetServer;
  private int place;

  public Queue(Player player, String targetServer, int place) {
    this.player = player;
    this.targetServer = targetServer;
    this.place = place;
  }

  public Player getPlayer() {
    return player;
  }

  public String getTargetServer() {
    return targetServer;
  }

  public int getPlace() {
    return place;
  }

  public void setPlace(int place) {
    this.place = place;
  }
}
