package com.example.demo.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class BannerImageDTO {
	private long id;
	private String description;
	private String imageUrl;
	private long accountId;
	private String status;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime createdDate;
	private String createdBy;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime modifiedDate;
	private String modifiedBy;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the createdDate
	 */
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the modifiedDate
	 */
	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	/**
	 * @return the modifiedBy
	 */
	public String getModifiedBy() {
		return modifiedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public BannerImageDTO() {
	}

	public BannerImageDTO(String description, String imageUrl) {
		super();
		this.description = description;
		this.imageUrl = imageUrl;
	}

	public BannerImageDTO(long id, String description, long accountId) {
		super();
		this.id = id;
		this.description = description;
		this.accountId = accountId;
	}

	public BannerImageDTO(String description, String imageUrl, long accountId) {
		super();
		this.description = description;
		this.imageUrl = imageUrl;
		this.accountId = accountId;
	}

}
