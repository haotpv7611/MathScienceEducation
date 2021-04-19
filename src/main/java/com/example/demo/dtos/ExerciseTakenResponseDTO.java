package com.example.demo.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ExerciseTakenResponseDTO {
	private long id;
	private String exerciseName;
	private String totalScore;
	@JsonFormat(pattern = "dd-MMM-yyyy HH:mm")
	private LocalDateTime createdDate;

	public ExerciseTakenResponseDTO() {
	}

	public ExerciseTakenResponseDTO(long id, float totalScore, LocalDateTime createdDate) {
		super();
		this.id = id;
		this.totalScore = totalScore + "/10";
		this.createdDate = createdDate;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the exerciseName
	 */
	public String getExerciseName() {
		return exerciseName;
	}

	/**
	 * @param exerciseName the exerciseName to set
	 */
	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}

	/**
	 * @return the score
	 */
	public String getTotalScore() {
		return totalScore;
	}

	/**
	 * @param score the score to set
	 */
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	/**
	 * @return the createdDate
	 */
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

}
