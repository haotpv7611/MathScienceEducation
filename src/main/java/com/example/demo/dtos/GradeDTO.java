package com.example.demo.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class GradeDTO {

	private long id;
	private int gradeName;
	private boolean isDisable;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime createdDate;
	private String createdBy;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime modifiedDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getGradeName() {
		return gradeName;
	}
	public void setGradeName(int gradeName) {
		this.gradeName = gradeName;
	}
	public boolean isDisable() {
		return isDisable;
	}
	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public GradeDTO() {
		
	}
	public GradeDTO(long id, int gradeName, boolean isDisable) {
		super();
		this.id = id;
		this.gradeName = gradeName;
		this.isDisable = isDisable;
	}
	
	
	
}
