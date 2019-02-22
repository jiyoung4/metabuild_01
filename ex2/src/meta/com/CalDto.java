package meta.com;

public class CalDto {

	private String start;
	private String end;
	private String contentLen;
	
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getContentLen() {
		return contentLen;
	}
	public void setContentLen(String contentLen) {
		this.contentLen = contentLen;
	}
	
	
	@Override
	public String toString() {
		return "CalDto [start=" + start + ", end=" + end + ", contentLen=" + contentLen + "]";
	}
	
	
	
	
}
