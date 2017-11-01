package com.shirish.finra.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import com.shirish.finra.util.ApplicationProperties;
import com.shirish.finra.util.FileUploadException;

public class FileUploadServiceTests {

	private ApplicationProperties properties = new ApplicationProperties();
	private FileUploadService service;

	@Before
	public void init() {
		properties.setStorageLocation(("target/files/" + Math.abs(new Random().nextLong())));
		service = new FileUploadServiceImpl(properties);
		service.init();
	}

	@Test
	public void saveAndLoad() {
		service.uploadFile(
				new MockMultipartFile("foo", "foo.txt", MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
		assertThat(service.load("foo.txt")).exists();
		assertThat(service.load("foo_MetaInfo.txt")).exists();
	}

	@Test(expected = FileUploadException.class)
	public void saveNotPermitted() {
		service.uploadFile(
				new MockMultipartFile("foo", "../foo.txt", MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
	}

	@Test
	public void savePermitted() {
		service.uploadFile(
				new MockMultipartFile("foo", "bar/../foo.txt", MediaType.TEXT_PLAIN_VALUE, "Hello World".getBytes()));
	}

}
