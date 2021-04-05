package com.example.demo.dtos;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class StudentRequestDTO {
	private long schoolId;
	private long gradeId;
	private long classId;
	@NotNull(message = "FirstName must be not null!")
	@Length(max = 30, message = "FirstName length must be less than 30!")
	private String firtName;
	@NotNull(message = "LastName must be not null!")
	@Length(max = 50, message = "LastName length must be less than 50!")
	private String lastName;
	private String DoB;
	@NotNull(message = "Gender must be not null!")
	@Length(max = 10, message = "Gender length must be less than 10!")
	private String gender;
	@NotNull(message = "ParentName must be not null!")
	@Length(max = 50, message = "ParentName length must be less than 50!")
	private String parentName;
	@NotNull(message = "ParentPhone must be not null!")
	@Length(max = 50, message = "ParentPhone length must be less than 50!")
	private String parentPhone;

	/**
	 * @return the schoolId
	 */
	public long getSchoolId() {
		return schoolId;
	}

	/**
	 * @param schoolId the schoolId to set
	 */
	public void setSchoolId(long schoolId) {
		this.schoolId = schoolId;
	}

	/**
	 * @return the gradeId
	 */
	public long getGradeId() {
		return gradeId;
	}

	/**
	 * @param gradeId the gradeId to set
	 */
	public void setGradeId(long gradeId) {
		this.gradeId = gradeId;
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
	 * @return the firtName
	 */
	public String getFirtName() {
		return firtName;
	}

	/**
	 * @param firtName the firtName to set
	 */
	public void setFirtName(String firtName) {
		this.firtName = firtName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the parentPhone
	 */
	public String getParentPhone() {
		return parentPhone;
	}

	/**
	 * @param parentPhone the parentPhone to set
	 */
	public void setParentPhone(String parentPhone) {
		this.parentPhone = parentPhone;
	}

}
