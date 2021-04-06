package com.example.demo.dtos;

import org.hibernate.validator.constraints.Length;

public class UnitRequestDTO {

	private long subjectId;
	private int unitName;
	@Length(max = 50, message = "Description length must be less than 50")
	private String description;

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

	public int getUnitName() {
		return unitName;
	}

	public void setUnitName(int unitName) {
		this.unitName = unitName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
