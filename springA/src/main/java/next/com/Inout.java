package next.com;


public class Inout {

	private Input input;
	private Output output;
	private Count count;
	
	String letters = null;
	int result = 0;
	
	public Inout(Input input, Output output, Count count) {
		this.input = input;
		this.output = output;
		this.count = count;
		letters = input.read();
		result = count.count(letters);
		output.write(result);
		
	}
	
	public void start() {
		
		input.read();
	}
	

}
