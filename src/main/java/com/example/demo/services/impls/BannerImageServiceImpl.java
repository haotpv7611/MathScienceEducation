package com.example.demo.services.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	Logger logger = LoggerFactory.getLogger(BannerImageServiceImpl.class);
	private final int DESCRIPTION_MAX_LENGTH = 150;
	private final String DELETED_STATUS = "DELETED";

	@Autowired
	private IBannerImageRepository iBannerImageRepositoy;

	@Autowired
	private IAccountRepository iAccountRepository;

	@Autowired
	private IFirebaseService iFirebaseService;

	@Autowired
	private ModelMapper modelMapper;

	// done
	@Override
	public String createBannerImage(String description, MultipartFile file, long accountId)
			throws SizeLimitExceededException, IOException {
		try {
			// 1. validate data input
			Account account = iAccountRepository.findByIdAndStatusNot(accountId, DELETED_STATUS);
			if (account == null) {
				throw new ResourceNotFoundException();
			}
			String error = Util.validateRequiredFile(file, "image", "File is invalid!",
					"Not supported this file type for image!");
			error += Util.validateString(description, DESCRIPTION_MAX_LENGTH, "\nDescription is invalid!");
			if (!error.isEmpty()) {
				return error.trim();
			}

			// 2. connect database through repository
			// 3. find entity by Id
			// 4. if not found throw not found exception

			// 5. if role is not admin, return error no permission

			// 6. create new entity and return SUCCESS
			BannerImage bannerImage = new BannerImage();
			if (description != null) {
				if (!description.trim().isEmpty()) {
					bannerImage.setDescription(description.trim());
				}
			}
			bannerImage.setImageUrl(iFirebaseService.uploadFile(file));
			bannerImage.setStatus("ACTIVE");
			bannerImage.setAccountId(accountId);
			iBannerImageRepositoy.save(bannerImage);
		} catch (Exception e) {
			logger.error("Create banner! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CREATE FAIL";
		}

		return "CREATE SUCCESS!";

	}

	@Override
	public List<BannerImageDTO> findAll() {
		List<BannerImageDTO> bannerImageDTOList = new ArrayList<>();
		try {

			// 1. connect database through repository
			// 2. find all entities
			List<BannerImage> bannerImageList = iBannerImageRepositoy.findByStatusNotOrderByStatusAsc(DELETED_STATUS);
			// 3. convert all entities to dtos
			// 4. add all dtos to bannerImageDTOList
			if (!bannerImageList.isEmpty()) {
				for (BannerImage bannerImage : bannerImageList) {
					BannerImageDTO bannerImageDTO = modelMapper.map(bannerImage, BannerImageDTO.class);
					bannerImageDTO.setAccountId(0);
					bannerImageDTOList.add(bannerImageDTO);
				}
			}
		} catch (Exception e) {
			logger.error("Find all banner! " + e.getMessage());

			return null;
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
		try {
			for (Long id : ids) {
				BannerImage bannerImage = iBannerImageRepositoy.findByIdAndStatusNot(id, DELETED_STATUS);
				if (bannerImage == null) {
					throw new ResourceNotFoundException();
				}

				if (status.equalsIgnoreCase(DELETED_STATUS)) {
					String imageURL = bannerImage.getImageUrl();
					bannerImage.setImageUrl(DELETED_STATUS);
					if (!imageURL.isEmpty()) {
						iFirebaseService.deleteFile(imageURL);
					}
				}

				// 4. update entity with isDisable = true
				bannerImage.setStatus(status);
				iBannerImageRepositoy.save(bannerImage);
			}
		} catch (Exception e) {
			logger.error("Change banner status  ids = " + ids.toString() + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "CHANGE FAIL";
		}

		return "CHANGE SUCCESS!";
	}

	@Override
	public Object findById(long id) {
		// 1. connect database through repository
		// 2. find entity by Id
		// 3. if not found throw not found exception
		// 4. else convert entity to dto
		// 5. return
		BannerImageDTO bannerImageDTO = new BannerImageDTO();
		try {

			BannerImage bannerImage = iBannerImageRepositoy.findByIdAndStatusNot(id, DELETED_STATUS);
			if (bannerImage == null) {
				throw new ResourceNotFoundException();
			}
			bannerImageDTO = new BannerImageDTO(bannerImage.getDescription(), bannerImage.getImageUrl());

		} catch (Exception e) {
			logger.error("FIND: unitId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "FIND FAIL!";
		}

		return bannerImageDTO;
	}

	@Override
	public String updateBannerImage(long id, String description, MultipartFile file)
			throws SizeLimitExceededException, IOException {
		try {
			BannerImage bannerImage = iBannerImageRepositoy.findByIdAndStatusNot(id, DELETED_STATUS);
			if (bannerImage == null) {
				throw new ResourceNotFoundException();
			}
			String error = Util.validateRequiredString(description, DESCRIPTION_MAX_LENGTH,
					"\nDescription is invalid!");
			error += Util.validateFile(file, "image", "Not supported this file type for image!");
			if (!error.isEmpty()) {

				return error.trim();
			}

			// 5. if parameter valid, update entity and return SUCCESS
			// 6. else return error

			bannerImage.setDescription(description.trim());
			if (file != null) {
				if (!file.isEmpty()) {
					String imageUrl = bannerImage.getImageUrl();
					bannerImage.setImageUrl(iFirebaseService.uploadFile(file));
					if (!imageUrl.isEmpty()) {
						iFirebaseService.deleteFile(imageUrl);
					}
				}
			}
			iBannerImageRepositoy.save(bannerImage);
		} catch (Exception e) {
			logger.error("UPDATE: bannerId = " + id + "! " + e.getMessage());
			if (e instanceof ResourceNotFoundException) {

				return "NOT FOUND!";
			}

			return "UPDATE FAIL!";
		}

		return "UPDATE SUCCESS!";

	}

	@Override
	public List<String> showBannerImage() {
		List<String> listUrls = new ArrayList<>();
		try {
			List<BannerImage> listBannerImages = iBannerImageRepositoy.findByStatus("ACTIVE");
			if (!listBannerImages.isEmpty()) {
				for (BannerImage bannerImage : listBannerImages) {
					listUrls.add(bannerImage.getImageUrl());
				}
			}
		} catch (Exception e) {
			logger.error("Show all banner! " + e.getMessage());

			return null;
		}

		return listUrls;
	}

}
