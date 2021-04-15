package com.example.demo.dtos;

import java.util.List;

public class ScoreResponseDTO {
	private String unitName;
	private String process;
	private List<LessonScoreViewDTO> lessonScoreViewDTOList;

	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(int unitName) {
		this.unitName = "Unit " + unitName;
	}

	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * @return the process
	 */
	public String getProcess() {
		return process;
	}

	/**
	 * @param process the process to set
	 */
	public void setProcess(String process) {
		this.process = process;
	}

	/**
	 * @return the lessonScoreViewDTOList
	 */
	public List<LessonScoreViewDTO> getLessonScoreViewDTOList() {
		return lessonScoreViewDTOList;
	}

	/**
	 * @param lessonScoreViewDTOList the lessonScoreViewDTOList to set
	 */
	public void setLessonScoreViewDTOList(List<LessonScoreViewDTO> lessonScoreViewDTOList) {
		this.lessonScoreViewDTOList = lessonScoreViewDTOList;
	}

}
