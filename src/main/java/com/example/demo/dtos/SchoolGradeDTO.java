package com.example.demo.dtos;

public class SchoolGradeDTO {
	private long schoolId;
	private int gradeId;

	public SchoolGradeDTO() {
	}

	public SchoolGradeDTO(long schoolId, int gradeId) {
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
	public int getGradeId() {
		return gradeId;
	}

	/**
	 * @param gradeId the gradeId to set
	 */
	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
	}

}
