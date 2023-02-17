package exception;

public class SrtTimeMinuteOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "输入的时间单位\"分钟\"超出范围";
    public SrtTimeMinuteOutOfBoundaryException() {
        super(msg);
    }
}