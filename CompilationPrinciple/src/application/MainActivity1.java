package application;
import core.SrtNode;
import core.SrtTime;
import exception.NullOnLoadSrtFileListenerException;
import exception.NullOnOperateSrtNodesListenerException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import listener.OnLoadSrtFileListener;
import core.SrtMain;
import listener.OnOperateSrtNodesListener;
import listener.OnSaveSrtFileListener;
import operation.SrtOperator;
import operation.SrtOperatorFactory;

import javax.management.JMException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class MainActivity1 {
	
	//
	@FXML
    private TextField captionPath;
	@FXML
    private TextArea printingBoard;
	@FXML
    private Label tips;
	@FXML
    private Button exitBtn;
	@FXML
    private TextField moveOverallTimeInput;
	@FXML
    private Button findACaptionBtn;
	@FXML
    private TextField sidInput;
	@FXML
    private TextArea printBoard2;
	@FXML
    private TextField findACaptionInputS;
    @FXML
    private TextField findACaptionInputM;
    @FXML
    private TextField findACaptionInputH;
    @FXML
    private TextField findACaptionInputMS;
    @FXML
    private Button goBack;
    @FXML
    private TextField moveACaptionTime;
	
	
	//新定义的一些参数
	//用来记录所有的返回值
	public static String path = "0\0";//还是得有一个正确的路径才行
	public static String bigText;
	public static int operationSelect;
	public static String input1;
	public static int input2;
	public static int SIDinput;
	public static int searchOp = -1;
	public static int Hinput;
	public static int Minput;
	public static int Sinput;
	public static int MSinput;
	public static int moveACaptionTimeInput;
	public static int singleNodeMove = 0;//是否调用移动某一条字幕的函数
	public static int singleNodeOp = 0;
	
	
	public static final int OPERTION_INIT = -1;
	private static final int OPERTION_EXIT = 0;
	public static final int OPERTION_1 = 1;
	public static final int OPERTION_2 = 2;
	public static final int OPERTION_3 = 3;
	public static final int OPERTION_4 = 4;
	public static final int OPERTION_5 = 5;

	public static final int SEARCH_OPERTION_1 = 1;
	public static final int SEARCH_OPERTION_2 = 2;

	public static final int SINGLE_NODE_OPERTION_1 = 1;
	
	public static SrtMain srtMain = new SrtMain();

	public void Hello() throws NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException, FileNotFoundException {
		// 1.读入字幕文件
		// 2.打印可用操作
		// 3.选择一个操作，可用操作有：
			//a.字幕整体前移,输入一个00:00:00,000格式的数字
			//b.字幕整体后移,输入一个00:00:00,000格式的数字
		// 4.调用操作器对应功能，传入参数
		// 5.操作器执行对应操作
		// 6.操作器调取SrtAnalyzer

//		SrtMain srtMain = new SrtMain();

		srtMain.setOnLoadSrtFileListener(new OnLoadSrtFileListener() {
			public void onLoadSrtFileStart() {

			}
			@Override
			public void onLoadSrtFileSuccess(List<SrtNode> list) {
				System.out.println("读取srt文件成功！共有字幕"+srtMain.getSrtNodeListSize()+"条");
				try{
					doLoop(srtMain);
				}catch (Exception e){
					e.printStackTrace();
				}

			}
			public void onLoadSrtFileFail(Exception e) {
				System.out.println("读取srt文件失败！原因："+e.toString());
				System.out.println("请尝试重新输入");
				srtMain.loadSrtFile(path);
			}
		});

		//1、输入 srt 文件路径
		srtMain.loadSrtFile(path);
	}
	
	public static void doLoop(SrtMain srtMain) throws NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException, InterruptedException {

				int op = OPERTION_INIT;
					//2、只要没有退出程序，每条指令结束后都会打印  可选择操作菜单（这个删掉）
					printAvailableOperator();

					op = operationSelect;
					
					//选择的操作判断
					switch (op){
						case OPERTION_1: //时间轴平移
							HashMap<String,Object> param = scanInTimeShiftParam(input1,input2);

							srtMain.operateSrtNodes("SrtTimeShift",param,new OnOperateSrtNodesListener() {
								@Override
								public void onOperationStart() {

								}

								@Override
								public void onOperationSuccess(SrtNode node) {

								}

								@Override
								public void onOperationSuccess(List<SrtNode> srtNodeList) {
									System.out.println("成功移动所有字幕的时间轴！");
									//srtMain.printSrtNodeList();
								}

								@Override
								public void onOperationFail(Exception e) {
									System.out.println("移动字幕时间轴失败！原因："+e.toString());
								}
							});
							;
						
							break;
							
						case OPERTION_2://查找某条字幕
							printAvailableSearchOperator();
							System.out.println("选择查找方式：");

							
							HashMap<String,Object> searchParam = new HashMap<>();
							switch (searchOp){
								case SEARCH_OPERTION_1://根据 sid 查找
									searchParam.put("sid",SIDinput);
									break;
								case SEARCH_OPERTION_2://根据时间查找
									try{
										searchParam.put("hour",Hinput);
										searchParam.put("minute",Minput);
										searchParam.put("second",Sinput);
										searchParam.put("msecond",MSinput);

									}catch (Exception e){
										System.out.println("输入有误！"+e.toString());
//										continue;
									}

							}
							srtMain.operateSrtNodes("SrtNodeSearch", searchParam, new OnOperateSrtNodesListener() {
								@Override
								public void onOperationStart() {
								}

								@Override
								public void onOperationSuccess(SrtNode node) {
									System.out.println("查找成功！该条字幕信息如下：");
									bigText = srtMain.printSrtNode(node);

//									int singleNodeOp = OPERTION_INIT;
//									while(singleNodeOp != OPERTION_EXIT){
//										printAvailableSingleNodeOperator();
//										singleNodeOp = moveACaptionTimeInput;//本来是输入的地方
										
										singleNodeOp = singleNodeMove;
										//对单条字幕进行操作
										switch (singleNodeOp){
											case SINGLE_NODE_OPERTION_1://对单条字幕进行时间轴平移
												HashMap<String,Object> shiftParam = scanInTimeShiftParam(input1,input2);
												shiftParam.put("startNode",node);
												srtMain.operateSrtNodes("SrtTimeShift", shiftParam , new OnOperateSrtNodesListener() {
													@Override
													public void onOperationStart() {

													}

													@Override
													public void onOperationSuccess(SrtNode node) {

													}

													@Override
													public void onOperationSuccess(List<SrtNode> srtNodes) {
														System.out.println("成功移动该字和受影响的字幕的时间轴！");
													}

													@Override
													public void onOperationFail(Exception e) {
														System.out.println("移动字幕时间轴失败！原因："+e.toString());
													}
												});
												;
												
												break;
											default:
												break;
										}
//									}
								}

								@Override
								public void onOperationSuccess(List<SrtNode> srtNodes) {

								}

								@Override
								public void onOperationFail(Exception e) {
									System.out.println("查找失败！原因："+e.toString());
								}
							});
						
							;
							
							break;
							
						case OPERTION_3://更换字幕文件（切换路径）
							srtMain.setOnLoadSrtFileListener(new OnLoadSrtFileListener() {
								@Override
								public void onLoadSrtFileStart() {

								}

								@Override
								public void onLoadSrtFileSuccess(List<SrtNode> list) {
									System.out.println("读取srt文件成功！共有字幕"+srtMain.getSrtNodeListSize()+"条");
								}

								@Override
								public void onLoadSrtFileFail(Exception e) {
									System.out.println("读取srt文件失败！原因："+e.toString());
									System.out.println("请尝试重新输入");
									srtMain.loadSrtFile(path);
								}
							});
							srtMain.loadSrtFile(path);
						;

							
							break;
							
						case OPERTION_4://更新 srt 文件
							srtMain.saveSrtFile(new OnSaveSrtFileListener() {
								@Override
								public void onSaveSrtFileSuccess() {
									System.out.println("保存成功！保存路径："+srtMain.getSrtFilePath());
								}

								@Override
								public void onSaveSrtFileFail(Exception e) {
									System.out.println("保存失败!原因："+e.toString());
								}
							});
							
							break;
							
						case OPERTION_5://打印 srt 文件
							bigText = srtMain.printSrtNodeList();
							break;
							
						case OPERTION_EXIT://退出程序
							System.out.println("退出成功");
							break;
							
						default:
							System.out.println("输入非法");
							break;
					}

				}

//			}
		;



	// 一些提示信息
	public static void printAvailableOperator(){
		System.out.println("***************可用操作：***************");
		System.out.println(String.valueOf(OPERTION_1)+".当前字幕整体时间移动");
		System.out.println(String.valueOf(OPERTION_2)+".查找一条字幕");
		System.out.println(String.valueOf(OPERTION_3)+".重新选择字幕文件");
		System.out.println(String.valueOf(OPERTION_4)+".保存修改到字幕文件");
		System.out.println(String.valueOf(OPERTION_5)+".打印当前字幕");
		System.out.println(String.valueOf(OPERTION_EXIT)+".退出");
		System.out.println("*************************************");
	}
	public static void printAvailableSingleNodeOperator(){
		System.out.println("***************可用操作：***************");
		System.out.println(String.valueOf(SINGLE_NODE_OPERTION_1)+".当前字幕时间移动");
		System.out.println(String.valueOf(OPERTION_EXIT)+".退出到主菜单");
		System.out.println("*************************************");
	}

	public static void printAvailableSearchOperator(){
		System.out.println("***************可用操作：***************");
		System.out.println(String.valueOf(SEARCH_OPERTION_1)+".根据sid查找字幕");
		System.out.println(String.valueOf(SEARCH_OPERTION_2)+".查找时间点所在的字幕");
		System.out.println("*************************************");
	}

	//输入路径，之后打印整个字幕文件
		@FXML
	    void scanInFilePath(ActionEvent event) throws FileNotFoundException, NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException {
			path = captionPath.getText();
			operationSelect = 5;
			Hello();
			printingBoard.setText(bigText);
			System.out.println("看看textArea是否打印成功");
	    }
		//保存整个字幕文件
		@FXML
	    void saveFile(ActionEvent event) throws FileNotFoundException, NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException {

			operationSelect = 4;
			Hello();
			tips.setText("保存成功！\n");
			System.out.println("保存文件的函数跑完了");
	    }	
		//弹出：整体字幕移动
		@FXML
	    void tanChuang1(ActionEvent event) {

			Stage stage = new Stage();
			try {
				AnchorPane root = new AnchorPane();	//窗口
				root = FXMLLoader.load(getClass().getResource("/view/Tanchuang1.fxml"));	//getResource使用左斜杆表示路径
				Scene scene = new Scene(root);	//场景
				stage.setTitle("整体字幕平移小弹窗");	//舞台要有title
				stage.setScene(scene);	//场景放入舞台
				stage.show();	//show舞台
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	    }
		//弹窗输入前进后退以及时长
		 @FXML
	    void moveOverall(ActionEvent event) throws FileNotFoundException, NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException {

			 operationSelect=1;
			 input2 = Integer.parseInt(moveOverallTimeInput.getText());
			 if(((Button)event.getTarget()).getText().equals("前进")) {
				 System.out.println("前进");
				 input1 = "+";
			 }else if(((Button)event.getTarget()).getText().equals("后退")) {
				 System.out.println("后退");
				 input1 = "-";
			 }

			Hello();
			Stage stage1 = (Stage) moveOverallTimeInput.getScene().getWindow();
			stage1.close();
			 
		}
		//弹出：查找某条字幕
		 @FXML
		void findACaption(ActionEvent event) {
			
			Stage stage2 = new Stage();
			try {
				AnchorPane root = new AnchorPane();	//窗口
				root = FXMLLoader.load(getClass().getResource("/view/findACaption.fxml"));	//getResource使用左斜杆表示路径
				Scene scene = new Scene(root);	//场景
				stage2.setTitle("查找字幕平移子窗口");	//舞台要有title
				stage2.setScene(scene);	//场景放入舞台
				stage2.show();	//show舞台
			} catch (Exception e) {
				e.printStackTrace();
			}
			 
	    }
		//输入SID时候，查找某一条字幕并打印
		@FXML
		void findBySid(ActionEvent event) throws FileNotFoundException, NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException {
			operationSelect = 2;
			searchOp = 1;
			SIDinput = Integer.parseInt(sidInput.getText());
			Hello();
			printBoard2.setText(bigText);
			System.out.println("打印某一条字幕完毕");
		}
		//输入时间查找字幕并打印
		@FXML
	    void findByTime(ActionEvent event) throws FileNotFoundException, NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException {
			operationSelect = 2;
			searchOp = 2;
			Hinput = Integer.parseInt(findACaptionInputH.getText());
			Minput = Integer.parseInt(findACaptionInputM.getText());
			Sinput = Integer.parseInt(findACaptionInputS.getText());
			MSinput = Integer.parseInt(findACaptionInputMS.getText());
			Hello();
			printBoard2.setText(bigText);
	    }
		//移动某一条字幕
		@FXML
	    void moveACaption(ActionEvent event) throws FileNotFoundException, NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException {
//			operationSelect = 2;
//			if(searchOp==1) {
//				
//			}else if(searchOp==2) {
//				
//			}
			singleNodeMove = 1;
			moveACaptionTimeInput = Integer.parseInt(moveACaptionTime.getText());
			if(((Button)event.getTarget()).getText().equals("前进")) {
				 System.out.println("前进");
				 input1 = "+";
			 }else if(((Button)event.getTarget()).getText().equals("后退")) {
				 System.out.println("后退");
				 input1 = "-";
			 }

			Hello();
		}
		//退出查找某一条字幕的子菜单
		@FXML
	    void goHome(ActionEvent event) {
			Stage stage1 = (Stage) goBack.getScene().getWindow();
			stage1.close();
	    }
		//退出程序，关闭窗口
		@FXML
	    void exitAndClose(ActionEvent event) throws FileNotFoundException, NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException {
			
			Stage stage = (Stage) exitBtn.getScene().getWindow();
			stage.close();
	    }
		

	public static int scanInNodeSearchSid(){
		int inputSid;
		Scanner scan = new Scanner(System.in);
		System.out.println("输入查找的Sid：");
		inputSid = scan.nextInt();
		return inputSid;
	}

	public static int[] scanInNodeSearchSrtTime(){
		int time[] = new int[4];
		Scanner scan = new Scanner(System.in);
		System.out.println("输入时间点：（格式：小时 分钟 秒 毫秒）");
		time[0] = scan.nextInt();
		time[1] = scan.nextInt();
		time[2] = scan.nextInt();
		time[3] = scan.nextInt();
		scan.nextLine();
		return time;
	}

//	public static HashMap<String,Object> scanInTimeShiftParam(){
//		HashMap<String,Object> param = new HashMap();
//		System.out.println("请输入参数1：(后移输\"+\",前移输入\"-\")");
//
//		Scanner scan = new Scanner(System.in);
//		String param1 = scan.nextLine();
//
//		param.put("shiftType",param1);
//		System.out.println("请输入参数2：(要增加或减少的时间，整数，单位毫秒)");
//
//		String param2 = scan.nextLine();
//		param.put("srtMsecond",Integer.parseInt(param2));
//		return param;
//	}
	
	 public static HashMap<String,Object> scanInTimeShiftParam(String input1, int input2){
		  HashMap<String,Object> param = new HashMap();
		  
		  param.put("shiftType",input1);
		  param.put("srtMsecond",input2);
		  return param;
		 }



}
