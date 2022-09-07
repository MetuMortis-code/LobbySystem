package xyz.yooniks.lobby.server;

import org.bukkit.inventory.ItemStack;

public class Server {

  private final String name;
  private int online;

  private final ItemStack item;
  private final int slot;

  public Server(String name, ItemStack item, int slot) {
    this.name = name;
    this.item = item;
    this.slot = slot;
  }

  public ItemStack getItem() {
    return item;
  }

  public int getSlot() {
    return slot;
  }

  public int getOnline() {
    return online;
  }

  public void setOnline(int online) {
    this.online = online;
  }

  public String getName() {
    return name;
  }

}
