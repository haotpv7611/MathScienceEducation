package com.example.demo.dtos;

public class ExerciseGameQuestionDTO {
	private long questionId;
	private long exerciseId;
	private long gameId;
	private boolean isExercise;

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

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
	}

	public long getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(long exerciseId) {
		this.exerciseId = exerciseId;
	}

	public long getGameId() {
		return gameId;
	}

	public void setGameId(long gameId) {
		this.gameId = gameId;
	}

}
