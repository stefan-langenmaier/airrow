package net.langenmaier.airrow.backend.app.model;

import java.io.File;

import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.ConfigProvider;

import net.langenmaier.airrow.backend.app.dto.JsonDownloadDto;

public class Download {

    public File file;
    public MediaType mediaType;
    public String fileName;

    public static Download of(JsonDownloadDto download) {
        String storageDirectory = ConfigProvider.getConfig().getValue("storage.directory", String.class);
        
		Download d  = new Download();
        d.file = new File(storageDirectory + getFileBasePath(download.fileHash) + download.fileHash);
        d.mediaType = MediaType.valueOf(download.mimeType);
        d.fileName = download.fileName;
        
        return d;
    }

    // merge with upload code
    public static String getFileBasePath(String fileHash) {
		String basePath = fileHash.substring(0,2) + "/" + fileHash.substring(0,4) + "/";
		return basePath;
	}
}
