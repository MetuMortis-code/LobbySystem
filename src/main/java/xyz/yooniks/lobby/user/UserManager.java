package xyz.yooniks.lobby.user;

import com.google.common.collect.ImmutableList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class UserManager {

  private final Map<UUID, User> userMap = new HashMap<>();

  public User findOrCreate(UUID uuid, String name) {
    User user = this.userMap.get(uuid);
    if (user == null) {
      this.userMap.put(uuid, user = new User(uuid, name));
    }
    return user;
  }

  public User findOrCreate(UUID uuid) {
    User user = this.userMap.get(uuid);
    if (user == null) {
      this.userMap.put(uuid, user = new User(uuid));
    }
    return user;
  }

  public List<User> getUsers() {
    return ImmutableList.copyOf(this.userMap.values());
  }

}
