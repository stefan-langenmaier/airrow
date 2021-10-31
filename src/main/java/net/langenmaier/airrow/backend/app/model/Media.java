package net.langenmaier.airrow.backend.app.model;

public class Media {
	public String fileName;
	public String fileHash;
	public String mimeType;

	public Media() {}

	public Media(Media m) {
		fileName = m.fileName;
		fileHash = m.fileHash;
		mimeType = m.mimeType;
	}
}
