package net.langenmaier.strohstern.data.storage.model;

import java.io.File;

import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.ConfigProvider;

import net.langenmaier.strohstern.data.storage.dto.JsonDownloadDto;

public class Download {

    public File file;
    public MediaType mediaType;

    public static Download of(JsonDownloadDto download) {
        String storageDirectory = ConfigProvider.getConfig().getValue("storage.directory", String.class);
        
		Download d  = new Download();
        d.file = new File(storageDirectory + getFileBasePath(download.fileHash) + download.fileHash);
        d.mediaType = MediaType.valueOf(download.mimeType);
        
        return d;
    }

    // merge with upload code
    public static String getFileBasePath(String fileHash) {
		String basePath = fileHash.substring(0,2) + "/" + fileHash.substring(0,4) + "/";
		return basePath;
	}
}
