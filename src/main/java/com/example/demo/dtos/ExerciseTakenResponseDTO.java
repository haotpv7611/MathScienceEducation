package com.example.demo.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ExerciseTakenResponseDTO {
	private long id;
	private String score;
	@JsonFormat(pattern = "dd-MMM-yyyy HH:mm")
	private LocalDateTime createdDate;

	public ExerciseTakenResponseDTO() {
	}

	public ExerciseTakenResponseDTO(long id, float score, LocalDateTime createdDate) {
		super();
		this.id = id;
		this.score = score + "/10";
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
	 * @return the score
	 */
	public String getScore() {
		return score;
	}

	/**
	 * @param score the score to set
	 */
	public void setScore(String score) {
		this.score = score;
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
