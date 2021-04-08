package com.example.demo.services.impls;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class FirebaseService {
	private final String JSON_PATH = "mathscience-e425d-firebase-adminsdk-wml4d-9e17737d2d.json";
	private final String PROJECT_ID = "mathscience-e425d";
	private final String BUCKET_NAME = "mathscience-e425d.appspot.com";
	private final String STORAGE_URL = "https://firebasestorage.googleapis.com/v0/b/mathscience-e425d.appspot.com/o/";

	private Storage storage;

	@EventListener
	public void init(ApplicationReadyEvent event) {
		try {
			// connect to firebase by service account in JSON file
			// firebase.google.com --> project setting --> Service accounts --> Java -->
			// Generate new private key
			ClassPathResource serviceAccount = new ClassPathResource(JSON_PATH);

			// Sets the service authentication credentials
			storage = StorageOptions.newBuilder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
					.setProjectId(PROJECT_ID).build().getService();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String saveFile(MultipartFile file) throws IOException, SizeLimitExceededException {
		String error = "";

		// 1. validate file is empty and file type only image and audio
		if (file.isEmpty()) {
			error = "File is invalid!";
			return error;
		}
		if (!file.getContentType().contains("image") && !file.getContentType().contains("audio")) {
			error = "Not supported this file type!";
			return error;
		}

		// 2. generate new file name with random UUID + fileExtension
		// 3. generate new token
		String fileName = UUID.randomUUID().toString() + "."
				+ StringUtils.getFilenameExtension(file.getOriginalFilename());
		String token = UUID.randomUUID().toString();

		// 4. folder store file with 2 option image and audio
		String folder = "";
		if (file.getContentType().contains("image")) {
			folder = "images";
		}
		if (file.getContentType().contains("audio")) {
			folder = "audios";
		}

		// 5. mapping token and identify reference path on firebase to file
		Map<String, String> map = new HashMap<>();
		map.put("firebaseStorageDownloadTokens", token);
		BlobId blobId = BlobId.of(BUCKET_NAME, folder + "/" + fileName);

		// 6. create file with blodId, set fileName and token, set file type
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setMetadata(map).setContentType(file.getContentType()).build();
		storage.create(blobInfo, file.getBytes());

		// 7. return URL
		String URL = STORAGE_URL + folder + "%2F" + fileName + "?alt=media&token=" + token;

		return URL;
	}

	public void deleteFile(String fileUrl) {
		if (!fileUrl.isEmpty()) {
			// get storage folder store file
			String folder = "";
			if (fileUrl.contains("images")) {
				folder = "images";
			}
			if (fileUrl.contains("audios")) {
				folder = "audios";
			}
			String filename = fileUrl.substring(fileUrl.indexOf("%2F") + 3, fileUrl.indexOf("?alt"));
			BlobId blobId = BlobId.of(BUCKET_NAME, folder + "/" + filename);

			// check file in firebase storage and delete
			try {
				if (storage.get(blobId) != null) {
					storage.delete(blobId);
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}
}
