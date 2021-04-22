package com.example.demo.services.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dtos.BannerImageDTO;
import com.example.demo.dtos.ListIdAndStatusDTO;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.models.Account;
import com.example.demo.models.BannerImage;
import com.example.demo.repositories.IAccountRepository;
import com.example.demo.repositories.IBannerImageRepository;
import com.example.demo.services.IBannerImageService;
import com.example.demo.services.IFirebaseService;
import com.example.demo.utils.Util;

@Service
public class BannerImageServiceImpl implements IBannerImageService {
	private final int DESCRIPTION_MAX_LENGTH = 150;

	@Autowired
	IBannerImageRepository iBannerImageRepositoy;

	@Autowired
	IAccountRepository iAccountRepository;

	@Autowired
	IFirebaseService iFirebaseService;

	@Autowired
	ModelMapper modelMapper;

	// done
	@Override
	public String createBannerImage(String description, MultipartFile file, long accountId)
			throws SizeLimitExceededException, IOException {
		// 1. validate data input
		Account account = iAccountRepository.findByIdAndStatusNot(accountId, "DELETED");
		if (account == null) {
			throw new ResourceNotFoundException();
		}
		String error = validateRequiredFile(file, "image", "File is invalid!",
				"Not supported this file type for image!");
		error += Util.validateString(description, DESCRIPTION_MAX_LENGTH, "\nDescription is invalid!");
		if (!error.isEmpty()) {
			return error.trim();
		}
		if (account.getRoleId() != 1) {
			return "You do not have permission!";
		}
//				String error = "";
//		if (file.isEmpty()) {
//			error += "File is invalid!";
//		} else if (!file.getContentType().contains("image")) {
//			error += "Not supported this file type for image!";
//		}

		// 2. connect database through repository
		// 3. find entity by Id
		// 4. if not found throw not found exception

		// 5. if role is not admin, return error no permission

		// 6. create new entity and return SUCCESS
		BannerImage bannerImage = new BannerImage();
		bannerImage.setDescription(description.trim());
		bannerImage.setImageUrl(iFirebaseService.uploadFile(file));
		bannerImage.setStatus("ACTIVE");
		bannerImage.setAccountId(accountId);
		iBannerImageRepositoy.save(bannerImage);

		return "CREATE SUCCESS!";

	}

	@Override
	public List<BannerImageDTO> findAll() {
		// 1. connect database through repository
		// 2. find all entities
		List<BannerImage> bannerImageList = iBannerImageRepositoy.findByStatusNotOrderByStatusAsc("DELETED");
		List<BannerImageDTO> bannerImageDTOList = new ArrayList<>();
		// 3. convert all entities to dtos
		// 4. add all dtos to bannerImageDTOList
		if (!bannerImageList.isEmpty()) {
			for (BannerImage bannerImage : bannerImageList) {
				BannerImageDTO bannerImageDTO = modelMapper.map(bannerImage, BannerImageDTO.class);
				bannerImageDTO.setAccountId(0);
				bannerImageDTOList.add(bannerImageDTO);
			}
		}

		return bannerImageDTOList;
	}

	// done
	@Override
	@Transactional
	public String changeStatusBannerImage(ListIdAndStatusDTO listIdAndStatusDTO) {
		List<Long> ids = listIdAndStatusDTO.getIds();
		String status = listIdAndStatusDTO.getStatus();

		// 1. connect database through repository
		// 2. find entity by id
		// 3. if not existed throw exception
		for (Long id : ids) {
			BannerImage bannerImage = iBannerImageRepositoy.findByIdAndStatusNot(id, "DELETED");
			if (bannerImage == null) {
				throw new ResourceNotFoundException();
			}

			// 4. update entity with isDisable = true
			bannerImage.setStatus(status);
			iBannerImageRepositoy.save(bannerImage);
		}

		return "CHANGE SUCCESS!";
	}

	@Override
	public BannerImageDTO findById(long id) {
		// 1. connect database through repository
		// 2. find entity by Id
		// 3. if not found throw not found exception
		// 4. else convert entity to dto
		// 5. return
		BannerImage bannerImage = iBannerImageRepositoy.findByIdAndStatusNot(id, "DELETED");
		if (bannerImage == null) {
			throw new ResourceNotFoundException();
		}
		BannerImageDTO bannerImageDTO = new BannerImageDTO(bannerImage.getDescription(), bannerImage.getImageUrl());

		return bannerImageDTO;
	}

	@Override
	public String updateBannerImage(long id, String description, MultipartFile file)
			throws SizeLimitExceededException, IOException {
		BannerImage bannerImage = iBannerImageRepositoy.findByIdAndStatusNot(id, "DELETED");
		if (bannerImage == null) {
			throw new ResourceNotFoundException();
		}
		String error = validateRequiredString(description, DESCRIPTION_MAX_LENGTH, "\nDescription is invalid!");
		error += validateFile(file, "image", "Not supported this file type for image!");
		if (!error.isEmpty()) {

			return error.trim();
		}
//		if (file != null) {
//			if (!file.getContentType().contains("image")) {
//				error += "Not supported this file type for image!";
//			}
//		}

		// 1. connect database through repository
		// 2. find entity by id
		// 3. if not existed throw exception

		// 4. validate parameter
//		if (description == null) {
//			error += "\nDescription is invalid!";
//		} else {
//			if (description.length() > DESCRIPTION_MAX_LENGTH) {
//				error += "\nDescription is invalid!";
//			}
//		}

		// 5. if parameter valid, update entity and return SUCCESS
		// 6. else return error

		bannerImage.setDescription(description.trim());
		if (file != null) {
			bannerImage.setImageUrl(iFirebaseService.uploadFile(file));
		}
		iBannerImageRepositoy.save(bannerImage);

		return "UPDATE SUCCESS!";

	}

	@Override
	public List<String> showBannerImage() {
		List<BannerImage> listBannerImages = iBannerImageRepositoy.findByStatus("ACTIVE");
		List<String> listUrls = new ArrayList<>();

		if (!listBannerImages.isEmpty()) {
			for (BannerImage bannerImage : listBannerImages) {
				listUrls.add(bannerImage.getImageUrl());
			}
		}

		return listUrls;
	}

	private String validateRequiredString(String property, int length, String errorMessage) {
		String error = "";
		if (property == null) {
			error = errorMessage;
		} else {
			if (property.isEmpty() || property.length() > length) {
				error = errorMessage;
			}
		}

		return error;
	}

	private String validateFile(MultipartFile file, String contentType, String errorMessage) {
		String error = "";
		if (file != null) {
			if (!file.getContentType().contains(contentType)) {
				error += errorMessage;
			}
		}

		return error;
	}

	private String validateRequiredFile(MultipartFile file, String contentType, String errorMessage,
			String errorMessage2) {
		String error = "";
		if (file == null) {
			error = errorMessage;
		} else {
			if (file.isEmpty()) {
				error = errorMessage;
			} else if (!file.getContentType().contains(contentType)) {
				error += errorMessage2;
			}
		}

		return error;
	}

}
