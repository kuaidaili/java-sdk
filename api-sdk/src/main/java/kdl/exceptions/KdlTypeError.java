package kdl.exceptions;

/**
 * 类型异常类
 */
public class KdlTypeError extends KdlException {
    public KdlTypeError(String message) {
        super(-1, message);
        this.setHintMessage(String.format("[KdlTypeError] message: %s", this.getMessage()));
    }
}
