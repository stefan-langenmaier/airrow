package net.langenmaier.strohstern.data.storage.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.microprofile.config.ConfigProvider;

import net.langenmaier.strohstern.data.storage.Location;
import net.langenmaier.strohstern.data.storage.dto.MultipartUpload;

public class Upload {

	public UUID creator;
	public Location location;
	public String status;
    public Integer accuracy;
	public String fileName;
	public String fileHash;
	public OffsetDateTime updatedAt;
	
	public static Upload of(MultipartUpload data) {
		Upload u  = new Upload();
		
		File uploadedFile = data.file;
		try {
			u.fileHash = DigestUtils.sha256Hex(new FileInputStream(uploadedFile));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		u.store(uploadedFile);
		
		u.creator = data.umd.creator;
		u.location = data.umd.location;
		u.status = data.umd.status;
		u.accuracy = data.umd.accuracy;
		u.fileName = data.umd.fileName;

		ZoneOffset zoneOffSet= ZoneOffset.of("+00:00");
		u.updatedAt = OffsetDateTime.now(zoneOffSet);
		
		return u;
	}
	
	public static String getFileBasePath(String fileHash) {
		String basePath = fileHash.substring(0,2) + "/" + fileHash.substring(0,4) + "/";
		return basePath;
	}
	
	private boolean store(File f) {
		String storageDirectory = ConfigProvider.getConfig().getValue("storage.directory", String.class);
		File baseDir = new File(storageDirectory + getFileBasePath(fileHash));
		baseDir.mkdirs();
		File saveFile = new File(storageDirectory + getFileBasePath(fileHash) + fileHash);
		boolean success =  f.renameTo(saveFile);
		return success;
	}
	
}
