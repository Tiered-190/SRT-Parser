package exception;

public class NullOnOperateSrtNodesListenerException extends Exception{
    private static final String msg = "����û��srt�ڵ������������";
    public NullOnOperateSrtNodesListenerException() {
        super(msg);
    }

    public NullOnOperateSrtNodesListenerException(Throwable cause) {
        super(msg,cause);
    }
}
