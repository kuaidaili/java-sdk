package kdl.enums;

/**
 * 开放代理订单级别
 */

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
