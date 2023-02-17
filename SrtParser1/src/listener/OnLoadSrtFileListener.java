package listener;

import core.SrtNode;

import java.util.List;

public interface OnLoadSrtFileListener {
	/*��ʼ��ȡsrt��Ļ�ļ�*/
	public void onLoadSrtFileStart();

	/*��ȡsrt��Ļ�ļ�����ҳɹ�*/
	public void onLoadSrtFileSuccess(List<SrtNode> list);

	/*��ʼ��ȡsrt��Ļ�ļ�ʧ��*/
	public void onLoadSrtFileFail(Exception e);
}
