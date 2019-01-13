package next.com;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * File을 통해 문자열을 입력받는 기능
 */
public class FromFile extends Input {
	
	private String line="";// 문자열 저장할 변수

	public String getLine() {
		return line;
	}
	

	@Override
	public String read() {
		
		//파일 객체 생성
		File file = new File("C:\\OOP_IO_txt\\test.txt");
		try {
			//입력 스트림 생성
			FileReader fileReader = new FileReader(file);
			//입력 버퍼 생성
			BufferedReader bufReader = new BufferedReader(fileReader);
			//읽어온 문자열을 String line에 입력
			line = bufReader.readLine();
			bufReader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return line;
	}

}
