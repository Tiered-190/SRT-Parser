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
		// 1.������Ļ�ļ�
		// 2.��ӡ���ò���
		// 3.ѡ��һ�����������ò����У�
			//a.��Ļ����ǰ��,����һ��00:00:00,000��ʽ������
			//b.��Ļ�������,����һ��00:00:00,000��ʽ������
		// 4.���ò�������Ӧ���ܣ��������
		// 5.������ִ�ж�Ӧ����
		// 6.��������ȡSrtAnalyzer
		System.out.println("                     ---------------------                    ");
		System.out.println("----------------------|����ԭ��+��Ļ������|----------------------");
		System.out.println("                     ---------------------                ");
		System.out.println("===============================================================");
		System.out.println("                           С���Ա                           ");
		System.out.println("---------------------------------------------------------------");
		System.out.println("               ֣���롢֣���ˡ��ž�����������Ź���");
		System.out.println("===============================================================");
		SrtMain srtMain = new SrtMain();

		srtMain.setOnLoadSrtFileListener(new OnLoadSrtFileListener() {
			public void onLoadSrtFileStart() {

			}
			@Override
			public void onLoadSrtFileSuccess(List<SrtNode> list) {
				System.out.println("\n��ȡsrt�ļ��ɹ���ʾ�����ļ�һ����  "+srtMain.getSrtNodeListSize()+"  ����Ļ");
				try{
					doLoop(srtMain);
				}catch (Exception e){
					e.printStackTrace();
				}

			}
			public void onLoadSrtFileFail(Exception e) {
				System.out.println("��ȡsrt�ļ�ʧ����ʾ��ԭ��Ϊ  "+e.toString());
				System.out.println("������������ȷ��srt�ļ�����·����");
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
					//ֻҪû���˳�����ÿ��ָ������󶼻��ӡ  ��ѡ������˵������ɾ����
					printAvailableOperator();

					//3
					System.out.println("��������Ĺ���ѡ��");
					op = Integer.parseInt(scan.nextLine());

					//ѡ��Ĳ����ж�
					switch (op){
						//4
						case OPERTION_1: //ʱ����ƽ��
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
											System.out.println("�����ɹ���ʾ���ɹ��ƶ�������Ļ��ʱ���");
											//srtMain.printSrtNodeList();
										}

										@Override
										public void onOperationFail(Exception e) {
											System.out.println("����ʧ����ʾ��ԭ��Ϊ  "+e.toString());
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
							
						case OPERTION_2://����ĳ����Ļ
							printAvailableSearchOperator();
							System.out.println("��ѡ������Ҫ������Ļ�ķ�ʽ��");

							int searchOp = Integer.parseInt(scan.nextLine());
							HashMap<String,Object> searchParam = new HashMap<>();
							switch (searchOp){
								case SEARCH_OPERTION_1://���� sid ����
									searchParam.put("sid",scanInNodeSearchSid());
									break;
								case SEARCH_OPERTION_2://���� ʱ�� ����
									try{
										int time[] = scanInNodeSearchSrtTime();
										searchParam.put("hour",time[0]);
										searchParam.put("minute",time[1]);
										searchParam.put("second",time[2]);
										searchParam.put("msecond",time[3]);

									}catch (Exception e){
										System.out.println("���������ʾ��ԭ��Ϊ  "+e.toString());
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
											System.out.println("���ҳɹ���ʾ��������Ļ��Ϣ������ʾ��");
											srtMain.printSrtNode(node);

											int singleNodeOp = OPERTION_INIT;
											while(singleNodeOp != OPERTION_EXIT){
												printAvailableSingleNodeOperator();
												singleNodeOp = Integer.parseInt(scan.nextLine());
												
												//�Ե�����Ļ���в���
												switch (singleNodeOp){
													case SINGLE_NODE_OPERTION_1://�Ե�����Ļ����ʱ����ƽ��
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
																		System.out.println("�����ɹ���ʾ���ɹ��ƶ�������Ļʱ���");
																	}

																	@Override
																	public void onOperationFail(Exception e) {
																		System.out.println("����ʧ����ʾ��ԭ��Ϊ  "+e.toString());
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
											System.out.println("����ʧ����ʾ��ԭ��Ϊ  "+e.toString());
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
							
						case OPERTION_3://������Ļ�ļ����л�·����
							Thread loadNewFileThread = new Thread(new Runnable() {
								@Override
								public void run() {
									srtMain.setOnLoadSrtFileListener(new OnLoadSrtFileListener() {
										@Override
										public void onLoadSrtFileStart() {

										}

										@Override
										public void onLoadSrtFileSuccess(List<SrtNode> list) {
											System.out.println("\n��ȡsrt�ļ��ɹ���ʾ�����ļ�һ����  "+srtMain.getSrtNodeListSize()+"  ����Ļ");
										}

										@Override
										public void onLoadSrtFileFail(Exception e) {
											System.out.println("��ȡsrt�ļ�ʧ����ʾ��ԭ��Ϊ  "+e.toString());
											System.out.println("������������ȷ��srt�ļ�����·����");
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
							
						case OPERTION_4://���� srt �ļ�
							Thread saveFileThread = new Thread(new Runnable() {
								@Override
								public void run() {
									srtMain.saveSrtFile(new OnSaveSrtFileListener() {
										@Override
										public void onSaveSrtFileSuccess() {
											System.out.println("����ɹ���ʾ������·��Ϊ  "+srtMain.getSrtFilePath());
										}

										@Override
										public void onSaveSrtFileFail(Exception e) {
											System.out.println("����ʧ����ʾ��ԭ��Ϊ  "+e.toString());
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
							
						case OPERTION_5://��ӡ srt �ļ�
							srtMain.printSrtNodeList();
							break;
							
						case OPERTION_EXIT://�˳�����
							System.out.println("�˳��ɹ�");
							break;
							
						default:
							System.out.println("����Ƿ�");
							break;
					}

				}

			}
		};

		Thread mainThread = new Thread(mainRun);

		mainThread.start();
	}


	// һЩ��ʾ��Ϣ
	public static void printAvailableOperator(){
		System.out.println("\n                      ------------------                    ");
		System.out.println("-----------------------|��ϵͳ�ṩ�Ĺ���|-------------------------");
		System.out.println("|                     ------------------                \t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_1)+".������Ļ�����ƶ�\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_2)+".����ĳһ������Ļ\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_3)+".���Ķ�ȡ��Ļ�ļ�\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_4)+".������Ļ�ļ��޸�\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_5)+".��ӡ��ǰ��Ļ����\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_EXIT)+".�˳���ǰ����ϵͳ\t\t\t|");
		System.out.println("=================================================================");
	}
	public static void printAvailableSingleNodeOperator(){
		System.out.println("\n                      ------------------                    ");
		System.out.println("----------------------|��ҳ���ṩ�Ĳ���|-------------------------");
		System.out.println("|                     ------------------                \t|");
		System.out.println("|\t\t\t"+String.valueOf(SINGLE_NODE_OPERTION_1)+".��ǰ��Ļʱ���ƶ�\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(OPERTION_EXIT)+".������һ���Ĳ˵�\t\t\t|");
		System.out.println("=================================================================");
	}

	public static void printAvailableSearchOperator(){
		System.out.println("\n                      ------------------                    ");
		System.out.println("-----------------------|��ҳ���ṩ�Ĳ���|-------------------------");
		System.out.println("|                     ------------------                \t|");
		System.out.println("|\t\t\t"+String.valueOf(SEARCH_OPERTION_1)+".������Ų�����Ļ\t\t\t|");
		System.out.println("|\t\t\t"+String.valueOf(SEARCH_OPERTION_2)+".����ʱ�������Ļ\t\t\t|");
		System.out.println("=================================================================");
	}

	public static String scanInFilePath(){
		String path;
		Scanner scan = new Scanner(System.in);
		System.out.println("\n������srt�ļ��ľ���·����");
		path = scan.nextLine();
		return path;
	}

	public static int scanInNodeSearchSid(){
		int inputSid;
		Scanner scan = new Scanner(System.in);
		System.out.println("\n������Ҫ���ҵ����Sid��");
		inputSid = scan.nextInt();
		return inputSid;
	}

	public static int[] scanInNodeSearchSrtTime(){
		int time[] = new int[4];
		Scanner scan = new Scanner(System.in);
		System.out.println("\n������Ҫ���ҵ���Ļʱ��㣺���磨00:00:00,000��������00(enter)00(enter)00(enter)000(enter)");
		time[0] = scan.nextInt();
		time[1] = scan.nextInt();
		time[2] = scan.nextInt();
		time[3] = scan.nextInt();
		scan.nextLine();
		return time;
	}

	public static HashMap<String,Object> scanInTimeShiftParam(){
		HashMap<String,Object> param = new HashMap();
		System.out.println("\n��ѡ����Ļ�ƶ��ķ���(ǰ����\"+\",��������\"-\")");

		Scanner scan = new Scanner(System.in);
		String param1 = scan.nextLine();

		param.put("shiftType",param1);
		System.out.println("\n��ѡ����Ļ�ƶ���ʱ�䳤�ȣ�(��������λ����)");

		String param2 = scan.nextLine();
		param.put("srtMsecond",Integer.parseInt(param2));
		return param;
	}



}
