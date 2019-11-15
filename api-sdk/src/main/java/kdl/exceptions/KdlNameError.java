package kdl.exceptions;

/**
 * 参数异常类
 */
public class KdlNameError extends KdlException {
    public KdlNameError(String message) {
        super(-2, message);
        this.setHintMessage(String.format("[KdlNameError] message: %s", this.getMessage()));
    }
}
