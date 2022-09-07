package xyz.yooniks.lobby.api.actionbar;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Collection;
import java.util.UUID;
import org.bukkit.entity.Player;

public class ActionbarMessageManager {

  private final Multimap<UUID, ActionbarMessage> messageMap = ArrayListMultimap.create();

  public void addMessage(Player player, ActionbarMessage message) {
    if (this.messageMap.containsKey(player.getUniqueId())) {
      for (ActionbarMessage anotherMessage : this.messageMap.get(player.getUniqueId())) {
        if (anotherMessage.getId().equalsIgnoreCase(message.getId())) {
          return;
        }
      }
    }
    this.messageMap.put(player.getUniqueId(), message);
  }

  public synchronized void removeMessage(UUID uuid, ActionbarMessage message) {
    this.messageMap.remove(uuid, message);
  }

  public synchronized Collection<ActionbarMessage> removeMessages(UUID uuid) {
    return this.messageMap.removeAll(uuid);
  }

  public Multimap<UUID, ActionbarMessage> getMessages() {
    return messageMap;
  }

}
