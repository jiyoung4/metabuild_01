package meta.com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainLog {

	public static void main(String[] args) {
		
		long preUseMemory = Runtime.getRuntime().totalMemory()-
				Runtime.getRuntime().freeMemory();
		long start = System.currentTimeMillis();

		
		MainLog mainLog = new MainLog();
		mainLog.manageData();
		
		
		long end = System.currentTimeMillis();
		long aftUseMemory = Runtime.getRuntime().totalMemory()-
				Runtime.getRuntime().freeMemory();
		
		long useMemory = (aftUseMemory - preUseMemory);
		
		System.out.println("Time: "+String.format("%,d",(end-start)) +"ms");
		System.out.println("Used Memory: "+String.format("%,d", useMemory/1000)+" kbyte");
		
		
	}
	
	
	/**
	 * line에서 사용할 데이터(threadName, keyword, setdata)만 
	 * 추출하여 Map으로 반환해주는 메소드
	 * @param line
	 * @return
	 */
	private Map<String, String> mkData(String line) {
		
		String threadName = "", setdata = "", keyword = "";
		Map<String, String> dataMap = new HashMap<>();
		
		if(line.contains("##galileo_bean start.")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"START"+"::"+line);
			threadName = line.substring(58,66);
			setdata = line.substring(1,18);
			keyword = "start";
			
		}else if(line.contains("ESB_TRAN_ID :")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"ESB"+"::"+line);
			threadName = line.substring(58,66);
			setdata = line.substring(line.indexOf("ESB_TRAN_ID :")+14, line.indexOf("ESB_TRAN_ID :")+61);
			keyword = "esbTran";
			
		}else if(line.contains("Content-Length:")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"Content"+"::"+line);
			threadName = line.substring(58,66);
			setdata = line.substring(124);
			keyword = "contentLen";
			
		}else if(line.contains("#galileo call time:")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"Calltime"+"::"+line);
			threadName = line.substring(58,66);
			String callTime = line.substring(131);
			setdata = callTime.substring(0, callTime.length()-3);
			keyword = "callTime";
			
		}else if(line.contains("StopWatch")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"Find"+"::"+line);
			threadName = line.substring(58,66);
			keyword = "runningTime";
			
		}else if(line.contains("1. Before Marshalling")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"1."+"::"+line);
			setdata = line.substring(0,5);
			keyword = "num1";
			
		}else if(line.contains("2. Marshalling")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"2."+"::"+line);
			setdata = line.substring(0,5);
			keyword = "num2";
			
		}else if(line.contains("3. Invoking galileo")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"3."+"::"+line);
			setdata = line.substring(0,5);
			keyword = "num3";
			
		}else if(line.contains("4. Unmarshalling and Send to CmmMod Server")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"4."+"::"+line);
			setdata = line.substring(0,5);
			keyword = "num4";
		
		}else if(line.contains("##galileo_bean end.")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"END"+"::"+line);
			threadName = line.substring(58,66);
			setdata = line.substring(1,18);
			keyword = "endTime";
		}else {
			keyword = "NoData";
		}
		
		dataMap.put("threadName", threadName);
		dataMap.put("keyword", keyword);
		dataMap.put("setdata", setdata);
		
		return dataMap;
		
	}
	
	
	/**
	 * line에서 사용할 데이터(theradName, keyword, setdata)들로
	 * 구성된 List를 만들어 주는 메소드
	 * @return
	 */
	private List<Map<String, String>> mkDataList() {
			
			String line = "", tempthread = "";
			//(theradName, keyword, setdata)정보가 들어가는 Map
			Map<String, String> dataMap = new HashMap<>();
			//파일의 모든 데이터List
			List<Map<String, String>> dataList = new ArrayList<>();
			
			File logFile = new File("D:\\A_logFile\\galileo.log");
			
			BufferedReader lineNum = null;
			try {
				FileReader fileReader = new FileReader(logFile);
				lineNum = new BufferedReader(fileReader);
				
				while((line = lineNum.readLine())!=null) {
					//System.out.println(lineNum.getLineNumber() + " : " + line);
					
					/*(theradName, keyword, setdata)으로 구성*/
					
					dataMap = mkData(line);
					
					/*소요시간에 해당되는 thread명을 임시로 저장해두고
					 * 각각의 소요시간에 해당 thread명을 입력*/
					if(dataMap.get("keyword").compareTo("runningTime")==0) {
						tempthread = dataMap.get("threadName");
						//System.out.println(tempthread);
						
					}else if((dataMap.get("keyword")).substring(0, 3).compareTo("num")==0) {
						dataMap.put("threadName", tempthread);
					}
					
					/*필요한 keyword만 추출해서 dataList로 만들기*/
					if(!(dataMap.get("keyword").equals("NoData"))&&!(dataMap.get("keyword").equals("runningTime"))) {
						//System.out.println(dataMap);
						dataList.add(dataMap);
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					lineNum.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return dataList;
		}
	
	
	/**
	 * 메인로직:
	 * DataList를 한줄씩 처리
	 * 1. keyword명 확인
	 * 	1) start O -> startedThread에 있는지 확인 (O:-> DTO 생성 후 Map의 key값으로 thread명 사용: new)
	 * 									   X:startThread에 등록 -> DTO 생성 후 Map의 key값으로 thread명 사용)
	 * 	2) start X -> startedThread에 있는지 확인 (O:데이터 추가-END: null확인 후 String 저장 / X:무시)
	 */
	private void manageData() {
		
		List<Map<String, String>> dataList = new ArrayList<>();
		dataList = mkDataList();// 데이터 리스트
		
		Map<String, LogDto> threadDataMap = new HashMap<>();
		String startedThread = "";//start한 thread명 기억 -> 확인할 수 있는 method(T/F)
		//String result = "";//최종 결과 
		List<String> result = new ArrayList<>();
		
		//LogDto logDto = new LogDto();//start~end까지의 Data를 담는 Dto
		boolean startTF = false;
		
		// line 1줄씩 처리
		for (int i = 0; i < dataList.size(); i++) {
			
			//START O
			//System.out.println(dataList.get(i).get("keyword"));
			String threadName = dataList.get(i).get("threadName");
			//System.out.println(a);
			
			if(dataList.get(i).get("keyword").compareTo("start")==0) {
				
				//System.out.println(startedThread+"========"+threadName);
				startTF = chkstartedThread(startedThread, threadName);
				//System.out.println(startTF);
				
				if(startTF) {//==true
					// DTO 생성
					LogDto logDto = new LogDto();
					threadDataMap.put(threadName, logDto);
					
					// 데이터 입력
					threadDataMap.put(threadName,insertData(dataList.get(i), threadDataMap.get(threadName)));
					
				}else {//==false: startedThread 등록
					startedThread += threadName+",";
					//System.out.println(startedThread);
					
					// DTO생성
					LogDto logDto = new LogDto();
					threadDataMap.put(threadName, logDto);
					// 데이터 입력
					threadDataMap.put(threadName,insertData(dataList.get(i), threadDataMap.get(threadName)));
				}
			
			//START X	2) start X -> startedThread에 있는지 확인 (O:데이터 추가-END: null확인 후 String 저장 / X:무시)
			}else {
				
				startTF = chkstartedThread(startedThread, threadName);
				//System.out.println(startTF);
				if(startTF) {//==true
					
					// 데이터 추가
					threadDataMap.put(threadName,insertData(dataList.get(i), threadDataMap.get(threadName)));
					
					if(dataList.get(i).get("keyword").compareTo("endTime")==0) {
						
						//확인 후 결과에 추가
						result.add(chkEndCondition(threadDataMap.get(threadName)));
					}
					
				}
				
			}
		}
		
		//System.out.println(result);
		//printResultLog(result);
		
		printCalLog();
		
	}
	
	
	
	private boolean chkstartedThread(String startedThread, String threadName) {
		
		boolean chk = false;
		String[] startArr = new String[startedThread.length()];
		startArr = startedThread.split(",");
		
		
		for (int i = 0; i < startArr.length; i++) {
			//System.out.println(startArr[i]);
			
			if(startArr[i].compareTo(threadName)==0) {
				//System.out.println(startArr[i]+"::::"+threadName);
				chk=true;
			}
		}
		//System.out.println(chk);
		return chk;
	}
	
	
	
	private LogDto insertData(Map<String, String> dataMap, LogDto logDto) {
		
		String keyword = dataMap.get("keyword");
		
		if(keyword.compareTo("start")==0) {
			//System.out.println(keyword +"::"+ "start"+dataMap.get("setdata"));
			logDto.setStartTime(dataMap.get("setdata"));
			
		}else if(keyword.compareTo("esbTran")==0) {
			//System.out.println(keyword +"::"+ "esbTran");
			logDto.setEsbTranId(dataMap.get("setdata"));
			
		}else if(keyword.compareTo("contentLen")==0) {
			//System.out.println(keyword +"::"+ "contentLen");
			logDto.setContentLen(dataMap.get("setdata"));
			
		}else if(keyword.compareTo("callTime")==0) {
			//System.out.println(keyword +"::"+ "callTime");
			logDto.setCallTime(dataMap.get("setdata"));
			
		}else if(keyword.compareTo("num1")==0) {
			//System.out.println(keyword +"::"+ "num1");
			logDto.setBeforeMarshalling(dataMap.get("setdata"));
			
		}else if(keyword.compareTo("num2")==0) {
			//System.out.println(keyword +"::"+ "num2");
			logDto.setMarshalling(dataMap.get("setdata"));
			
		}else if(keyword.compareTo("num3")==0) {
			//System.out.println(keyword +"::"+ "num3");
			logDto.setInvoking(dataMap.get("setdata"));
			
		}else if(keyword.compareTo("num4")==0) {
			//System.out.println(keyword +"::"+ "num4");
			logDto.setUnmarshalling(dataMap.get("setdata"));
			
		}else if(keyword.compareTo("endTime")==0) {
			//System.out.println(keyword +"::"+ "endTime");
			logDto.setEndTime(dataMap.get("setdata"));
			
		}
		
		return logDto;
		
	}
	
	
	private String chkEndCondition(LogDto logDto) {
		String resultLog="";
		int count=0;
		
		if(logDto.getStartTime() == null) {
			count++;
		
		}else if(logDto.getEsbTranId() == null){
			count++;
		
		}else if(logDto.getContentLen() == null){
			count++;
		
		}else if(logDto.getCallTime() == null){
			count++;
		
		}else if(logDto.getBeforeMarshalling() == null){
			count++;
		
		}else if(logDto.getMarshalling() == null){
			count++;
		
		}else if(logDto.getInvoking() == null){
			count++;
			
		}else if(logDto.getUnmarshalling() == null){
			count++;
		
		}else if(logDto.getEndTime() == null){
			count++;
		}
		
		//System.out.println(count);
		
		if(count==0) {
			//System.out.println(logDto.toString());
			resultLog += logDto.getStartTime()+", "+logDto.getEndTime()+", "+logDto.getEsbTranId()+", "+
			logDto.getContentLen()+", "+logDto.getCallTime()+", "+logDto.getBeforeMarshalling()+", "+
			logDto.getMarshalling()+", "+logDto.getInvoking()+", "+logDto.getUnmarshalling();
		}
		
		return resultLog;
		
	}
	
	private List<String> removeEmpty(List<String> result){
		
		List<String> finalResult = new ArrayList<>();
		
		for (int i = 0; i < result.size(); i++) {
			
			if(!(result.get(i).compareTo("")==0)) {
				finalResult.add(result.get(i));
			}
			
		}
		
		return finalResult;
	}
	
	private void printResultLog(List<String> result) {
		
		File resultFile1 = new File("D:\\A_logFile\\result\\file1.log");
		BufferedWriter resultWrite = null;
		
		try {
			resultWrite = new BufferedWriter(new FileWriter(resultFile1, true));
			
			for (int i = 0; i < result.size(); i++) {
				if(!(result.get(i).compareTo("") == 0)) {
					//System.out.println(result.get(i));
					resultWrite.write(result.get(i));
					resultWrite.newLine();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				resultWrite.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	/*=======file2========*/
	
	private void printCalLog() {
		
		File File1 = new File("D:\\A_logFile\\result\\file1.log");
		BufferedReader lineNum = null;
		
		Map<String, List<CalDto>> allMap = new HashMap<>();
		String startTimeList = "";
		
		try {
			FileReader fileReader = new FileReader(File1);
			lineNum = new LineNumberReader(fileReader);
			
			String line = "";
			boolean startTF = false;
			
			while((line = lineNum.readLine())!=null) {
				
				//System.out.println(line);
				String[] tempInfo = line.split(",");
				// 1줄을 Map
				CalDto caldto = new CalDto();
				caldto.setStart(tempInfo[0].trim());
				caldto.setEnd(tempInfo[1].trim());
				caldto.setContentLen(tempInfo[3].trim());
				
				
				//System.out.println(tempInfo[0].trim());
				//System.out.println(tempInfo[0].trim().substring(9, 14));
				String startTime = tempInfo[0].trim().substring(9, 14);
				
				
				// Map 시간 있는지 없는지 -
				startTF = chkstartedThread(startTimeList, startTime);
				
				if(startTF) {//true -> allMap에서 해당 시간 key로 넣어주기
					//System.out.println(startTF);
					List<CalDto> group = allMap.get(startTime);
					group.add(caldto);
					allMap.put(startTime, group);
					
				}else {//false startTime 등록 -> 해당 시간 리스트 만들고 Map 내용을 리스트추가해서 다시 넣어주기
					//System.out.println(startTF);
					startTimeList += startTime+",";
					List<CalDto> group = new ArrayList<>();
					group.add(caldto);
					allMap.put(startTime, group);
				}
				
			}
			
			//System.out.println(startTimeList);
			//System.out.println(allMap.toString());
			
			calAverage(startTimeList, allMap);
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				lineNum.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	
		
	}
	
	
	private void calAverage(String startTimeList, Map<String, List<CalDto>> allMap) {
		
		String[] times = startTimeList.split(",");
		List<String> resultCal = new ArrayList<>();
		String allResult = "";
		
		for (int i = 0; i < times.length; i++) {
			
			allMap.get(times[i]);
			//System.out.println(allMap.get(times[i]));
			
			// 처리 건수
			int num = allMap.get(times[i]).size();
			//System.out.println(cnt);
			
			Map<String, Long> timeResult = calTime(allMap.get(times[i]), num);
			// 평균 소요시간
			timeResult.get("timeAvg");
			// 최소 시간
			timeResult.get("timemin");
			// 최대 시간
			timeResult.get("timemax");

			
			
			Map<String, Integer> sizeResult = calSize(allMap.get(times[i]), num);
			// 평균 사이즈
			sizeResult.get("contentAvg");
			// 최소 사이즈
			sizeResult.get("contentmin");
			// 최대 사이즈
			sizeResult.get("contentmax");
			
			allResult = allMap.get(times[i]).get(i).getStart().substring(0, 14)+", "+num+", "+
								timeResult.get("timeAvg")+", "+timeResult.get("timemin")+", "+timeResult.get("timemax")+", "+
								sizeResult.get("contentAvg")+", "+sizeResult.get("contentmin")+", "+sizeResult.get("contentmax");
			
			resultCal.add(allResult);
		}
		
		//System.out.println(resultCal);
		printResultCal(resultCal);
		
	}

	private Map<String, Integer> calSize(List<CalDto> datas, int num) {
		
		int sum = 0;
		int contentmin = 0;
		int contentmax = 0;
		
		for (int i = 0; i < datas.size(); i++) {
			
			if(i==0) {
				contentmin = Integer.parseInt(datas.get(i).getContentLen());
			}
			
			sum += Integer.parseInt(datas.get(i).getContentLen());
			
			if(Integer.parseInt(datas.get(i).getContentLen()) < contentmin) {
				contentmin = Integer.parseInt(datas.get(i).getContentLen());
			}
			
			if(Integer.parseInt(datas.get(i).getContentLen()) > contentmax) {
				contentmax = Integer.parseInt(datas.get(i).getContentLen());
			}
			
		}
		
		int contentAvg = sum/num;
		
		Map<String, Integer> calSizeResult = new HashMap<>();
		calSizeResult.put("contentAvg", contentAvg);
		calSizeResult.put("contentmin", contentmin);
		calSizeResult.put("contentmax", contentmax);
		
		//System.out.println(calSizeResult.get("contentAvg")+"::"+calSizeResult.get("contentmin")+"::"+calSizeResult.get("contentmax"));
	
		return calSizeResult;
	}
	
	private Map<String, Long> calTime(List<CalDto> datas, int num) {
		
		SimpleDateFormat df = new SimpleDateFormat("YY.MM.DD HH:mm:ss", Locale.KOREA);
		
		
		long sum = 0;
		long timemin = 0;
		long timemax = 0;
		
		for (int i = 0; i < datas.size(); i++) {
			
			try {
				long diff = df.parse(datas.get(i).getEnd()).getTime() - df.parse(datas.get(i).getStart()).getTime();
				
				sum += diff;
				
				if(i==0) {
					timemin = diff;
				}
				
				if(diff < timemin) {
					timemin = diff;
				}
				
				if(diff > timemax) {
					timemax = diff;
				}
				
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		
		long timeAvg = sum/num;
		
		Map<String, Long> calTimeResult = new HashMap<>();
		calTimeResult.put("timeAvg", timeAvg);
		calTimeResult.put("timemin", timemin);
		calTimeResult.put("timemax", timemax);
		
		//System.out.println(calTimeResult.get("timeAvg")+"::"+calTimeResult.get("timemin")+"::"+calTimeResult.get("timemax"));
	
		return calTimeResult;
		
		
	}
	
	private void printResultCal(List<String> result) {
		
		File resultFile2 = new File("D:\\A_logFile\\result\\file2.log");
		BufferedWriter resultWrite = null;
		
		try {
			resultWrite = new BufferedWriter(new FileWriter(resultFile2, true));
			
			for (int i = 0; i < result.size(); i++) {
					resultWrite.write(result.get(i));
					resultWrite.newLine();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				resultWrite.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}


}
