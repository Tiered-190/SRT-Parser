package exception;

public class SrtTimeMinuteOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "�����ʱ�䵥λ\"����\"������Χ";
    public SrtTimeMinuteOutOfBoundaryException() {
        super(msg);
    }
}