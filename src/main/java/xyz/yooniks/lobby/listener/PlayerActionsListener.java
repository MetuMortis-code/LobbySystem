package xyz.yooniks.lobby.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.PlayerInventory;

public class PlayerActionsListener implements Listener {

  @EventHandler
  public void onPlace(BlockPlaceEvent event) {
    if (!event.getPlayer().isOp())
      event.setCancelled(true);
  }

  @EventHandler
  public void onBreak(BlockBreakEvent event) {
    if (!event.getPlayer().isOp())
      event.setCancelled(true);
  }

  @EventHandler
  public void onDrop(PlayerDropItemEvent event) {
    if (!event.getPlayer().isOp())
      event.setCancelled(true);
  }

  @EventHandler
  public void onPickup(PlayerPickupItemEvent event) {
    if (!event.getPlayer().isOp())
      event.setCancelled(true);
  }

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    if (event.getClickedInventory() instanceof PlayerInventory && !event.getWhoClicked().isOp()) {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onHunger(FoodLevelChangeEvent event) {
    event.setCancelled(true);
    event.setFoodLevel(20);
  }

  @EventHandler
  public void onDamage(EntityDamageEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onMobSpawn(CreatureSpawnEvent event) {
    event.setCancelled(true);
    event.getEntity().remove();
  }

  @EventHandler
  public void onThunder(WeatherChangeEvent event) {
    event.setCancelled(true);
    if (event.getWorld().isThundering())
      event.getWorld().setThundering(false);
    if (event.getWorld().hasStorm())
      event.getWorld().setStorm(false);
  }

}
