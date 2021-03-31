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

@Service
public class BannerImageServiceImpl implements IBannerImageService {
	private final static Logger logger = LoggerFactory.getLogger(BannerImageServiceImpl.class);
	private final int DESCRIPTION_MAX_LENGTH = 150;

	@Autowired
	IBannerImageRepository iBannerImageRepositoy;

	@Autowired
	IAccountRepository iAccountRepository;

	@Autowired
	FirebaseService firebaseService;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public String createBannerImage(String description, MultipartFile file, long accountId)
			throws SizeLimitExceededException, IOException {
		// 1. validate parameter and return error if have
		String error = "";
		if (file.isEmpty()) {
			error += "File is invalid!";
		} else if (!file.getContentType().contains("image")) {
			error += "Not supported this file type for image!";
		}
		if (description.isEmpty() || description.length() > DESCRIPTION_MAX_LENGTH) {
			error += "\nDescription is invalid!";
		}
		if (!error.isEmpty()) {

			return error.trim();
		}

		// 2. connect database through repository
		// 3. find entity by Id
		// 4. if not found throw not found exception
		Account account = iAccountRepository.findByIdAndIsDisable(accountId, false);
		if (account == null) {
			throw new ResourceNotFoundException();
		}

		// 5. if role is not admin, return error no permission
		if (account.getRoleId() != 1) {
			return "You do not have permission!";
		}

		// 6. create new entity and return SUCCESS
		BannerImage bannerImage = new BannerImage();
		bannerImage.setDescription(description.trim());
		bannerImage.setImageUrl(firebaseService.saveFile(file));
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
			logger.info("Change status Image id: " + id);
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
		String error = "";

		if (file != null) {
			if (!file.getContentType().contains("image")) {
				error += "Not supported this file type for image!";
			}
		}

		// 1. connect database through repository
		// 2. find entity by id
		// 3. if not existed throw exception
		BannerImage bannerImage = iBannerImageRepositoy.findByIdAndStatusNot(id, "DELETED");
		if (bannerImage == null) {
			throw new ResourceNotFoundException();
		}

		// 4. validate parameter
		if (description == null) {
			error += "\nDescription is invalid!";
		} else {
			if (description.length() > DESCRIPTION_MAX_LENGTH) {
				error += "\nDescription is invalid!";
			}
		}
		if (!error.isEmpty()) {

			return error.trim();
		}

		// 5. if parameter valid, update entity and return SUCCESS
		// 6. else return error

		bannerImage.setDescription(description.trim());

		if (file != null) {
			bannerImage.setImageUrl(firebaseService.saveFile(file));
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

}
