package xyz.yooniks.lobby.disco.setter;

import static xyz.yooniks.lobby.api.inventory.MessageHelper.colored;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.yooniks.lobby.LobbyPlugin;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.disco.DiscoType;
import xyz.yooniks.lobby.helper.PacketUtil;
import xyz.yooniks.lobby.user.premium.DiscoSettings;

public class DefaultDiscoSetter implements DiscoSetter {

  @Override
  public void sendNormalArmor(Player player, DiscoSettings settings) {
    final ItemStack[] armor = player.getInventory().getArmorContents();
    //send equip packets

    if (armor[0] == null) {
      armor[0] = new ItemStack(Material.AIR);
    }
    if (armor[1] == null) {
      armor[1] = new ItemStack(Material.AIR);
    }
    if (armor[2] == null) {
      armor[2] = new ItemStack(Material.AIR);
    }
    if (armor[3] == null) {
      armor[3] = new ItemStack(Material.AIR);
    }

    for (int slot = 1; slot < 5; ++slot) {
      //System.out.println("normalarmor, slot: " + slot + ", armortype: "+ armor[slot].getType().name());
      PacketUtil.sendEquipmentPacket(player, player.getEntityId(), slot, armor[slot - 1]);
    }
  }

  @Override
  public void createArmor(Player player, DiscoSettings settings, long delay) {
    settings.makeTask(new BukkitRunnable() {
      @Override
      public void run() {
        if (settings.currentDisco() == null || settings.currentDisco() == DiscoType.NONE) {
          this.cancel();
          return;
        }
        final ItemStack[] armor = armor(settings.findNextColor());
        //send equip packets

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
          /*if (onlinePlayer.getName().equalsIgnoreCase(player.getName())
              && !player.isSneaking()) {
            continue;
          }*/
          for (int slot = 1; slot < 5; slot++) {
            // System.out.println("createarmor, slot: " + slot + ", armortype: "+ armor[slot].getType().name());
            PacketUtil
                .sendEquipmentPacket(onlinePlayer, player.getEntityId(), slot - 1, armor[slot - 1]);
          }
        }
      }
    }.runTaskTimerAsynchronously(LobbyPlugin.getInstance(), delay, delay));
  }

  @Override
  public void removeArmor(Player player, DiscoSettings settings) {
    settings.findTask().ifPresent(BukkitTask::cancel);
    settings.makeTask(null);
  }

  private static final String ARMOR_NAME = colored(Settings.IMP.MESSAGES.ARMOR_NAME);

  private ItemStack[] armor(Color color) {

    final ItemStack helmetItem = new ItemStack(Material.LEATHER_HELMET);
    final LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmetItem.getItemMeta();
    helmetMeta.setColor(color);
    helmetMeta.setDisplayName(ARMOR_NAME);
    helmetItem.setItemMeta(helmetMeta);

    final ItemStack chestplateItem = new ItemStack(Material.LEATHER_CHESTPLATE);
    final LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplateItem.getItemMeta();
    chestplateMeta.setColor(color);
    chestplateMeta.setDisplayName(ARMOR_NAME);
    chestplateItem.setItemMeta(chestplateMeta);

    final ItemStack leggingsItem = new ItemStack(Material.LEATHER_LEGGINGS);
    final LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggingsItem.getItemMeta();
    leggingsMeta.setColor(color);
    leggingsMeta.setDisplayName(ARMOR_NAME);
    leggingsItem.setItemMeta(leggingsMeta);

    final ItemStack bootsItem = new ItemStack(Material.LEATHER_BOOTS);
    final LeatherArmorMeta bootsMeta = (LeatherArmorMeta) bootsItem.getItemMeta();
    bootsMeta.setColor(color);
    bootsMeta.setDisplayName(ARMOR_NAME);
    bootsItem.setItemMeta(bootsMeta);

    return new ItemStack[] {
        bootsItem, leggingsItem, chestplateItem, helmetItem
    };
  }

}
