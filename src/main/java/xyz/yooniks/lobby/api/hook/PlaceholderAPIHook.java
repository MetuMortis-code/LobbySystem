package xyz.yooniks.lobby.api.hook;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import xyz.yooniks.lobby.LobbyPlugin;

public class PlaceholderAPIHook extends PlaceholderExpansion {

  private final LobbyPlugin plugin;

  public static boolean EXECUTED = false;

  public PlaceholderAPIHook(LobbyPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean register() {
    EXECUTED = true;
    return super.register();
  }

  public static String withPlaceholders(Player player, String text) {
    return EXECUTED ? PlaceholderAPI.setPlaceholders(player, text) : text;
  }

  @Override
  public String getIdentifier() {
    return "valhalla-lobby";
  }

  @Override
  public String getPlugin() {
    return this.plugin.getDescription().getName();
  }

  @Override
  public String getAuthor() {
    return "yooniks";
  }

  @Override
  public String getVersion() {
    return this.plugin.getDescription().getVersion();
  }

  @Override
  public String onPlaceholderRequest(Player player, String identifier) {

    if (player == null) {
      return "";
    }

    return "";
  }

}
