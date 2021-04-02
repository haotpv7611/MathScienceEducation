package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class ProgressTestDTO {
	private long id;
	@NotNull(message = "Progress Test Name must be not null !")
	@Length(max =  20, message = "Progress Test Name lenght must be less than 20 !")
	private String progressTestName;
	private long unitAfterId;
	private long subjectId;
	@Length(max = 50, message = "Description lenght must be less than 50 !")
	private String description;
	
	

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ProgressTestDTO() {
	}

	public ProgressTestDTO(long id, String progressTestName, long unitAfterId, long subjectId) {
		super();
		this.id = id;
		this.progressTestName = progressTestName;
		this.unitAfterId = unitAfterId;
		this.subjectId = subjectId;
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
	 * @return the progressTestName
	 */
	public String getProgressTestName() {
		return progressTestName;
	}

	/**
	 * @param progressTestName the progressTestName to set
	 */
	public void setProgressTestName(String progressTestName) {
		this.progressTestName = progressTestName;
	}

	/**
	 * @return the subjectId
	 */
	public long getSubjectId() {
		return subjectId;
	}

	/**
	 * @param subjectId the subjectId to set
	 */
	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	/**
	 * @return the unitAfterId
	 */
	public long getUnitAfterId() {
		return unitAfterId;
	}

	/**
	 * @param unitAfterId the unitAfterId to set
	 */
	public void setUnitAfterId(long unitAfterId) {
		this.unitAfterId = unitAfterId;
	}
}
