package listener;

import core.SrtNode;

import java.util.List;

public interface OnOperateSrtNodesListener {
	/*开始操作*/
    public void onOperationStart();
    
    /*操作成功*/
    public void onOperationSuccess(SrtNode node);
    public void onOperationSuccess(List<SrtNode> srtNodes);
    
    /*操作失败*/
    public void onOperationFail(Exception e);
}
