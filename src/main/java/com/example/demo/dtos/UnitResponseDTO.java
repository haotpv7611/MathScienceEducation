package com.example.demo.dtos;

public class UnitResponseDTO {
	private long id;
	private int unitName;
	private long subjectId;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getUnitName() {
		return unitName;
	}

	public void setUnitName(int unitName) {
		this.unitName = unitName;
	}

	public long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(long subjectId) {
		this.subjectId = subjectId;
	}

}
