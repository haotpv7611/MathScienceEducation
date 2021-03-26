package com.example.demo.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class UnitResponseDTO {

	private long id;
	private int unitName;
	private long subjectId;
	private boolean isDisable;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime createdDate;
	private String createdBy;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime modifiedDate;
	private String description;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getUnitName() {
		return unitName;
	}
	public void setUnitName(int unitName) {
		this.unitName = unitName;
	}
	public long getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
