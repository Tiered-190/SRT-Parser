package exception;

public class NullOnOperateSrtNodesListenerException extends Exception{
    private static final String msg = "错误！没有srt节点操作监听器！";
    public NullOnOperateSrtNodesListenerException() {
        super(msg);
    }

    public NullOnOperateSrtNodesListenerException(Throwable cause) {
        super(msg,cause);
    }
}
