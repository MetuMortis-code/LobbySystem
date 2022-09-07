package xyz.yooniks.lobby.server;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import xyz.yooniks.lobby.api.inventory.ItemBuilder;
import xyz.yooniks.lobby.api.message.MessageBuilder;
import xyz.yooniks.lobby.config.Settings;

public class ServerManager {

  private final Map<String, Server> serversMap = new HashMap<>();
  private final String BUNGEECORD_CHANNEL = "BungeeCord";

  private final Plugin plugin;

  public ServerManager(Plugin plugin) {
    this.plugin = plugin;
  }

  public ImmutableMap<String, Server> getServersMap() {
    return ImmutableMap.copyOf(this.serversMap);
  }

  public ImmutableList<Server> getServers() {
    return ImmutableList.copyOf(this.serversMap.values());
  }

  public void requestPlayerCount(String serverId) {
    if (!this.serversMap.containsKey(serverId)) {
      return;
    }
    final ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("PlayerCount");
    out.writeUTF(serverId);

    final Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
    if (player == null) {
      return;
    }
    player.sendPluginMessage(this.plugin, this.BUNGEECORD_CHANNEL, out.toByteArray());
  }

  public void load() {
    final FileConfiguration config = this.plugin.getConfig();
    final ConfigurationSection section = config.getConfigurationSection("servers.list");

    for (String id : section.getKeys(false)) {
      final ConfigurationSection data = section.getConfigurationSection(id);
      this.serversMap.put(id, new Server(id, ItemBuilder.withSection(data).build(), data.getInt("slot")));
    }
  }

  public void connect(Player player, String serverId) {
    player.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.CONNECTING_MESSAGE)
        .withField("{SERVER}", serverId)
        .prefix().placeholders(player).coloured().toString());

    final ByteArrayOutputStream b = new ByteArrayOutputStream();
    final DataOutputStream out = new DataOutputStream(b);

    try {
      out.writeUTF("Connect");
      out.writeUTF(serverId);
    } catch (IOException exception) {
      player.sendMessage(MessageBuilder.newBuilder(Settings.IMP.MESSAGES.COULD_NOT_CONNECT)
          .withField("{SERVER}", serverId)
          .withField("{REASON}", exception.getMessage())
          .prefix()
          .coloured()
              .placeholders(player).toString());
      return;
    }

    player.sendPluginMessage(this.plugin, this.BUNGEECORD_CHANNEL, b.toByteArray());
  }

  public Optional<Server> findBySlot(int slot) {
    return this.serversMap.values().stream()
        .filter(server -> server.getSlot() == slot)
        .findFirst();
  }

}
