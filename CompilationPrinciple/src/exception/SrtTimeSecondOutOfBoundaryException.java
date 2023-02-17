package exception;

public class SrtTimeSecondOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "输入的时间单位\"秒\"超出范围";
    public SrtTimeSecondOutOfBoundaryException() {
        super(msg);
    }
}