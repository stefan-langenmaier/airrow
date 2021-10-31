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
	public Integer accuracy;
	public String artistName;
	public String artistCountry;
	public String artistPlace;
	public String artistIntroDe;
	public String artistIntroEn;
	public String objectName;
	public String objectYear;
	public String objectPlace;
	public String objectStreet;
	public String objectIdeaDe;
	public String objectIdeaEn;
	public String objectMediaName;
	public String previewMediaName;
	public String backgroundMediaName;
	public Media object;
	public Media preview;
	public Media background;
	public OffsetDateTime updatedAt;

	public static Upload of(MultipartUpload data) {
		Upload u = new Upload();
		u.uuid = UUID.randomUUID();

		u.creator = data.umd.creator;
		u.location = data.umd.location;
		u.accuracy = data.umd.accuracy;

		u.artistName = data.umd.artistName;
		u.artistCountry = data.umd.artistCountry;
		u.artistPlace = data.umd.artistPlace;
		u.artistIntroDe = data.umd.artistIntroDe;
		u.artistIntroEn = data.umd.artistIntroEn;
		u.objectName = data.umd.objectName;
		u.objectYear = data.umd.objectYear;
		u.objectPlace = data.umd.objectPlace;
		u.objectStreet = data.umd.objectStreet;
		u.objectIdeaDe = data.umd.objectIdeaDe;
		u.objectIdeaEn = data.umd.objectIdeaEn;

		u.object = addMedia(data.objectMedia, data.umd.objectMediaName);
		u.preview = addMedia(data.previewMedia, data.umd.previewMediaName);
		u.background = addMedia(data.backgroundMedia, data.umd.backgroundMediaName);

		ZoneOffset zoneOffSet = ZoneOffset.of("+00:00");
		u.updatedAt = OffsetDateTime.now(zoneOffSet);

		return u;
	}

	private static Media addMedia(File uploadedFile, String fileName) {
		Media m = new Media();
		m.fileName = fileName;
		try {
			m.fileHash = DigestUtils.sha256Hex(new FileInputStream(uploadedFile));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		m.mimeType = detect(uploadedFile, fileName);
		store(uploadedFile, m.fileHash);

		return m;
	}

	private static String detect(File uploadedFile, String fileName) {
		Tika tika = new Tika();
		String mimeType;
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
			if (fileName.endsWith(".glb")) {
				mimeType = "model/gltf-binary";
			}
		} catch (IOException e) {
			e.printStackTrace();
			mimeType = "application/octet-stream";
		}

		return mimeType;
	}

	public static String getFileBasePath(String fileHash) {
		String basePath = fileHash.substring(0, 2) + "/" + fileHash.substring(0, 4) + "/";
		return basePath;
	}

	private static void store(File f, String fileHash) {
		String storageDirectory = ConfigProvider.getConfig().getValue("airrow.storage.directory", String.class);
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
