package com.example.demo.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class ProgressTestRequestDTO {
	@NotNull(message = "Progress Test Name must be not null !")
	@Length(max = 20, message = "ProgressTest Name length must be less than 20!")
	private String progressTestName;
	@Min(value = 0, message = "Unit After must be greater than 0!")
	private long unitAfterId;
	private long subjectId;
	@Length(max = 50, message = "Description lenght must be less than 50 !")
	private String description;

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
