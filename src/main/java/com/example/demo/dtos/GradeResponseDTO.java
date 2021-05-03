package com.example.demo.dtos;

public class GradeResponseDTO implements Comparable<GradeResponseDTO> {
	private int id;
	private int gradeName;
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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
	public int compareTo(GradeResponseDTO o) {
		if (gradeName == o.gradeName)
			return 0;
		else if (gradeName > o.gradeName)
			return 1;
		else
			return -1;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
