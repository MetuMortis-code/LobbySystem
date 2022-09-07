package xyz.yooniks.lobby.helper;

import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class PacketUtil {

  private final static String packageName = Bukkit.getServer().getClass().getPackage().getName();
  private final static String version = packageName.substring(packageName.lastIndexOf(".") + 1);

  private PacketUtil() {
  }

  public static void sendEquipmentPacket(Player player, int id, int slot, ItemStack item) {
    try {
      final Class<?> packetClass = ReflectUtil.getCraftClass("PacketPlayOutEntityEquipment");
      final Object packet = packetClass.newInstance();
      ReflectUtil.setValue(ReflectUtil.getField(packet.getClass(), "a"), packet, id);
      ReflectUtil.setValue(ReflectUtil.getField(packet.getClass(), "b"), packet, slot);
      ReflectUtil
          .setValue(ReflectUtil.getField(packet.getClass(), "c"), packet, ReflectUtil.getMethod(
              ReflectUtil.getBukkitClass("inventory.CraftItemStack"), "asNMSCopy", ItemStack.class)
              .invoke(null, item));
      sendPacket(player, packet);
    } catch (Exception ignored) {}
  }

  public static void sendPacket(final Player player, final Object... os) {
    sendPacket(new Player[]{player}, os);
  }

  public static void sendPacket(final Player[] players, final Object... os) {
    try {
      final Class<?> packetClass = Class.forName("net.minecraft.server." + version
          + ".Packet"); //version bylo inaczej, jak nie bedzie dzialac to zmienic
      final Class<?> craftPlayer = Class
          .forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
      for (final Player p : players) {
        final Object cp = craftPlayer.cast(p);
        final Object handle = craftPlayer.getMethod("getHandle", (Class<?>[]) new Class[0])
            .invoke(cp, new Object[0]);
        final Object con = handle.getClass().getField("playerConnection").get(handle);
        final Method method = con.getClass().getMethod("sendPacket", packetClass);
        for (final Object o : os) {
          if (o != null) {
            method.invoke(con, o);
          }
        }
      }
    } catch (Exception ignored) {}
  }

}