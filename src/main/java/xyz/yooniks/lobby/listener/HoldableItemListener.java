package xyz.yooniks.lobby.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.yooniks.lobby.api.item.HoldableItemManager;
import xyz.yooniks.lobby.user.User;
import xyz.yooniks.lobby.user.UserManager;

public class HoldableItemListener implements Listener {

  private final HoldableItemManager itemManager;
  private final UserManager userManager;

  public HoldableItemListener(HoldableItemManager itemManager,
      UserManager userManager) {
    this.itemManager = itemManager;
    this.userManager = userManager;
  }

  @EventHandler
  public void onUse(PlayerInteractEvent event) {
    final Action action = event.getAction();
    if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) return;

    final Player player = event.getPlayer();
    final int slot = player.getInventory().getHeldItemSlot();

    final User user = this.userManager.findOrCreate(player.getUniqueId(), player.getName());
    this.itemManager.findBySlot(slot).ifPresent((item) -> item.use(player, user));
  }

}
