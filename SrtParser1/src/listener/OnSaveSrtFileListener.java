package listener;

import core.SrtNode;

import java.util.List;

public interface OnSaveSrtFileListener {
	/*����srt��Ļ�ļ��ɹ�*/
	public void onSaveSrtFileSuccess();

	/*����srt��Ļ�ļ�ʧ��*/
	public void onSaveSrtFileFail(Exception e);
}
