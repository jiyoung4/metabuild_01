package next.com;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/*
 * 단어 개수를 File을 통해 출력하는 기능 
 */
public class ToFile extends Output {
	

	@Override
	public void write(int cntLetters) {

		File file = new File("C:\\OOP_IO_txt\\write.txt");
		try {
			
			BufferedWriter bufWriter = new BufferedWriter(new FileWriter(file));
			bufWriter.write("단어 개수: "+Integer.toString(cntLetters));
			bufWriter.close();
			System.out.println("txt파일 생성완료");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}

}
