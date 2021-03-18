package com.example.demo.services.impls;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.BannerImage;
import com.example.demo.repositories.IBannerImageRepository;
import com.example.demo.services.IBannerImageService;

@Service
public class BannerImageServiceImpl implements IBannerImageService {

	@Autowired
	IBannerImageRepository iBannerImageRepositoy;
	
	@Override
	public List<String> findAll() {
		List<BannerImage> listBannerImages = iBannerImageRepositoy.findByIsDisable(false);
		List<String> listUrls = new ArrayList<>();
		
		if(!listBannerImages.isEmpty()) {
			for (BannerImage bannerImage : listBannerImages) {
				listUrls.add(bannerImage.getImageUrl());
			}
		}
		return listUrls;
	}

}
