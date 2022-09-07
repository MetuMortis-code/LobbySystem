package xyz.yooniks.lobby.api.message;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.yooniks.lobby.LobbyPlugin;
import xyz.yooniks.lobby.api.hook.PlaceholderAPIHook;
import xyz.yooniks.lobby.config.Settings;

public class MessageBuilder {

  private String text;

  public MessageBuilder(String text) {
    this.text = text;
  }

  public static MessageBuilder newBuilder(String text) {
    return new MessageBuilder(text);
  }

  public MessageBuilder withField(String field, String value) {
    this.text = StringUtils.replace(this.text, field, value);
    return this;
  }

  public MessageBuilder coloured() {
    this.text = ChatColor.translateAlternateColorCodes('&', text);
    return this;
  }

  public MessageBuilder placeholders(Player player) {
    if (LobbyPlugin.PAPI) {
      this.text = PlaceholderAPIHook.withPlaceholders(player, this.text);
    }
    return this;
  }

  public MessageBuilder stripped() {
    return withField("%nl%", "\n");
  }

  public MessageBuilder prefix() {
    return withField("{PREFIX}", Settings.IMP.MESSAGES.PREFIX);
  }

  @Override
  public String toString() {
    return this.text;
  }

}
