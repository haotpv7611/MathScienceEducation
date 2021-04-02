package com.example.demo.dtos;

import java.util.Comparator;

public class GradeDTO implements Comparable<GradeDTO> {

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

	@Override
	public int compareTo(GradeDTO o) {
		if (gradeName == o.gradeName)
			return 0;
		else if (gradeName > o.gradeName)
			return 1;
		else
			return -1;
	}

}
