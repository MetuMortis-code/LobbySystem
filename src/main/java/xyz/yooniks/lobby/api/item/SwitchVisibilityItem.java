package xyz.yooniks.lobby.api.item;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.config.Settings.MESSAGES.VISIBILITY;
import xyz.yooniks.lobby.user.User;
import xyz.yooniks.lobby.user.VisibilityState;

public class SwitchVisibilityItem implements HoldableItem {

  private final ItemStack visibilityShown, visibilityPremium, visibilityHidden;
  private final int slot;

  public SwitchVisibilityItem(ItemStack visibilityShown,
      ItemStack visibilityPremium, ItemStack visibilityHidden, int slot) {
    this.visibilityShown = visibilityShown;
    this.visibilityPremium = visibilityPremium;
    this.visibilityHidden = visibilityHidden;
    this.slot = slot;
  }

  @Override
  public ItemStack getItem(User user) {
    switch (user.getVisibility()) {
      case ENABLED: {
        return this.visibilityShown;
      }
      case PREMIUM_ONLY: {
        return this.visibilityPremium;
      }
      default: {
        return this.visibilityHidden;
      }
    }
  }

  @Override
  public int getSlot() {
    return slot;
  }

  @Override
  public void use(Player player, User user) {
    final VISIBILITY visibility = Settings.IMP.MESSAGES.VISIBILITY;

    switch (user.getVisibility()) {
      case ENABLED: {
        user.setVisibility(VisibilityState.PREMIUM_ONLY);
        player.sendMessage(MessageBuilder.newBuilder(visibility.SHOWN_PREMIUM).prefix().coloured().toString());
        player.getInventory().setItem(this.slot, this.visibilityPremium);

        for (Player online : Bukkit.getOnlinePlayers()) {
          if (!online.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION))
            player.hidePlayer(online);
        }
        break;
      }
      case PREMIUM_ONLY: {
        user.setVisibility(VisibilityState.DISABLED);
        player.sendMessage(MessageBuilder.newBuilder(visibility.HIDDEN).prefix().coloured().toString());
        player.getInventory().setItem(this.slot, this.visibilityHidden);

        for (Player online : Bukkit.getOnlinePlayers()) {
          player.hidePlayer(online);
        }
        break;
      }
      default: {
        user.setVisibility(VisibilityState.ENABLED);
        player.sendMessage(MessageBuilder.newBuilder(visibility.SHOWN_ALL).prefix().coloured().toString());
        player.getInventory().setItem(this.slot, this.visibilityShown);

        for (Player online : Bukkit.getOnlinePlayers()) {
          player.showPlayer(online);
        }
        break;
      }
    }
  }

}
