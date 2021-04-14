package com.example.demo.dtos;

import java.util.List;

import javax.validation.constraints.NotEmpty;

public class ExerciseGameQuestionRequestDTO {
	@NotEmpty(message = "List QuestionId must not empty")
	private List<Long> questionIds;
	private long exerciseId;
	private long gameId;
	private boolean isExercise;

	/**
	 * @return the questionIds
	 */
	public List<Long> getQuestionIds() {
		return questionIds;
	}

	/**
	 * @param questionIds the questionIds to set
	 */
	public void setQuestionIds(List<Long> questionIds) {
		this.questionIds = questionIds;
	}

	/**
	 * @return the exerciseId
	 */
	public long getExerciseId() {
		return exerciseId;
	}

	/**
	 * @param exerciseId the exerciseId to set
	 */
	public void setExerciseId(long exerciseId) {
		this.exerciseId = exerciseId;
	}

	/**
	 * @return the gameId
	 */
	public long getGameId() {
		return gameId;
	}

	/**
	 * @param gameId the gameId to set
	 */
	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return the isExercise
	 */
	public boolean isExercise() {
		return isExercise;
	}

	/**
	 * @param isExercise the isExercise to set
	 */
	public void setExercise(boolean isExercise) {
		this.isExercise = isExercise;
	}

}
