package exception;

public class SrtTimeHourOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "输入的时间单位\"小时\"超出范围";
    public SrtTimeHourOutOfBoundaryException() {
        super(msg);
    }
}