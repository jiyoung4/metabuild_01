package meta.com;

public class MainXML {

	public static void main(String[] args) throws Exception {
		
		long preUseMemory = Runtime.getRuntime().totalMemory()-
				Runtime.getRuntime().freeMemory();
		
		
		long start = System.currentTimeMillis();
		
		/*=============================================*/
		String fileIdsArr[]; // FILE_ID 생성
		XMLFiles fpfile = new XMLFiles();
		
		// FILE_ID 가져오기
		fileIdsArr = fpfile.selectFileId();
	
		// FILE_ID를 이용하여 파일 생성 후 결과 파일 생성 실행
		fpfile.changeInfo(fileIdsArr);
		/*=============================================*/
		
		long end = System.currentTimeMillis();

		long aftUseMemory = Runtime.getRuntime().totalMemory()-
				Runtime.getRuntime().freeMemory();
		
		long useMemory = (aftUseMemory - preUseMemory);
		
		//System.out.println((end-start)/1000.0 +"초");
		System.out.println(String.format("%,d",(end-start)) +"ms");
		System.out.println("Used Memory: "+String.format("%,d", useMemory/1000)+" kbytes");
		
	}

}
