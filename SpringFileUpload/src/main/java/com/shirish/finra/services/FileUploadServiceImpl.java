package com.shirish.finra.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.shirish.finra.util.ApplicationProperties;
import com.shirish.finra.util.FileUploadException;

@Service
public class FileUploadServiceImpl implements FileUploadService {

	@Value("${storage.location}")
	private String dirStore;

	private final Path rootLocation;

	@Autowired
	public FileUploadServiceImpl(ApplicationProperties prop) {

		rootLocation = Paths.get(prop.getStorageLocation());
	}

	@Override
	public void uploadFile(MultipartFile file) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new FileUploadException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new FileUploadException(
						"Cannot store file with relative path outside current directory " + filename);
			}
			Files.copy(file.getInputStream(), this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
			// Writing the meta info...
			writeMetaInfo(filename);
		} catch (IOException e) {
			throw new FileUploadException("Failed to store file " + filename, e);
		}

	}

	@Override
	public void cleanUp() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		} catch (IOException e) {
			throw new FileUploadException("Could not initialize storage", e);
		}
	}

	private void writeMetaInfo(String fileName) throws IOException {
		BasicFileAttributes fileAttributes = Files.readAttributes(this.rootLocation.resolve(fileName),
				BasicFileAttributes.class);

		// fileAttributes.
		Path metaPath = Files.createFile(this.rootLocation.resolve(getFileBaseName(fileName) + "_MetaInfo.txt"));
		BufferedWriter buf = Files.newBufferedWriter(metaPath);
		buf.write("File creation time :: " + fileAttributes.creationTime().toString());
		buf.newLine();
		buf.write("File Last Accessed time :: " + fileAttributes.lastAccessTime().toString());
		buf.close();

	}

	private String getFileBaseName(String fullName) {
		return fullName.substring(0, fullName.indexOf("."));
	}

}
