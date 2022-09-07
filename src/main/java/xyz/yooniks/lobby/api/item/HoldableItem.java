package xyz.yooniks.lobby.api.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.yooniks.lobby.user.User;

public interface HoldableItem {

  ItemStack getItem(User user);

  int getSlot();

  void use(Player player, User user);

}
