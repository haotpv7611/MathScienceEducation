package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class StudentRequestDTO {

	@NotNull(message = "DoB must be not null!")
	@Length(max = 20, message = "DoB length must be less than 20!")
	private String DoB;
	@NotNull(message = "Gender must be not null!")
	@Length(max = 10, message = "Gender length must be less than 10!")
	private String gender;
	@Length(max = 50, message = "ParentName length must be less than 50!")
	private String parentName;
	@NotNull(message = "Contact must be not null!")
	@Length(max = 100, message = "Contact length must be less than 100!")
	private String contact;
	private long classesId;
	@NotNull(message = "FullName must be not null!")
	@Length(max = 50, message = "FullName length must be less than 50!")
	private String fullName;

	public StudentRequestDTO() {
	}

	public StudentRequestDTO(long classesId,
			@NotNull(message = "FullName must be not null!") @Length(max = 30, message = "FullName length must be less than 30!") String fullName,
			String doB,
			@NotNull(message = "Gender must be not null!") @Length(max = 10, message = "Gender length must be less than 10!") String gender,
			@NotNull(message = "ParentName must be not null!") @Length(max = 50, message = "ParentName length must be less than 50!") String parentName,
			@NotNull(message = "ParentPhone must be not null!") @Length(max = 50, message = "ParentPhone length must be less than 50!") String contact) {
		super();
		this.classesId = classesId;
		this.fullName = fullName;
		DoB = doB;
		this.gender = gender;
		this.parentName = parentName;
		this.contact = contact;
	}

	/**
	 * @return the classesId
	 */
	public long getClassesId() {
		return classesId;
	}

	/**
	 * @param classesId the classesId to set
	 */
	public void setClassesId(long classesId) {
		this.classesId = classesId;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the doB
	 */
	public String getDoB() {
		return DoB;
	}

	/**
	 * @param doB the doB to set
	 */
	public void setDoB(String doB) {
		DoB = doB;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

}
