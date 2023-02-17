package listener;

import core.SrtNode;

import java.util.List;

public interface OnOperateSrtNodesListener {
	/*��ʼ����*/
    public void onOperationStart();
    
    /*�����ɹ�*/
    public void onOperationSuccess(SrtNode node);
    public void onOperationSuccess(List<SrtNode> srtNodes);
    
    /*����ʧ��*/
    public void onOperationFail(Exception e);
}
