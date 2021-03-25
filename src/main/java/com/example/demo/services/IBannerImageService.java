package com.example.demo.services;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.BannerImageDTO;

public interface IBannerImageService {
	List<String> showBannerImage();

	List<BannerImageDTO> findAll();

	BannerImageDTO findById(long id);

	String createBannerImage(String description, MultipartFile file, long accountId)
			throws SizeLimitExceededException, IOException;

	String updateBannerImage(long id, String description, MultipartFile file)
			throws SizeLimitExceededException, IOException;

	String deleteBannerImage(long id);
}
