package com.example.demo.dtos;

public class SchoolGradeDTO {
	private long schoolId;
	private long gradeId;

	public SchoolGradeDTO() {
	}

	public SchoolGradeDTO(long schoolId, long gradeId) {
		super();
		this.schoolId = schoolId;
		this.gradeId = gradeId;
	}

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

}
