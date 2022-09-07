package xyz.yooniks.lobby.disco;


public enum DiscoType {

  SMOOTH,
  RANDOM,
  ULTRA,
  FAST,
  CURRENT,
  DISABLE,
  NONE;

  static DiscoType findByName(String name) {
    for (DiscoType type : values()) {
      if (name.equalsIgnoreCase(type.name())) {
        return type;
      }
    }
    return SMOOTH;
  }

}
