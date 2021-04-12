package com.example.demo.dtos;

import java.util.List;

public class UnitViewDTO {
	private String subjectName;
	private List<UnitResponseDTO> unit;
	private ProgressTestResponseDTO progressTest;

	public UnitViewDTO() {
	}

	public UnitViewDTO(String subjectName, List<UnitResponseDTO> unit, ProgressTestResponseDTO progressTest) {
		super();
		this.subjectName = subjectName;
		this.unit = unit;
		this.progressTest = progressTest;
	}

	/**
	 * @return the subjectName
	 */
	public String getSubjectName() {
		return subjectName;
	}

	/**
	 * @param subjectName the subjectName to set
	 */
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	/**
	 * @return the unit
	 */
	public List<UnitResponseDTO> getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(List<UnitResponseDTO> unit) {
		this.unit = unit;
	}

	/**
	 * @return the progressTest
	 */
	public ProgressTestResponseDTO getProgressTest() {
		return progressTest;
	}

	/**
	 * @param progressTest the progressTest to set
	 */
	public void setProgressTest(ProgressTestResponseDTO progressTest) {
		this.progressTest = progressTest;
	}

}
