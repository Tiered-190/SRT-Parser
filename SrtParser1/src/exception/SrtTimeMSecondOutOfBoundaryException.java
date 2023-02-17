package exception;

public class SrtTimeMSecondOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "输入的时间单位\"毫秒\"超出范围";
    public SrtTimeMSecondOutOfBoundaryException() {
        super(msg);
    }
}