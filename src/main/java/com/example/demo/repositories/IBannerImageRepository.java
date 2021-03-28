package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.BannerImage;

@Repository
public interface IBannerImageRepository extends JpaRepository<BannerImage, Long> {
	List<BannerImage> findByIsDisable(boolean isDisable);

	BannerImage findByIdAndIsDisable(long id, boolean isDisable);
}
