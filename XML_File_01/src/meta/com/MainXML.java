package meta.com;

public class MainXML {

	public static void main(String[] args) throws Exception {
		
		long start = System.currentTimeMillis();
		
		String fileIdsArr[]; // FILE_ID 생성
		XMLFiles fpfile = new XMLFiles();
		
		// FILE_ID 가져오기
		fileIdsArr = fpfile.selectFileId();
	
		// FILE_ID를 이용하여 파일 생성 후 결과 파일 생성 실행
		fpfile.changeInfo(fileIdsArr);
		
		long end = System.currentTimeMillis();
		
		System.out.println((end-start)/1000.0 +"초");
		
	}

}
