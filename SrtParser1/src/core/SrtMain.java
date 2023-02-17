package core;

import exception.NullOnLoadSrtFileListenerException;
import exception.NullOnOperateSrtNodesListenerException;
import listener.OnLoadSrtFileListener;
import listener.OnOperateSrtNodesListener;
import listener.OnSaveSrtFileListener;
import operation.SrtOperator;
import operation.SrtOperatorFactory;

import javax.management.JMException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SrtMain {
	/**
	 * �� srt �ļ����н����Լ������ֲ�����ͳ����
	 * */
	
	private List<SrtNode> srtNodeLinkList = new LinkedList<SrtNode>(); //ʹ������洢
	private OnLoadSrtFileListener onLoadSrtFileListener;
	private String srtFilePath;
	private static String LINE_SEPARATOR = System.lineSeparator();

	public SrtMain() {
//		��srt��Ļ�ļ����ؽ�����洢
		LoadNodes();
	}

	public void LoadNodes(){
	}

	//���� srt �ļ�������·����
	public void loadSrtFile(String path){
		if(onLoadSrtFileListener == null){
			loadSrtFileFail(new NullOnLoadSrtFileListenerException());
		}
		this.srtFilePath = path;

		loadSrtFileStart();

		SrtParser parser = new SrtParser();
		File file=new File(path);
		
		try {
			parser.execute(file, new OnLoadSrtFileListener() {
				@Override
				public void onLoadSrtFileStart() {
					loadSrtFileStart();
				}

				@Override
				public void onLoadSrtFileSuccess(List<SrtNode> list) {
					if(!srtNodeLinkList.isEmpty()){
						srtNodeLinkList.clear();
					}
					srtNodeLinkList = list;
					loadSrtFileSuccess();
				}

				@Override
				public void onLoadSrtFileFail(Exception e) {
					loadSrtFileFail(e);
				}
			});
		} catch (FileNotFoundException e) {
			onLoadSrtFileListener.onLoadSrtFileFail(e);
		}
	}

	//���� srt �ļ�
	public void saveSrtFile(OnSaveSrtFileListener listener){
		File file = new File(this.srtFilePath);
//		����ת��
		String content = convertString(srtNodeLinkList);

		try {
//			д���ļ����޸ĺ󸲸�ԭ�ļ���
			Files.write(file.toPath(), content.getBytes());
		}catch (IOException e){
			listener.onSaveSrtFileFail(e);
		}
		listener.onSaveSrtFileSuccess();
	}

	public void saveSrtFile(String filePath,OnSaveSrtFileListener listener){
		File file = new File(filePath);
		String content = convertString(srtNodeLinkList);
		try {
			Files.write(file.toPath(), content.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	����������ת�����ַ��ͣ�������
	public static String convertString(List<SrtNode> list){
		String str = "";
		Iterator it = list.iterator();
		while (it.hasNext()){
			SrtNode node = (SrtNode)it.next();
			str+=convertString(node);
		}
		return str;
	}

//	�����ϵ������ת�����ַ���
	public static String convertString(SrtNode node){
		String str = "";
		str+=String.format("%d\n",node.getSid());
		str+=String.format("%s --> %s\n",node.getBegin().toString(),node.getEnd().toString());
		str+=String.format("%s\n",node.getContent());
		str+=LINE_SEPARATOR;
		return str;
	}

	//��������������
	public void operateSrtNodes(String opName, HashMap<String,Object> parameter, OnOperateSrtNodesListener listener) {
		try{
			//���ò���
			SrtOperator operator = SrtOperatorFactory.getSrtOperator(opName,parameter);
			operator.execute(srtNodeLinkList,listener);
		} catch (NullOnOperateSrtNodesListenerException e) {
			listener.onOperationFail(e);
			//e.printStackTrace();
		} catch (JMException e) {
			listener.onOperationFail(e);
			//e.printStackTrace();
		}
	}

	public void loadSrtFileStart() {
		this.onLoadSrtFileListener.onLoadSrtFileStart();
	}

	public void loadSrtFileSuccess() {
		this.onLoadSrtFileListener.onLoadSrtFileSuccess(srtNodeLinkList);
	}

	public void loadSrtFileFail(Exception e) {
		this.onLoadSrtFileListener.onLoadSrtFileFail(e);
	}

	public String getSrtFilePath() {
		return srtFilePath;
	}

	public void setSrtFilePath(String srtFilePath) {
		this.srtFilePath = srtFilePath;
	}

	public OnLoadSrtFileListener getOnLoadSrtFileListener() {
		return onLoadSrtFileListener;
	}

	public void setOnLoadSrtFileListener(OnLoadSrtFileListener onLoadSrtFileListener) {
		this.onLoadSrtFileListener = onLoadSrtFileListener;
	}

	//��ȡ�����С
	public int getSrtNodeListSize(){
		return this.srtNodeLinkList.size();
	}

	public void printSrtNodeList(){
		System.out.println(SrtMain.convertString(srtNodeLinkList));
	}

	public void printSrtNode(SrtNode node){
		System.out.println(SrtMain.convertString(node));
	}
}
