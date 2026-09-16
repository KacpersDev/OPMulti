package dev.kacperm.opmulti.multi;

public enum MultiType {

    MONEY("money"),
    TOKENS("tokens"),
    ANCIENT_COINS("ancient-coins"),
    ENCHANT("enchant"),
    PASS("pass"),
    ARMOR("armor"),
    TAMER("tamer");

    private final String key;

    MultiType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
