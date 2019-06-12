package ca.bc.gov.iamp.email.model;

public class Attachment {
	
	private String originalFilename;
	private String contentType;
	private byte[] data;

	public Attachment(String originalFilename, String contentType, byte[] data) {
		this.originalFilename = originalFilename;
		this.contentType = contentType;
		this.data = data;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public void setOriginalFilename(String originalFilename) {
		this.originalFilename = originalFilename;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
}
