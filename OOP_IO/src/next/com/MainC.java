package next.com;

public class MainC {

	public static void main(String[] args) {

		System.out.println("<MainC: Console to File>");
		
		// 콘솔의 문자열을 letters에 입력
		FromConsole fromConsole = new FromConsole();
		String letters = fromConsole.read();
		System.out.println(letters);
		
		// letters의 단어 수 
		CountLetter countLetter = new CountLetter();
		int cntLetters = countLetter.count(letters);
		
		// 단어 개수 출력
		ToFile toFile = new ToFile();
		toFile.write(cntLetters);
		
	}

}
