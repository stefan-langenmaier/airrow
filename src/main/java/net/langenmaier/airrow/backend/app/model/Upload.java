package net.langenmaier.airrow.backend.app.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tika.Tika;
import org.eclipse.microprofile.config.ConfigProvider;

import net.langenmaier.airrow.backend.app.dto.MultipartUpload;

public class Upload {
	public UUID uuid;
	public UUID creator;
	public Location location;
	public String status;
    public Integer accuracy;
	public String fileName;
	public String fileHash;
	public String mimeType;
	public OffsetDateTime updatedAt;
	
	public static Upload of(MultipartUpload data) {
		Upload u  = new Upload();
		u.uuid = UUID.randomUUID();
		
		File uploadedFile = data.file;
		try {
			u.fileHash = DigestUtils.sha256Hex(new FileInputStream(uploadedFile));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		u.detect(uploadedFile);
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
	
	private void detect(File uploadedFile) {
		Tika tika = new Tika();
		try {
			mimeType = tika.detect(uploadedFile);
			if (mimeType.equals("text/plain") && uploadedFile.length() < 1000) {
				try {
					String content = Files.readString(uploadedFile.toPath());
					new URL(content);
					mimeType = "text/link";
				} catch (IOException e) {
					// content is no link
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			mimeType = "application/octet-stream";
		}
	}

	public static String getFileBasePath(String fileHash) {
		String basePath = fileHash.substring(0,2) + "/" + fileHash.substring(0,4) + "/";
		return basePath;
	}
	
	private void store(File f) {
		String storageDirectory = ConfigProvider.getConfig().getValue("storage.directory", String.class);
		Path source = f.toPath();
		File baseDir = new File(storageDirectory + getFileBasePath(fileHash));
		baseDir.mkdirs();
		Path saveFile = Paths.get(storageDirectory + getFileBasePath(fileHash) + fileHash);
		try {
			Files.move(source, saveFile, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
