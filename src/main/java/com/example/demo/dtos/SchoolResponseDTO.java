package com.example.demo.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SchoolResponseDTO {
	private long id;
	private String schoolName;
	private String schoolStreet;
	private String schoolDistrict;
	private String schoolCode;
	private String schoolLevel;
	private String status;

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime createdDate;
	private String createdBy;
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime modifiedDate;
	private String modifiedBy;

	public SchoolResponseDTO() {

	}

	public SchoolResponseDTO(String schoolName, String schoolStreet, String schoolDistrict, String schoolLevel) {
		super();
		this.schoolName = schoolName;
		this.schoolStreet = schoolStreet;
		this.schoolDistrict = schoolDistrict;
		this.schoolLevel = schoolLevel;
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
	 * @return the schoolName
	 */
	public String getSchoolName() {
		return schoolName;
	}

	/**
	 * @param schoolName the schoolName to set
	 */
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	/**
	 * @return the schoolStreet
	 */
	public String getSchoolStreet() {
		return schoolStreet;
	}

	/**
	 * @param schoolStreet the schoolStreet to set
	 */
	public void setSchoolStreet(String schoolStreet) {
		this.schoolStreet = schoolStreet;
	}

	/**
	 * @return the schoolDistrict
	 */
	public String getSchoolDistrict() {
		return schoolDistrict;
	}

	/**
	 * @param schoolDistrict the schoolDistrict to set
	 */
	public void setSchoolDistrict(String schoolDistrict) {
		this.schoolDistrict = schoolDistrict;
	}

	/**
	 * @return the schoolCode
	 */
	public String getSchoolCode() {
		return schoolCode;
	}

	/**
	 * @param schoolCode the schoolCode to set
	 */
	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
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

	/**
	 * @return the schoolLevel
	 */
	public String getSchoolLevel() {
		return schoolLevel;
	}

	/**
	 * @param schoolLevel the schoolLevel to set
	 */
	public void setSchoolLevel(String schoolLevel) {
		this.schoolLevel = schoolLevel;
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

}
