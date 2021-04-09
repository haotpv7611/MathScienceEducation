package com.example.demo.dtos;

import java.util.List;

public class UnitViewDTO {
	List<UnitResponseDTO> unit;
	ProgressTestResponseDTO progressTest;

	public UnitViewDTO() {
	}

	public UnitViewDTO(List<UnitResponseDTO> unit, ProgressTestResponseDTO progressTest) {
		super();
		this.unit = unit;
		this.progressTest = progressTest;
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
