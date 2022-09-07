package xyz.yooniks.lobby.user;

import com.keenant.tabbed.tablist.TabList;
import java.util.UUID;
import xyz.yooniks.lobby.user.premium.DiscoSettings;

public class User {

  private final UUID id;
  private String name;

  private VisibilityState visibility = VisibilityState.ENABLED;
  private TabList tabList;

  private long lastChatMessage;

  private DiscoSettings discoSettings;

  public User(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  public User(UUID id) {
    this.id = id;
  }

  public DiscoSettings getDiscoSettings() {
    return discoSettings;
  }

  public void setDiscoSettings(DiscoSettings discoSettings) {
    this.discoSettings = discoSettings;
  }

  public long getLastChatMessage() {
    return lastChatMessage;
  }

  public void setLastChatMessage(long lastChatMessage) {
    this.lastChatMessage = lastChatMessage;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public TabList getTabList() {
    return tabList;
  }

  public void setTabList(TabList tabList) {
    this.tabList = tabList;
  }

  public void setName(String name) {
    this.name = name;
  }

  public VisibilityState getVisibility() {
    return visibility;
  }

  public void setVisibility(VisibilityState visibility) {
    this.visibility = visibility;
  }

}
