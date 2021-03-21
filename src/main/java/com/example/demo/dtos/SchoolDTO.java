package com.example.demo.dtos;

public class SchoolDTO {
	private Long id;
	private String schoolName;
	private String schoolAddress;
//	private String createdBy;

	public SchoolDTO() {
	}

	public SchoolDTO(Long id, String schoolName, String schoolAddress) {
		super();
		this.id = id;
		this.schoolName = schoolName;
		this.schoolAddress = schoolAddress;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
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
	 * @return the schoolAddress
	 */
	public String getSchoolAddress() {
		return schoolAddress;
	}

	/**
	 * @param schoolAddress the schoolAddress to set
	 */
	public void setSchoolAddress(String schoolAddress) {
		this.schoolAddress = schoolAddress;
	}

	public static Object builder() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the createdBy
	 */
//	public String getCreatedBy() {
//		return createdBy;
//	}
//
//	/**
//	 * @param createdBy the createdBy to set
//	 */
//	public void setCreatedBy(String createdBy) {
//		this.createdBy = createdBy;
//	}
}
