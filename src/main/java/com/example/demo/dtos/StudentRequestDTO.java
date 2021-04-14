package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class StudentRequestDTO {
//	private long schoolId;
//	private long gradeId;
	private long classId;
	@NotNull(message = "FullName must be not null!")
	@Length(max = 30, message = "FullName length must be less than 30!")
	private String fullName;

	private String DoB;
	@NotNull(message = "Gender must be not null!")
	@Length(max = 10, message = "Gender length must be less than 10!")
	private String gender;
	@NotNull(message = "ParentName must be not null!")
	@Length(max = 50, message = "ParentName length must be less than 50!")
	private String parentName;
	@NotNull(message = "ParentPhone must be not null!")
	@Length(max = 50, message = "ParentPhone length must be less than 50!")
	private String contact;

//	/**
//	 * @return the schoolId
//	 */
//	public long getSchoolId() {
//		return schoolId;
//	}
//
//	/**
//	 * @param schoolId the schoolId to set
//	 */
//	public void setSchoolId(long schoolId) {
//		this.schoolId = schoolId;
//	}
//
//	/**
//	 * @return the gradeId
//	 */
//	public long getGradeId() {
//		return gradeId;
//	}
//
//	/**
//	 * @param gradeId the gradeId to set
//	 */
//	public void setGradeId(long gradeId) {
//		this.gradeId = gradeId;
//	}

	public StudentRequestDTO() {
	}

	public StudentRequestDTO(long classId,
			@NotNull(message = "FullName must be not null!") @Length(max = 30, message = "FullName length must be less than 30!") String fullName,
			String doB,
			@NotNull(message = "Gender must be not null!") @Length(max = 10, message = "Gender length must be less than 10!") String gender,
			@NotNull(message = "ParentName must be not null!") @Length(max = 50, message = "ParentName length must be less than 50!") String parentName,
			@NotNull(message = "ParentPhone must be not null!") @Length(max = 50, message = "ParentPhone length must be less than 50!") String contact) {
		super();
		this.classId = classId;
		this.fullName = fullName;
		DoB = doB;
		this.gender = gender;
		this.parentName = parentName;
		this.contact = contact;
	}

	/**
	 * @return the classId
	 */
	public long getClassId() {
		return classId;
	}

	/**
	 * @param classId the classId to set
	 */
	public void setClassId(long classId) {
		this.classId = classId;
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
