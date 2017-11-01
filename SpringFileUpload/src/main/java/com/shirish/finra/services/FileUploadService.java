package com.shirish.finra.services;

import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

	public void uploadFile(MultipartFile multiPart);

	public void init();

	public void cleanUp();

	public Path load(String fileName);
}
