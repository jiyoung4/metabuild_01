package next.com;

public class MainD {

	public static void main(String[] args) {
		
		System.out.println("<MainD: Console to Console>");
		
		// 콘솔의 문자열을 letters에 입력
		FromConsole fromConsole = new FromConsole();
		String letters = fromConsole.read();
		System.out.println(letters);
		
		// letters의 단어 수 
		CountLetter countLetter = new CountLetter();
		int cntLetters = countLetter.count(letters);
		
		// 단어 개수 출력
		ToConsole toConsole = new ToConsole();
		toConsole.write(cntLetters);
		
	}

}
