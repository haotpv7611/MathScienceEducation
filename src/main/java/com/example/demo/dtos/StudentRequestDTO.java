package com.example.demo.dtos;

import java.time.LocalDate;

public class StudentRequestDTO {
	private long accountId;
	private long classId;
	private String firtName;
	private String lastName;
	private LocalDate DoB;
	private String gender;
	private String parentName;
	private String parentPhone;

	/**
	 * @return the accountId
	 */
	public long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(long accountId) {
		this.accountId = accountId;
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
	public LocalDate getDoB() {
		return DoB;
	}

	/**
	 * @param doB the doB to set
	 */
	public void setDoB(LocalDate doB) {
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
