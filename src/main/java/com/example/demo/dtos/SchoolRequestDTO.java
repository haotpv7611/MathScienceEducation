package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class SchoolRequestDTO {
	@NotNull(message = "SchoolName must be not null!")
	@Length(max = 50, message = "SchoolName length must be less than 50!")
	private String schoolName;
	@NotNull(message = "SchoolStreet must be not null!")
	@Length(max = 100, message = "SchoolName length must be less than 100!")
	private String schoolStreet;
	@NotNull(message = "SchoolDistrict must be not null!")
	@Length(max = 20, message = "SchoolDistrict length must be less than 20!")
	private String schoolDistrict;
	@NotNull(message = "SchoolLevel must be not null!")
	@Length(max = 20, message = "SchoolLevel length must be less than 20!")
	private String schoolLevel;

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

}
