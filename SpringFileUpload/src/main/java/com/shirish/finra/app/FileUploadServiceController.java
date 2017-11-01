package com.shirish.finra.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shirish.finra.services.FileUploadService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "File Uploader", description = "Uploads a file to file system.")
public class FileUploadServiceController {

	private final FileUploadService uploadService;

	@Autowired
	public FileUploadServiceController(FileUploadService uploadService) {
		this.uploadService = uploadService;
	}

	@ApiOperation(value = "Upload a file to file system", code = 200, notes = "Choose a file less than 4 MB in size")
	@PostMapping("/upload")
	public String storeFile(@RequestParam("file") MultipartFile file) {
		uploadService.uploadFile(file);
		return "Your file is uploaded Successfully";
	}

	@ApiOperation(value = " Welcome Message")
	@GetMapping("/")
	public String sayHello() {
		return "Hello Finra!!";
	}
}
