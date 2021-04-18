package net.langenmaier.airrow.backend.app.model;

import net.langenmaier.airrow.backend.app.dto.EsTarget;

public class Target{

	public String refCode;
	public String status;
	public String mimeType;
	public String fileHash;

	public static Target of(EsTarget et) {
		Target t = new Target();

		t.refCode = et.refCode;
		t.status = et.status;
		t.mimeType = et.mimeType;
		t.fileHash = et.fileHash;

		return t;
	}

	public void clean() {
		refCode = null;
		mimeType = null;
		fileHash = null;
	}
}
