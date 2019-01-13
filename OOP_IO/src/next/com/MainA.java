package next.com;


public class MainA {

	public static void main(String[] args){

		System.out.println("<MainA: File to Console>");
		
		// 파일에서 문자를 읽은 후 문자열을 letters에 입력
		FromFile fromFile = new FromFile();
		String letters = fromFile.read();
		System.out.println(fromFile.getLine());
		
		// letters의 단어 수 
		CountLetter countLetter = new CountLetter();
		int cntLetters = countLetter.count(letters);
		
		// 단어 개수 출력
		ToConsole toConsole = new ToConsole();
		toConsole.write(cntLetters);
		
	}

}
