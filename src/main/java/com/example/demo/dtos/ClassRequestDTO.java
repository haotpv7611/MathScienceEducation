package com.example.demo.dtos;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class ClassRequestDTO {
	private int gradeId;
	private long schoolId;
	@NotNull(message = "ClassName must be not null!")
	@NotEmpty(message = "Input cannot blank!")
	@Length(max = 20, message = "ClassName length must be less than 20!")
	private String className;

	public ClassRequestDTO() {
	}

	public ClassRequestDTO(int gradeId, long schoolId,
			@NotNull(message = "ClassName must be not null!") @Length(max = 20, message = "ClassName length must be less than 20!") String className) {
		super();
		this.gradeId = gradeId;
		this.schoolId = schoolId;
		this.className = className;
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
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

}
