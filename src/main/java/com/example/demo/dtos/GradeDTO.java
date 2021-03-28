package com.example.demo.dtos;

public class GradeDTO {

	private long id;
	private String gradeName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the gradeName
	 */
	public String getGradeName() {
		return gradeName;
	}

	/**
	 * @param gradeName the gradeName to set
	 */
	public void setGradeName(Integer gradeName) {
		this.gradeName = "Grade " + gradeName;
	}
}
