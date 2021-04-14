package com.example.demo.dtos;

import java.util.List;

public class LessonScoreViewDTO {
	private int lessonName;
	private List<ExerciseResponseDTO> exerciseResponseDTOList;

	public LessonScoreViewDTO() {
	}

	public LessonScoreViewDTO(int lessonName, List<ExerciseResponseDTO> exerciseResponseDTOList) {
		super();
		this.lessonName = lessonName;
		this.exerciseResponseDTOList = exerciseResponseDTOList;
	}

	/**
	 * @return the lessonName
	 */
	public int getLessonName() {
		return lessonName;
	}

	/**
	 * @param lessonName the lessonName to set
	 */
	public void setLessonName(int lessonName) {
		this.lessonName = lessonName;
	}

	/**
	 * @return the exerciseResponseDTOList
	 */
	public List<ExerciseResponseDTO> getExerciseResponseDTOList() {
		return exerciseResponseDTOList;
	}

	/**
	 * @param exerciseResponseDTOList the exerciseResponseDTOList to set
	 */
	public void setExerciseResponseDTOList(List<ExerciseResponseDTO> exerciseResponseDTOList) {
		this.exerciseResponseDTOList = exerciseResponseDTOList;
	}

}
