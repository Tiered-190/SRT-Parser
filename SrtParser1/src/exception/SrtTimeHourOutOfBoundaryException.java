package exception;

public class SrtTimeHourOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "�����ʱ�䵥λ\"Сʱ\"������Χ";
    public SrtTimeHourOutOfBoundaryException() {
        super(msg);
    }
}