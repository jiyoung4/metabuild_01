package meta.com;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainLog {

	public static void main(String[] args) {
		
		long preUseMemory = Runtime.getRuntime().totalMemory()-
				Runtime.getRuntime().freeMemory();
		long start = System.currentTimeMillis();

		/*-------실행--------*/
		MainLog mainLog = new MainLog();
		mainLog.manageData();
		/*-----------------*/
		
		long end = System.currentTimeMillis();
		long aftUseMemory = Runtime.getRuntime().totalMemory()-
				Runtime.getRuntime().freeMemory();
		
		long useMemory = (aftUseMemory - preUseMemory);
		
		System.out.println("Time: "+String.format("%,d",(end-start)) +"ms");
		System.out.println("Used Memory: "+String.format("%,d", useMemory/1000)+" kbyte");
		
		
	}
	
	
	
	/**
	 * 첨부된 로그파일을 1줄씩 확인하면서 keyword를 가진 line에서
	 * (threadName, keyword, extractionData)를 
	 * 추출하여 Map으로 반환해주는 메소드
	 * @param line
	 * @return Map<String, String>
	 */
	private Map<String, String> mkData(String line) {
		
		String threadName = ""; // thread명
		String extractionData = ""; // 추출 데이터
		String keyword = ""; // 추출 키워드
		
		// threadName, extractionData, keyword를 담을 dataMap
		Map<String, String> dataMap = new HashMap<>();
		
		if(line.contains("##galileo_bean start.")) {
			threadName = line.substring(58,66);
			extractionData = line.substring(1,18);
			keyword = "start";
			
		}else if(line.contains("ESB_TRAN_ID :")) {
			threadName = line.substring(58,66);
			extractionData = line.substring(line.indexOf("ESB_TRAN_ID :")+14, line.indexOf("ESB_TRAN_ID :")+61);
			keyword = "esbTran";
			
		}else if(line.contains("Content-Length:")) {
			threadName = line.substring(58,66);
			extractionData = line.substring(124);
			keyword = "contentLen";
			
		}else if(line.contains("#galileo call time:")) {
			threadName = line.substring(58,66);
			String callTime = line.substring(131);
			extractionData = callTime.substring(0, callTime.length()-3);
			keyword = "callTime";
			
			// 각 소요시간에 해당하는 threadName 담기
		}else if(line.contains("StopWatch")) {
			threadName = line.substring(58,66);
			keyword = "runningTime";
			
			// 각각의 소요시간 데이터의 threadName은  mkDataList();에서 입력
		}else if(line.contains("1. Before Marshalling")) {
			extractionData = line.substring(0,5);
			keyword = "num1";
			
		}else if(line.contains("2. Marshalling")) {
			extractionData = line.substring(0,5);
			keyword = "num2";
			
		}else if(line.contains("3. Invoking galileo")) {
			extractionData = line.substring(0,5);
			keyword = "num3";
			
		}else if(line.contains("4. Unmarshalling and Send to CmmMod Server")) {
			extractionData = line.substring(0,5);
			keyword = "num4";
		
		}else if(line.contains("##galileo_bean end.")) {
			threadName = line.substring(58,66);
			extractionData = line.substring(1,18);
			keyword = "endTime";
		
		}else {// 해당하는 keyword가 없는 경우 "NoData"입력
			keyword = "NoData";
		}
		
		dataMap.put("threadName", threadName);
		dataMap.put("keyword", keyword);
		dataMap.put("extractionData", extractionData);
		
		return dataMap;
		
	}
	
	
	
	/**
	 * mkData(String line); 에서 추출한 데이터 전체를 List로 만들어 주는 메소드
	 *  - 필요한 데이터만 추출
	 *  - 소요시간에 threadName입력
	 * @return List<Map<String, String>>
	 */
	private List<Map<String, String>> mkDataList() {
			
			String line = ""; // 로그파일 1줄
			String tempthread = ""; // 소요시간 ThreadName
			
			//(theradName, keyword, extractionData)정보가 들어가는 Map
			Map<String, String> dataMap = new HashMap<>();
			//파일의 모든 데이터List
			List<Map<String, String>> dataList = new ArrayList<>();
			
			File logFile = new File("D:\\A_logFile\\galileo.log");
			BufferedReader bufReader = null;

			try {
				
				FileReader fileReader = new FileReader(logFile);
				bufReader = new BufferedReader(fileReader);
				
				while((line = bufReader.readLine())!=null) {
					
					/*{theradName, keyword, extractionData}으로 구성된 dataMap 만들기*/
					dataMap = mkData(line);
					
					/*소요시간에 해당되는 thread명을 임시로 저장해두고
					 * 각각의 소요시간에 해당 thread명을 입력*/
					if(dataMap.get("keyword").compareTo("runningTime")==0) {
						tempthread = dataMap.get("threadName");
						
					}else if((dataMap.get("keyword")).substring(0, 3).compareTo("num")==0) {
						dataMap.put("threadName", tempthread);
					}
					
					/*dataMap으로 dataList로 만들기*/
					if(!(dataMap.get("keyword").equals("NoData"))&&!(dataMap.get("keyword").equals("runningTime"))) {
						dataList.add(dataMap);
					}
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally {
				try {
					bufReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			return dataList;
		}
	
	
	
	/**
	 * [메인로직_file1]
	 * DataList를 한줄씩 처리
	 * 1. threadName & keyword 확인
	 * 	: keyword명 확인
	 * 	1) keyword == start -> startedThread에 있는지 확인 
	 * 		(O: DTO 생성 후 Map의 key값으로 thread명 사용)
	 * 		 X: startThread에 등록 -> DTO 생성 후 Map의 key값으로 thread명 사용)
	 * 	2) keyword != start -> startedThread에 있는지 확인
	 * 		(O:데이터 추가, 만약 keyword가 'endTime'이면 DTO에서  null값이 있는지 확인 후 결과 값 String으로 저장
	 * 		 X:무시)
	 */
	private void manageData() {
		
		// 정제된 데이터 리스트 받기 위한 List
		List<Map<String, String>> dataList = new ArrayList<>();
		dataList = mkDataList();
		
		// start한 thread명 입력
		String startedThread = "";
		// start한 thread인지 확인 결과의 초기 선언
		boolean startTF = false;
		
		// threadName별 keyword의 데이터 입력을 위한 Map
		Map<String, LogDto> threadDataMap = new HashMap<>();
		// 추출된 모든 로그
		List<String> result = new ArrayList<>();
		
		
		
		// line 1줄씩 처리
		for (int i = 0; i < dataList.size(); i++) {
			
			// 1. threadName 확인
			String threadName = dataList.get(i).get("threadName");
			
			// 2. keyword 확인
			// START O
			if(dataList.get(i).get("keyword").compareTo("start")==0) {
				
				// 시작된 thread인지 판단
				startTF = chkInclude(startedThread, threadName);
				
				if(startTF) {// ==true: 데이터 입력
					// DTO 생성
					LogDto logDto = new LogDto();
					threadDataMap.put(threadName, logDto);
					
					// 데이터 입력
					threadDataMap.put(threadName,insertData(dataList.get(i), threadDataMap.get(threadName)));
					
				}else {// ==false: startedThread 등록 후 데이터 입력
					// 시작 thread 등록
					startedThread += threadName+",";
					
					// DTO생성
					LogDto logDto = new LogDto();
					threadDataMap.put(threadName, logDto);
					
					// 데이터 입력
					threadDataMap.put(threadName,insertData(dataList.get(i), threadDataMap.get(threadName)));
				}
			
				
			//START X	2) start X -> startedThread에 있는지 확인 (O:데이터 추가-END: null확인 후 String 저장 / X:무시)
			}else {
				
				// 시작된 thread인지 판단
				startTF = chkInclude(startedThread, threadName);

				if(startTF) {// ==true: 데이터 입력
					
					// 데이터 입력
					threadDataMap.put(threadName,insertData(dataList.get(i), threadDataMap.get(threadName)));
					
					// endTime 입력 시 DTO의 null값 확인
					if(dataList.get(i).get("keyword").compareTo("endTime")==0) {
						
						/*확인 결과 입력
						 - DTO에 값이 한개라도 없다면 ""(빈값)으로 반환
						 - DTO에 모두 값이 있다면 한줄로 결과 로그 반환
						 */
						result.add(chkEndCondition(threadDataMap.get(threadName)));
					}
					
				}
				
			}
		}
		
		
		/*File 1: 로그데이터 출력*/
		String filepath1 = "D:\\A_logFile\\result\\file1.log";
		result = removeEmpty(result);
		printResultLog(result, filepath1);
		
		/*File 2: 통계 로그 데이터 출력*/
		printCalLog();
		
	}
	
	
	
	/**
	 * 목록안에 특정 값이 있는지 확인하여 T/F 반환하는 메소드
	 *  - startedThread에서 threadName 확인
	 *  - startTimeList에서 startTime 확인
	 * @param inventory
	 * @param threadName
	 * @return boolean
	 */
	private boolean chkInclude(String inventory, String sValue) {
		
		// 확인값 초기 설정
		boolean chk = false;
		// 확인할 목록
		String[] startArr = new String[inventory.length()];
		startArr = inventory.split(",");
		
		
		for (int i = 0; i < startArr.length; i++) {
			// 해당 값이 있으면 true 반환
			if(startArr[i].compareTo(sValue)==0) {
				chk=true;
			}
		}
		
		return chk;
	}
	
	
	
	/**
	 * keyword에 따라 DTO에 해당 값을 입력하고 
	 * LogDto를 반환하는 메소드
	 * @param dataMap
	 * @param logDto
	 * @return LogDto
	 */
	private LogDto insertData(Map<String, String> dataMap, LogDto logDto) {
		
		String keyword = dataMap.get("keyword");
		
		if(keyword.compareTo("start")==0) {
			logDto.setStartTime(dataMap.get("extractionData"));
			
		}else if(keyword.compareTo("esbTran")==0) {
			logDto.setEsbTranId(dataMap.get("extractionData"));
			
		}else if(keyword.compareTo("contentLen")==0) {
			logDto.setContentLen(dataMap.get("extractionData"));
			
		}else if(keyword.compareTo("callTime")==0) {
			logDto.setCallTime(dataMap.get("extractionData"));
			
		}else if(keyword.compareTo("num1")==0) {
			logDto.setBeforeMarshalling(dataMap.get("extractionData"));
			
		}else if(keyword.compareTo("num2")==0) {
			logDto.setMarshalling(dataMap.get("extractionData"));
			
		}else if(keyword.compareTo("num3")==0) {
			logDto.setInvoking(dataMap.get("extractionData"));
			
		}else if(keyword.compareTo("num4")==0) {
			logDto.setUnmarshalling(dataMap.get("extractionData"));
			
		}else if(keyword.compareTo("endTime")==0) {
			logDto.setEndTime(dataMap.get("extractionData"));
			
		}
		
		return logDto;
		
	}
	
	
	
	/**
	 * DTO안에 null값이 있는지 판단해 주는 메소드
	 *  - null이 하나라도 있다면 결과로 ""만 입력
	 *  - 모든 데이터가 있다면 로그 형식으로 반환
	 * @param logDto
	 * @return
	 */
	private String chkEndCondition(LogDto logDto) {
		// 결과로그
		String resultLog="";
		// null 유무 확인
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
		
		if(count==0) {
			resultLog += logDto.getStartTime()+", "+logDto.getEndTime()+", "+logDto.getEsbTranId()+", "+
			logDto.getContentLen()+", "+logDto.getCallTime()+", "+logDto.getBeforeMarshalling()+", "+
			logDto.getMarshalling()+", "+logDto.getInvoking()+", "+logDto.getUnmarshalling();
		}
		
		return resultLog;
	}

	
	
	/**
	 * 결과에서 ""(빈값)을 제거한 뒤 반환하는 메소드
	 * @param result
	 * @return List<String>
	 */
	private List<String> removeEmpty(List<String> result){
		
		// 최종 결과
		List<String> finalResult = new ArrayList<>();
		
		for (int i = 0; i < result.size(); i++) {
			
			if(!(result.get(i).compareTo("")==0)) {
				finalResult.add(result.get(i));
			}
			
		}
		
		return finalResult;
	}
	
	
	
	/**
	 * 입력한 파일경로로 파일을 생성하여 출력하는 메소드
	 * @param result
	 * @param filepath
	 */
	private void printResultLog(List<String> result, String filepath) {
		
		File resultFile = new File(filepath);
		BufferedWriter resultWrite = null;
		
		try {
			resultWrite = new BufferedWriter(new FileWriter(resultFile, true));
			
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
	
	
	
	/**
	 * [메인로직_file2]
	 * 로그 데이터 1줄 처리
	 * 1. startTime, endTime, contentLen DTO에 담기
	 * 2. 분별 확인 위해서 startTime에서 시간:분 추출
	 * 3. startTimeList에서 stratTime 유무 확인
	 * 		1) 시간이 있는 경우: Map에서 해당 시간 List를 꺼내 DTO 담기
	 * 		2) 시간이 없는 경우: startTimeList에 추가 -> List 생성하여 DTO 담기
	 * 4. 통계 계산
	 */
	private void printCalLog() {
		
		// File1 결과파일
		File File1 = new File("D:\\A_logFile\\result\\file1.log");
		BufferedReader bufReader = null;
		// 결과파일 1줄
		String line = "";

		// 시작시간 목록
		String startTimeList = "";
		// 시작시간 목록 유무 확인
		boolean startTF = false;
		
		/* 시간별 필요 데이터(startTime, endTime, ContentLen)를 List로 묶어서 관리
		 * 데이터들은 CalDto에 담겨져 있음*/
		Map<String, List<CalDto>> allMap = new HashMap<>();
		
		try {
			FileReader fileReader = new FileReader(File1);
			bufReader = new BufferedReader(fileReader);
			
			// 1줄씩 처리
			while((line = bufReader.readLine())!=null) {
				
				// 추출 데이터 나누기
				String[] tempInfo = line.split(",");

				// 1줄을 Map
				CalDto caldto = new CalDto();
				caldto.setStart(tempInfo[0].trim());
				caldto.setEnd(tempInfo[1].trim());
				caldto.setContentLen(tempInfo[3].trim());
				
				// 분별 처리를 위해 startTime 시간, 분 추출
				String startTime = tempInfo[0].trim().substring(9, 14);
				
				
				// startTime목록에 동일 시간 있는지 확인
				startTF = chkInclude(startTimeList, startTime);
				
				if(startTF) {//true -> allMap에서 해당 시간 key로 List 꺼내서 넣어주기
					List<CalDto> group = allMap.get(startTime);
					group.add(caldto);
					allMap.put(startTime, group);
					
				}else {//false startTime 등록 -> 해당 시간 리스트 만들고 Map 내용을 리스트추가해서 넣어주기
					startTimeList += startTime+",";
					List<CalDto> group = new ArrayList<>();
					group.add(caldto);
					allMap.put(startTime, group);
				}
				
			}
			
			// 통계 계산 및 해당 메소드에서 결과 출력
			calAverage(startTimeList, allMap);
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				bufReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	
		
	}
	
	
	
	/**
	 * 데이터 통계 계산을 위한 메소드
	 * @param startTimeList
	 * @param allMap
	 */
	private void calAverage(String startTimeList, Map<String, List<CalDto>> allMap) {
		
		// 시간(분별)
		String[] times = startTimeList.split(",");
		// 결과 로그
		String allResult = "";
		// 모든 결과 로그
		List<String> resultCal = new ArrayList<>();
		
		for (int i = 0; i < times.length; i++) {
			
			// 분별 처리 
			allMap.get(times[i]);
			
			// 처리 건수
			int num = allMap.get(times[i]).size();
			
			Map<String, Long> timeResult = calTime(allMap.get(times[i]), num);
			// 평균 소요시간
			//timeResult.get("timeAvg");
			// 최소 시간
			//timeResult.get("timemin");
			// 최대 시간
			//timeResult.get("timemax");

			
			
			Map<String, Integer> sizeResult = calSize(allMap.get(times[i]), num);
			// 평균 사이즈
			//sizeResult.get("contentAvg");
			// 최소 사이즈
			//sizeResult.get("contentmin");
			// 최대 사이즈
			//sizeResult.get("contentmax");
			
			allResult = allMap.get(times[i]).get(i).getStart().substring(0, 14)+", "+num+", "+
								timeResult.get("timeAvg")+", "+timeResult.get("timemin")+", "+timeResult.get("timemax")+", "+
								sizeResult.get("contentAvg")+", "+sizeResult.get("contentmin")+", "+sizeResult.get("contentmax");
			
			resultCal.add(allResult);
		}
		
		/*File 2: 통계로그 출력*/
		String filepath2 = "D:\\A_logFile\\result\\file2.log";
		printResultLog(resultCal, filepath2);

	}

	
	
	/**
	 * contentLen에 관련된 연산 메소드
	 * 	- 평균사이즈, 최소사이즈, 최대사이즈
	 * @param datas
	 * @param num
	 * @return Map<String, Integer>
	 */
	private Map<String, Integer> calSize(List<CalDto> datas, int num) {
		
		int sum = 0; // 총합
		int contentmin = 0; // 최소값
		int contentmax = 0; // 최대값
		
		// contentLen 관련 결과 값 담을 Map
		Map<String, Integer> calSizeResult = new HashMap<>();
		
		for (int i = 0; i < datas.size(); i++) {
			
			if(i==0) {// 최소값 초기설정
				contentmin = Integer.parseInt(datas.get(i).getContentLen());
			}
			
			// 합계 누적
			sum += Integer.parseInt(datas.get(i).getContentLen());
			
			if(Integer.parseInt(datas.get(i).getContentLen()) < contentmin) {
				contentmin = Integer.parseInt(datas.get(i).getContentLen());
			}
			
			if(Integer.parseInt(datas.get(i).getContentLen()) > contentmax) {
				contentmax = Integer.parseInt(datas.get(i).getContentLen());
			}
			
		}
		
		int contentAvg = sum/num; //평균 사이즈
		
		calSizeResult.put("contentAvg", contentAvg);
		calSizeResult.put("contentmin", contentmin);
		calSizeResult.put("contentmax", contentmax);
		
		return calSizeResult;
	}
	
	
	
	/**
	 * 소요시간에 관련된 연산 메소드
	 * 	- 평균시간, 최소시간, 최대시간
	 * @param datas
	 * @param num
	 * @return Map<String, Long>
	 */
	private Map<String, Long> calTime(List<CalDto> datas, int num) {
		
		// 시간연산을 위한 SimpleDateFormat 생성
		SimpleDateFormat df = new SimpleDateFormat("YY.MM.DD HH:mm:ss", Locale.KOREA);
		
		long sum = 0; // 총합
		long timemin = 0; // 최소값
		long timemax = 0; // 최대값
		
		// 소요시간 관련 결과 값 담을 Map
		Map<String, Long> calTimeResult = new HashMap<>();
		
		for (int i = 0; i < datas.size(); i++) {
			
			try {
				// 소요시간 = 종료시간 - 시작시간
				long diff = df.parse(datas.get(i).getEnd()).getTime() - df.parse(datas.get(i).getStart()).getTime();
				
				sum += diff;
				
				if(i==0) {// 최소값 초기설정
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
		
		// 평균소요시간
		long timeAvg = sum/num;
		
		calTimeResult.put("timeAvg", timeAvg);
		calTimeResult.put("timemin", timemin);
		calTimeResult.put("timemax", timemax);
		
		return calTimeResult;
		
	}


}
