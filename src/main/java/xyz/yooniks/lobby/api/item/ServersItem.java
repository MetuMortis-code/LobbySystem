package xyz.yooniks.lobby.api.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.yooniks.lobby.server.ServersInventory;
import xyz.yooniks.lobby.user.User;

public class ServersItem implements HoldableItem {

  private final ItemStack item;
  private final int slot;

  private final ServersInventory serversInventory;

  public ServersItem(ItemStack item, int slot,
      ServersInventory serversInventory) {
    this.item = item;
    this.slot = slot;
    this.serversInventory = serversInventory;
  }

  @Override
  public ItemStack getItem(User user) {
    return this.item;
  }

  @Override
  public int getSlot() {
    return slot;
  }

  @Override
  public void use(Player player, User user) {
    player.openInventory(this.serversInventory.getInventory());
  }

}
