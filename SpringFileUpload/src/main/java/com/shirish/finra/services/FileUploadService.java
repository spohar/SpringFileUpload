package com.shirish.finra.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {

	public void uploadFile(MultipartFile multiPart);

	public void init();

	public void cleanUp();
}
