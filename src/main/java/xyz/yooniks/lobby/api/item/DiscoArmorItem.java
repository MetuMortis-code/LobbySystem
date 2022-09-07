package xyz.yooniks.lobby.api.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.disco.DiscoInventory;
import xyz.yooniks.lobby.server.ServersInventory;
import xyz.yooniks.lobby.user.User;

public class DiscoArmorItem implements HoldableItem {

  private final ItemStack item;
  private final int slot;

  private final DiscoInventory discoInventory;

  public DiscoArmorItem(ItemStack item, int slot,
      DiscoInventory discoInventory) {
    this.item = item;
    this.slot = slot;
    this.discoInventory = discoInventory;
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
    if (player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION) || player.isOp()) {
      this.discoInventory.open(player);
    }
    else {
      player.sendMessage(
          MessageBuilder.newBuilder(Settings.IMP.MESSAGES.DISCO_ARMOR.NO_PERMISSION).prefix().coloured().toString());
    }
  }

}
