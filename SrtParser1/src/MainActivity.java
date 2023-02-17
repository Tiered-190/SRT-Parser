import core.SrtNode;
import core.SrtTime;
import exception.NullOnLoadSrtFileListenerException;
import exception.NullOnOperateSrtNodesListenerException;
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

public class MainActivity {
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

	public static void main(String[] args) throws NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException, FileNotFoundException {
		// 1.读入字幕文件
		// 2.打印可用操作
		// 3.选择一个操作，可用操作有：
			//a.字幕整体前移,输入一个00:00:00,000格式的数字
			//b.字幕整体后移,输入一个00:00:00,000格式的数字
		// 4.调用操作器对应功能，传入参数
		// 5.操作器执行对应操作
		// 6.操作器调取SrtAnalyzer
		System.out.println("                     ---------------------                    ");
		System.out.println("----------------------|编译原理+字幕分析器|----------------------");
		System.out.println("                     ---------------------                ");
		System.out.println("===============================================================");
		System.out.println("                           小组成员                           ");
		System.out.println("---------------------------------------------------------------");
		System.out.println("               郑书桦、郑梦宜、张菊敏、郭清扬、张国润");
		System.out.println("===============================================================");
		SrtMain srtMain = new SrtMain();

		srtMain.setOnLoadSrtFileListener(new OnLoadSrtFileListener() {
			public void onLoadSrtFileStart() {

			}
			@Override
			public void onLoadSrtFileSuccess(List<SrtNode> list) {
				System.out.println("\n读取srt文件成功提示：该文件一共有  "+srtMain.getSrtNodeListSize()+"  条字幕");
				try{
					doLoop(srtMain);
				}catch (Exception e){
					e.printStackTrace();
				}

			}
			public void onLoadSrtFileFail(Exception e) {
				System.out.println("读取srt文件失败提示：原因为  "+e.toString());
				System.out.println("请重新输入正确的srt文件绝对路径：");
				srtMain.loadSrtFile(scanInFilePath());
			}
		});

		//1
		srtMain.loadSrtFile(scanInFilePath());
	}

	public static void doLoop(SrtMain srtMain) throws NullOnLoadSrtFileListenerException, JMException, NullOnOperateSrtNodesListenerException, InterruptedException {

		Runnable mainRun = new Runnable() {
			@Override
			public void run() {
				int op = OPERTION_INIT;
				Scanner scan = new Scanner(System .in);

				outer:
				while (op != OPERTION_EXIT){
					//只要没有退出程序，每条指令结束后都会打印  可选择操作菜单（这个删掉）
					printAvailableOperator();

					//3
					System.out.println("请输入你的功能选择：");
					op = Integer.parseInt(scan.nextLine());

					//选择的操作判断
					switch (op){
						//4
						case OPERTION_1: //时间轴平移
							HashMap<String,Object> param = scanInTimeShiftParam();

							Runnable executeTimeShiftRun = new Runnable() {
								@Override
								public void run() {
									srtMain.operateSrtNodes("SrtTimeShift",param,new OnOperateSrtNodesListener() {
										@Override
										public void onOperationStart() {

										}

										@Override
										public void onOperationSuccess(SrtNode node) {

										}

										@Override
										public void onOperationSuccess(List<SrtNode> srtNodeList) {
											System.out.println("操作成功提示：成功移动所有字幕的时间点");
											//srtMain.printSrtNodeList();
										}

										@Override
										public void onOperationFail(Exception e) {
											System.out.println("操作失败提示：原因为  "+e.toString());
										}
									});

								}
							};
							try {
								Thread executeThread = new Thread(executeTimeShiftRun);
								executeThread.start();
								executeThread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
								break outer;
							}
							break;
							
						case OPERTION_2://查找某条字幕
							printAvailableSearchOperator();
							System.out.println("请选择你想要查找字幕的方式：");

							int searchOp = Integer.parseInt(scan.nextLine());
							HashMap<String,Object> searchParam = new HashMap<>();
							switch (searchOp){
								case SEARCH_OPERTION_1://根据 sid 查找
									searchParam.put("sid",scanInNodeSearchSid());
									break;
								case SEARCH_OPERTION_2://根据 时间 查找
									try{
										int time[] = scanInNodeSearchSrtTime();
										searchParam.put("hour",time[0]);
										searchParam.put("minute",time[1]);
										searchParam.put("second",time[2]);
										searchParam.put("msecond",time[3]);

									}catch (Exception e){
										System.out.println("输入错误提示：原因为  "+e.toString());
										continue;
									}
									break;
							}
							Thread searchNodeThread = new Thread(new Runnable() {
								@Override
								public void run() {
									srtMain.operateSrtNodes("SrtNodeSearch", searchParam, new OnOperateSrtNodesListener() {
										@Override
										public void onOperationStart() {
										}

										@Override
										public void onOperationSuccess(SrtNode node) {
											System.out.println("查找成功提示：该条字幕信息如下所示→");
											srtMain.printSrtNode(node);

											int singleNodeOp = OPERTION_INIT;
											while(singleNodeOp != OPERTION_EXIT){
												printAvailableSingleNodeOperator();
												singleNodeOp = Integer.parseInt(scan.nextLine());
												
												//对单条字幕进行操作
												switch (singleNodeOp){
													case SINGLE_NODE_OPERTION_1://对单条字幕进行时间轴平移
														Thread thread = new Thread(new Runnable() {
															@Override
															public void run() {
																HashMap<String,Object> shiftParam = scanInTimeShiftParam();
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
																		System.out.println("操作成功提示：成功移动该条字幕时间点");
																	}

																	@Override
																	public void onOperationFail(Exception e) {
																		System.out.println("操作失败提示：原因为  "+e.toString());
																	}
																});
															}
														});
														try {
															thread.start();
															thread.join();
														} catch (InterruptedException e) {
															e.printStackTrace();
														}

														break;
													default:
														break;
												}
											}
										}

										@Override
										public void onOperationSuccess(List<SrtNode> srtNodes) {

										}

										@Override
										public void onOperationFail(Exception e) {
											System.out.println("查找失败提示：原因为  "+e.toString());
										}
									});
								}
							});
							try {
								searchNodeThread.start();
								searchNodeThread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							break;
							
						case OPERTION_3://更换字幕文件（切换路径）
							Thread loadNewFileThread = new Thread(new Runnable() {
								@Override
								public void run() {
									srtMain.setOnLoadSrtFileListener(new OnLoadSrtFileListener() {
										@Override
										public void onLoadSrtFileStart() {

										}

										@Override
										public void onLoadSrtFileSuccess(List<SrtNode> list) {
											System.out.println("\n读取srt文件成功提示：该文件一共有  "+srtMain.getSrtNodeListSize()+"  条字幕");
										}

										@Override
										public void onLoadSrtFileFail(Exception e) {
											System.out.println("读取srt文件失败提示：原因为  "+e.toString());
											System.out.println("请重新输入正确的srt文件绝对路径：");
											srtMain.loadSrtFile(scanInFilePath());
										}
									});
									srtMain.loadSrtFile(scanInFilePath());
								}
							});

							try {
								loadNewFileThread.start();
								loadNewFileThread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							break;
							
						case OPERTION_4://更新 srt 文件
							Thread saveFileThread = new Thread(new Runnable() {
								@Override
								public void run() {
									srtMain.saveSrtFile(new OnSaveSrtFileListener() {
										@Override
										public void onSaveSrtFileSuccess() {
											System.out.println("保存成功提示：保存路径为  "+srtMain.getSrtFilePath());
										}

										@Override
										public void onSaveSrtFileFail(Exception e) {
											System.out.println("保存失败提示：原因为  "+e.toString());
										}
									});
								}
							});

							try {
								saveFileThread.start();
								saveFileThread.join();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							break;
							
						case OPERTION_5://打印 srt 文件
							srtMain.printSrtNodeList();
							break;
							
						case OPERTION_EXIT://退出程序
							System.out.println("退出成功");
							break;
							
						default:
							System.out.println("输入非法");
							break;
					}

				}

			}
		};

		Thread mainThread = new Thread(mainRun);

		mainThread.start();
	}


	// 一些提示信息
	public static void printAvailableOperator(){
		System.out.println("\n                      ------------------                    ");
		System.out.println("-----------------------|该系统提供的功能|-------------------------");
		System.out.println("|                     ------------------                \t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_1)+".整体字幕进行移动\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_2)+".查找某一条的字幕\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_3)+".更改读取字幕文件\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_4)+".保存字幕文件修改\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_5)+".打印当前字幕内容\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_EXIT)+".退出当前所在系统\t\t\t|");
		System.out.println("=================================================================");
	}
	public static void printAvailableSingleNodeOperator(){
		System.out.println("\n                      ------------------                    ");
		System.out.println("----------------------|该页面提供的操作|-------------------------");
		System.out.println("|                     ------------------                \t|");
		System.out.println("|\t\t\t"+String.valueOf(SINGLE_NODE_OPERTION_1)+".当前字幕时间移动\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_EXIT)+".返回上一级的菜单\t\t\t|");
		System.out.println("=================================================================");
	}

	public static void printAvailableSearchOperator(){
		System.out.println("\n                      ------------------                    ");
		System.out.println("-----------------------|该页面提供的操作|-------------------------");
		System.out.println("|                     ------------------                \t|");
		System.out.println("|\t\t\t"+String.valueOf(SEARCH_OPERTION_1)+".根据序号查找字幕\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(SEARCH_OPERTION_2)+".查找时间查找字幕\t\t\t|");
		System.out.println("=================================================================");
	}

	public static String scanInFilePath(){
		String path;
		Scanner scan = new Scanner(System.in);
		System.out.println("\n请输入srt文件的绝对路径：");
		path = scan.nextLine();
		return path;
	}

	public static int scanInNodeSearchSid(){
		int inputSid;
		Scanner scan = new Scanner(System.in);
		System.out.println("\n输入想要查找的序号Sid：");
		inputSid = scan.nextInt();
		return inputSid;
	}

	public static int[] scanInNodeSearchSrtTime(){
		int time[] = new int[4];
		Scanner scan = new Scanner(System.in);
		System.out.println("\n输入想要查找的字幕时间点：比如（00:00:00,000）则输入00(enter)00(enter)00(enter)000(enter)");
		time[0] = scan.nextInt();
		time[1] = scan.nextInt();
		time[2] = scan.nextInt();
		time[3] = scan.nextInt();
		scan.nextLine();
		return time;
	}

	public static HashMap<String,Object> scanInTimeShiftParam(){
		HashMap<String,Object> param = new HashMap();
		System.out.println("\n请选择字幕移动的方向：(前进输\"+\",后退输入\"-\")");

		Scanner scan = new Scanner(System.in);
		String param1 = scan.nextLine();

		param.put("shiftType",param1);
		System.out.println("\n请选择字幕移动的时间长度：(整数，单位毫秒)");

		String param2 = scan.nextLine();
		param.put("srtMsecond",Integer.parseInt(param2));
		return param;
	}



}
