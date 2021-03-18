package com.example.demo.dtos;

import java.util.List;

public class UnitViewDTO {
	List<UnitDTO> unit;
	ProgressTestDTO progressTest;

	public UnitViewDTO() {
	}

	public UnitViewDTO(List<UnitDTO> unit, ProgressTestDTO progressTest) {
		super();
		this.unit = unit;
		this.progressTest = progressTest;
	}

	/**
	 * @return the unit
	 */
	public List<UnitDTO> getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(List<UnitDTO> unit) {
		this.unit = unit;
	}

	/**
	 * @return the progressTest
	 */
	public ProgressTestDTO getProgressTest() {
		return progressTest;
	}

	/**
	 * @param progressTest the progressTest to set
	 */
	public void setProgressTest(ProgressTestDTO progressTest) {
		this.progressTest = progressTest;
	}

}
