package com.example.demo.services;

import java.io.IOException;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

public interface IFirebaseService {
	String uploadFile(MultipartFile file) throws IOException, SizeLimitExceededException;
	void deleteFile(String fileUrl);
}
