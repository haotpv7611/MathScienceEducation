package com.example.demo.services;

import java.util.List;

import com.example.demo.dtos.BannerImageDTO;

public interface IBannerImageService {
	List<String> showBannerImage();
	List<BannerImageDTO> findAll();
	BannerImageDTO findById(long id);
	String createBannerImage(String imageUrl, String description, long accountId);
	String updateBannerImage(long id, String imageUrl, String description);
	String deleteBannerImage(long id);
}
