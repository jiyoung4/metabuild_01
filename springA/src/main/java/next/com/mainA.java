package next.com;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;


public class mainA {

	public static void main(String[] args) {

		// Container 생성
		ApplicationContext factory = new ClassPathXmlApplicationContext("next/com/beans.xml");
		
		
		// 인자로 주어지는 타입과 빈 이름의 인스턴스를 반환
		Inout io = factory.getBean("inout", Inout.class);
		
		// io의 start(); 실행
		io.start();
		
	}

}
