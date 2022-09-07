package xyz.yooniks.lobby.api.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.disco.DiscoInventory;
import xyz.yooniks.lobby.user.User;

public class FlyItem implements HoldableItem {

  private final ItemStack item;
  private final int slot;


  public FlyItem(ItemStack item, int slot) {
    this.item = item;
    this.slot = slot;
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
      if (player.getAllowFlight()) {
        player.setAllowFlight(false);
        player.sendMessage(
            MessageBuilder.newBuilder(Settings.IMP.MESSAGES.FLY_DISABLED).prefix().coloured().toString());
        return;
      }
      player.setAllowFlight(true);
      player.sendMessage(
          MessageBuilder.newBuilder(Settings.IMP.MESSAGES.FLY_ENABLED).prefix().coloured().toString());
    }
    else {
      player.sendMessage(
          MessageBuilder.newBuilder(Settings.IMP.MESSAGES.FLY_NO_PERMISSION).prefix().coloured().toString());
    }
  }

}
