package com.example.demo.dtos;

import java.util.Date;

public class NewsDTO {

	private long id;
	private String title;
	private String shortDescription;
	private String newsContent;
	private Date createdDate;

	public NewsDTO() {
		// TODO Auto-generated constructor stub
	}

	public NewsDTO(long id, String title, String shortDescription, String newsContent, Date createdDate) {
		super();
		this.id = id;
		this.title = title;
		this.shortDescription = shortDescription;
		this.newsContent = newsContent;
		this.createdDate = createdDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getNewsContent() {
		return newsContent;
	}

	public void setNewsContent(String newsContent) {
		this.newsContent = newsContent;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
