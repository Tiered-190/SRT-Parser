package exception;

public class NullOnLoadSrtFileListenerException extends Exception{
    private static final String msg = "����ʧ����ʾ��û�е���srt�ļ�������";
    public NullOnLoadSrtFileListenerException() {
        super(msg);
    }

    public NullOnLoadSrtFileListenerException(Throwable cause) {
        super(msg,cause);
    }
}
