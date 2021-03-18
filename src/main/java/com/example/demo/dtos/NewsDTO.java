package com.example.demo.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class NewsDTO {

	private long id;
	private String newsTitle;
	private String shortDescription;
	private String newsContent;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime createdDate;

	public NewsDTO() {
	}

	public NewsDTO(long id, String newsTitle, String shortDescription, String newsContent, LocalDateTime createdDate) {
		super();
		this.id = id;
		this.newsTitle = newsTitle;
		this.shortDescription = shortDescription;
		this.newsContent = newsContent;
		this.createdDate = createdDate;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the newsTitle
	 */
	public String getNewsTitle() {
		return newsTitle;
	}

	/**
	 * @param newsTitle the newsTitle to set
	 */
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}

	/**
	 * @return the shortDescription
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * @param shortDescription the shortDescription to set
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * @return the newsContent
	 */
	public String getNewsContent() {
		return newsContent;
	}

	/**
	 * @param newsContent the newsContent to set
	 */
	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
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

}
