package next.com;

import java.util.Scanner;

/*
 * Console을 통해 문자열을 입력받는 기능
 */
public class FromConsole extends Input {
	
	private String line="";// 문자열 저장할 변수

	public String getLine() {
		return line;
	}


	@Override
	public String read() {
		
		// Scanner를 통해 입력받은 문자열을 String line에 입력
		Scanner scan = new Scanner(System.in);
		line = scan.nextLine();

		// 입력받은 문자열 반환
		return line;
	}

}
