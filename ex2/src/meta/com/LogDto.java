package meta.com;

public class LogDto {
	
	private String startTime;
	private String esbTranId;
	private String contentLen;
	private String callTime;
	private String beforeMarshalling;
	private String marshalling;
	private String invoking;
	private String unmarshalling;
	private String endTime;
	
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEsbTranId() {
		return esbTranId;
	}
	public void setEsbTranId(String esbTranId) {
		this.esbTranId = esbTranId;
	}
	public String getContentLen() {
		return contentLen;
	}
	public void setContentLen(String contentLen) {
		this.contentLen = contentLen;
	}
	public String getCallTime() {
		return callTime;
	}
	public void setCallTime(String callTime) {
		this.callTime = callTime;
	}
	public String getBeforeMarshalling() {
		return beforeMarshalling;
	}
	public void setBeforeMarshalling(String beforeMarshalling) {
		this.beforeMarshalling = beforeMarshalling;
	}
	public String getMarshalling() {
		return marshalling;
	}
	public void setMarshalling(String marshalling) {
		this.marshalling = marshalling;
	}
	public String getInvoking() {
		return invoking;
	}
	public void setInvoking(String invoking) {
		this.invoking = invoking;
	}
	public String getUnmarshalling() {
		return unmarshalling;
	}
	public void setUnmarshalling(String unmarshalling) {
		this.unmarshalling = unmarshalling;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
	@Override
	public String toString() {
		return "LogDto [startTime=" + startTime + ", esbTranId=" + esbTranId + ", contentLen=" + contentLen
				+ ", callTime=" + callTime + ", beforeMarshalling=" + beforeMarshalling + ", marshalling=" + marshalling
				+ ", invoking=" + invoking + ", unmarshalling=" + unmarshalling + ", endTime=" + endTime + "]";
	}
	
	
	
	
	

	
	
	
	
	

}
