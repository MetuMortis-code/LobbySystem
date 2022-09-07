package xyz.yooniks.lobby.listener;

import static xyz.yooniks.lobby.api.inventory.MessageHelper.colored;

import com.keenant.tabbed.Tabbed;
import com.keenant.tabbed.tablist.SimpleTabList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import xyz.yooniks.lobby.api.item.HoldableItemManager;
import xyz.yooniks.lobby.config.Settings;
import xyz.yooniks.lobby.event.UserInitializationEvent;
import xyz.yooniks.lobby.helper.ColorUtil;
import xyz.yooniks.lobby.user.User;
import xyz.yooniks.lobby.user.premium.DiscoSettings;

public class UserInitializationListener implements Listener {

  private final Tabbed tabbed;
  private final HoldableItemManager itemManager;

  public UserInitializationListener(Tabbed tabbed,
      HoldableItemManager itemManager) {
    this.tabbed = tabbed;
    this.itemManager = itemManager;
  }

  @EventHandler
  public void onUserInitialize(UserInitializationEvent event) {
    final Player player = event.getPlayer();

    final User user = event.getUser();

    if (this.tabbed != null) {
      final SimpleTabList tabList = this.tabbed
          .newSimpleTabList(player, 80, -1);

      tabList.enable();
      tabList.update();

      user.setTabList(tabList);
    }

    this.itemManager.putItems(player, user);
    if (player.hasPermission(Settings.IMP.MESSAGES.PREMIUM_PERMISSION) || player.isOp()) {
      user.setDiscoSettings(new DiscoSettings());
    }
    this.armorEquip(player);
  }

  private void armorEquip(Player player) {
    final ItemStack helmetItem = new ItemStack(Material.LEATHER_HELMET);
    final LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmetItem.getItemMeta();
    helmetMeta.setDisplayName(colored(Settings.IMP.MESSAGES.ARMOR_NAME));
    helmetItem.setItemMeta(helmetMeta);

    final ItemStack chestplateItem = new ItemStack(Material.LEATHER_CHESTPLATE);
    final LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplateItem.getItemMeta();
    chestplateMeta.setDisplayName(colored(Settings.IMP.MESSAGES.ARMOR_NAME));
    chestplateMeta.setColor(ColorUtil.randomColor());
    chestplateItem.setItemMeta(chestplateMeta);

    final ItemStack leggingsItem = new ItemStack(Material.LEATHER_LEGGINGS);
    final LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggingsItem.getItemMeta();
    leggingsMeta.setDisplayName(colored(Settings.IMP.MESSAGES.ARMOR_NAME));
    leggingsItem.setItemMeta(leggingsMeta);

    final ItemStack bootsItem = new ItemStack(Material.LEATHER_BOOTS);
    final LeatherArmorMeta bootsMeta = (LeatherArmorMeta) bootsItem.getItemMeta();
    bootsMeta.setDisplayName(colored(Settings.IMP.MESSAGES.ARMOR_NAME));
    bootsItem.setItemMeta(bootsMeta);

    player.getInventory().setArmorContents(new ItemStack[] {
       bootsItem, leggingsItem, chestplateItem, helmetItem});
  }

}
