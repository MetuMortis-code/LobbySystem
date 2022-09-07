package xyz.yooniks.lobby.server;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class ServerCountListener implements PluginMessageListener {

  private final ServerManager serverManager;

  public ServerCountListener(ServerManager serverManager) {
    this.serverManager = serverManager;
  }

  @Override
  public synchronized void onPluginMessageReceived(String channel, Player player, byte[] message) {
    if (!channel.equals("BungeeCord")) {
      return;
    }

    try {

      final ByteArrayDataInput in = ByteStreams.newDataInput(message);
      final String subchannel = in.readUTF();

      if (subchannel.equals("PlayerCount")) {
        final String serverId = in.readUTF();
        final int count = in.readInt();

        final Server server = this.serverManager.getServersMap().get(serverId);
        if (server == null) {
          return;
        }
        server.setOnline(count);
      }
    } catch (Exception ignored) {
    }
  }

}
