package exception;

public class NullOnLoadSrtFileListenerException extends Exception{
    private static final String msg = "错误！没有导入srt文件监听器！";
    public NullOnLoadSrtFileListenerException() {
        super(msg);
    }

    public NullOnLoadSrtFileListenerException(Throwable cause) {
        super(msg,cause);
    }
}
