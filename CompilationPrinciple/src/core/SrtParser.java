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
	 * 这个类用于解析字幕文件，将 srt 文件转换成链表形式存储
	 * */
	
     public static void execute(File file, OnLoadSrtFileListener listener) throws FileNotFoundException {
		/**
		 * 相当于语义分析，先将整个字幕文件划分成一条条独立字幕，
		 * 再对每一条字幕所对应时间条进行分析、切分，并存储在字幕结点中
		 * */
    	 
        try(Scanner input=new Scanner(file)) {
            List<SrtNode> srtNodeList = new LinkedList<>();

            listener.onLoadSrtFileStart();
            //开始读取 srt 文件，将每一条字幕用 ArrayList 形式存放
            while (input.hasNextLine()){
                ArrayList<String> node = new ArrayList<>();
                String str = input.nextLine();
                //一条字幕（sid、times、content）对应一个node
                while(!str.isEmpty()){
                    node.add(str);
                    if(input.hasNextLine()){
                        str = input.nextLine();
                    }else{
                        break;
                    }
                }
                //将 String 转换成 SrtNode
                SrtNode srtNode = parseStrToSrtNode(node);
                //将所有字幕结点串成链表
                srtNodeList.add(srtNode);
            }
            listener.onLoadSrtFileSuccess(srtNodeList);
        } catch (SrtFileParseException e) {
            listener.onLoadSrtFileFail(e);
        }

     }

   //将 String 转换成 SrtNode，相当于具体的语义分析
    public static SrtNode parseStrToSrtNode(ArrayList<String> strNode) throws SrtFileParseException {
        SrtNode node = new SrtNode();
        if(strNode.size()>2){

            SrtTime begin = new SrtTime();
            SrtTime end = new SrtTime();

            try{
                String content = "";
                for(int index = 2; index < strNode.size(); index++){
                	//存放字幕具体文本内容
                    content+=strNode.get(index);
                }

                //将开始时间与结束时间分隔开
                String[] timeStr = strNode.get(1).split("-->");
                //将开始时间下的时、分、秒、毫秒分隔开
                String[] beginStr = timeStr[0].split(":");
                //将结束时间下的时、分、秒、毫秒分隔开
                String[] endStr = timeStr[1].split(":");

                begin.setHour(Integer.parseInt(beginStr[0].trim()));
                begin.setMinute(Integer.parseInt(beginStr[1].trim()));
                //分隔秒和毫秒
                begin.setSecond(Integer.parseInt(beginStr[2].split(",")[0].trim()));
                begin.setMsecond(Integer.parseInt(beginStr[2].split(",")[1].trim()));

                end.setHour(Integer.parseInt(endStr[0].trim()));
                end.setMinute(Integer.parseInt(endStr[1].trim()));
                //分隔秒和毫秒
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
