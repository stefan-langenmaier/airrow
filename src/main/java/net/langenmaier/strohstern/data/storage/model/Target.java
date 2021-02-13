package net.langenmaier.strohstern.data.storage.model;

import net.langenmaier.strohstern.data.storage.dto.EsTarget;

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
