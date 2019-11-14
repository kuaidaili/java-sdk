package kdl.enums;

public enum OpsOrderLevel {
    NORMAL("dev"),
    VIP("dev"),
    SVIP("svip"),
    PRO("ent");

    private String value;

    private OpsOrderLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
