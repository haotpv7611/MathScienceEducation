package com.example.demo.dtos;

public class GradeDTO {

	private long id;
	private int gradeName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the gradeName
	 */
	public int getGradeName() {
		return gradeName;
	}

	/**
	 * @param gradeName the gradeName to set
	 */
	public void setGradeName(int gradeName) {
		this.gradeName = gradeName;
	}

}
