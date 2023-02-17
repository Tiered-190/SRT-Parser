package core;

import exception.SrtFileParseException;
import listener.OnLoadSrtFileListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class SrtParser {

	/**
	 * ��������ڽ�����Ļ�ļ����� srt �ļ�ת����������ʽ�洢
	 * */
	
     public static void execute(File file, OnLoadSrtFileListener listener) throws FileNotFoundException {
		/**
		 * �൱������������Ƚ�������Ļ�ļ����ֳ�һ����������Ļ��
		 * �ٶ�ÿһ����Ļ����Ӧʱ�������з������з֣����洢����Ļ�����
		 * */
    	 
        try(Scanner input=new Scanner(file)) {
            List<SrtNode> srtNodeList = new LinkedList<>();

            listener.onLoadSrtFileStart();
            //��ʼ��ȡ srt �ļ�����ÿһ����Ļ�� ArrayList ��ʽ���
            while (input.hasNextLine()){
                ArrayList<String> node = new ArrayList<>();
                String str = input.nextLine();
                //һ����Ļ��sid��times��content����Ӧһ��node
                while(!str.isEmpty()){
                    node.add(str);
                    if(input.hasNextLine()){
                        str = input.nextLine();
                    }else{
                        break;
                    }
                }
                //�� String ת���� SrtNode
                SrtNode srtNode = parseStrToSrtNode(node);
                //��������Ļ��㴮������
                srtNodeList.add(srtNode);
            }
            listener.onLoadSrtFileSuccess(srtNodeList);
        } catch (SrtFileParseException e) {
            listener.onLoadSrtFileFail(e);
        }

     }

   //�� String ת���� SrtNode���൱�ھ�����������
    public static SrtNode parseStrToSrtNode(ArrayList<String> strNode) throws SrtFileParseException {
        SrtNode node = new SrtNode();
        if(strNode.size()>2){

            SrtTime begin = new SrtTime();
            SrtTime end = new SrtTime();

            try{
                String content = "";
                for(int index = 2; index < strNode.size(); index++){
                	//�����Ļ�����ı�����
                    content+=strNode.get(index);
                }

                //����ʼʱ�������ʱ��ָ���
                String[] timeStr = strNode.get(1).split("-->");
                //����ʼʱ���µ�ʱ���֡��롢����ָ���
                String[] beginStr = timeStr[0].split(":");
                //������ʱ���µ�ʱ���֡��롢����ָ���
                String[] endStr = timeStr[1].split(":");

                begin.setHour(Integer.parseInt(beginStr[0].trim()));
                begin.setMinute(Integer.parseInt(beginStr[1].trim()));
                //�ָ���ͺ���
                begin.setSecond(Integer.parseInt(beginStr[2].split(",")[0].trim()));
                begin.setMsecond(Integer.parseInt(beginStr[2].split(",")[1].trim()));

                end.setHour(Integer.parseInt(endStr[0].trim()));
                end.setMinute(Integer.parseInt(endStr[1].trim()));
                //�ָ���ͺ���
                end.setSecond(Integer.parseInt(endStr[2].split(",")[0].trim()));
                end.setMsecond(Integer.parseInt(endStr[2].split(",")[1].trim()));

                node.setSid(Integer.parseInt(strNode.get(0).trim()));

                node.setContent(content);

                node.setBegin(begin);

                node.setEnd(end);
            }catch (Exception e){
                throw new SrtFileParseException(e.toString());
            }
        }
        return node;
    }
}
