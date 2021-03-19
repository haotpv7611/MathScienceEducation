package com.example.demo.dtos;

public class BannerImageDTO {
	
	private long id;
	private String description;
	private long accountId;
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
	public BannerImageDTO() {
		
	}
	public BannerImageDTO(long id, String description, long accountId) {
		super();
		this.id = id;
		this.description = description;
		this.accountId = accountId;
	}
	
	
	
	

}
