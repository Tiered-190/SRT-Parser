package exception;

public class SrtTimeSecondOutOfBoundaryException extends SrtTimeOutOfBoundaryException {
    public static final String msg = "��������������ʾ�������ʱ�䵥λ\"��\"������Χ";
    public SrtTimeSecondOutOfBoundaryException() {
        super(msg);
    }
}