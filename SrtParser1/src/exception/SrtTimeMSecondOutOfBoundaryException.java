package exception;

public class SrtTimeMSecondOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "�����ʱ�䵥λ\"����\"������Χ";
    public SrtTimeMSecondOutOfBoundaryException() {
        super(msg);
    }
}