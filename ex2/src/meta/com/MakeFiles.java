package meta.com;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeFiles {
	
	public List<String> makeThreadList() {
		
		String line = "";
		File logFile = new File("D:\\A_logFile\\galileo.log");
		
		List<String> threadChk = new ArrayList<>();
		List<String> threadNum = new ArrayList<>();
		
		try {
			FileReader fileReader = new FileReader(logFile);
			LineNumberReader lineNum = new LineNumberReader(fileReader);
			
			while ((line = lineNum.readLine())!=null) {
				
				if(line.indexOf("eclipse.galileo-bean-thread-")>-1) {
					String theradNum = line.substring(58, 66);
					threadChk.add(theradNum);
				}
				
			}
			
			for (int i = 0; i < threadChk.size(); i++) {
				if(!threadNum.contains(threadChk.get(i))) {
					threadNum.add(threadChk.get(i));
				}
			}
			
			System.out.println(threadNum.size());
			
			lineNum.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return threadNum;
	}//makeThreadList() :: END
	
	
	public void mkDataList() {
		
		String line = "", tempthread = "";
		Map<String, String> dataMap = new HashMap<>();
		
		File logFile = new File("D:\\A_logFile\\galileo.log");
		
		try {
			FileReader fileReader = new FileReader(logFile);
			LineNumberReader lineNum = new LineNumberReader(fileReader);
			
			while((line = lineNum.readLine())!=null) {
				//System.out.println(lineNum.getLineNumber() + " : " + line);
				
				dataMap = mkData(line);
				
				if(dataMap.get("keyword").compareTo("runningTime")==0) {
					tempthread = dataMap.get("theradNum");
				}else if((dataMap.get("keyword")).substring(0, 3).compareTo("num")==0) {
					dataMap.put("theradNum", tempthread);
				}
				
				/*데이터 처리 시작*/
				if(!(dataMap.get("keyword").equals("NoData"))&&!(dataMap.get("keyword").equals("runningTime"))) {
					System.out.println(dataMap);
					// 1.  threadNum 확인 - 1) 없으면 DTO생성? 2) 있으면 확인해야 하는 것은?
					
				}
				
			}
			
			lineNum.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}//mkDataList() :: END
	
	
	
	
	public Map<String, String> mkData(String line) {
		
		String theradNum = "", setdata = "", keyword = "", tmeptherad ="";
		Map<String, String> dataMap = new HashMap<>();
		
		if(line.contains("##galileo_bean start.")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"START"+"::"+line);
			theradNum = line.substring(58,66);
			setdata = line.substring(1,18);
			keyword = "start";
			
		}else if(line.contains("ESB_TRAN_ID :")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"ESB"+"::"+line);
			theradNum = line.substring(58,66);
			setdata = line.substring(111);
			keyword = "esbTran";
			
		}else if(line.contains("Content-Length:")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"Content"+"::"+line);
			theradNum = line.substring(58,66);
			setdata = line.substring(124);
			keyword = "contentLen";
			
		}else if(line.contains("#galileo call time:")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"Calltime"+"::"+line);
			theradNum = line.substring(58,66);
			String callTime = line.substring(131);
			setdata = callTime.substring(0, callTime.length()-3);
			keyword = "callTime";
			
		}else if(line.contains("StopWatch")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"Find"+"::"+line);
			theradNum = line.substring(58,66);
			tmeptherad = theradNum;
			keyword = "runningTime";
			
		}else if(line.contains("1. Before Marshalling")) {
			//System.out.println(lineNum.getLineNumber() + " : " +"1."+"::"+line);
			theradNum = tmeptherad;
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
			theradNum = line.substring(58,66);
			setdata = line.substring(1,18);
			keyword = "endTime";
		}else {
			keyword = "NoData";
		}
		
		dataMap.put("theradNum", theradNum);
		dataMap.put("keyword", keyword);
		dataMap.put("setdata", setdata);
		
		return dataMap;
		
	}// mkData() :: END
	
}
