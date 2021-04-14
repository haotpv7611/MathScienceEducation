package com.example.demo.dtos;

import javax.validation.constraints.Min;

public class ExerciseTakenRequestDTO {
	private long accountId;
	private long exerciseId;
	@Min(value = 0, message = "Total Score must be greater than 0!")
	private float totalScore;
	private String takenObject;

	/**
	 * @return the accountId
	 */
	public long getAccountId() {
		return accountId;
	}

	/**
	 * @param accountId the accountId to set
	 */
	public void setAccountId(long accountId) {
		this.accountId = accountId;
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
	 * @return the mark
	 */
	public float getTotalScore() {
		return totalScore;
	}

	/**
	 * @param mark the mark to set
	 */
	public void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}

	/**
	 * @return the takenObject
	 */
	public String getTakenObject() {
		return takenObject;
	}

	/**
	 * @param takenObject the takenObject to set
	 */
	public void setTakenObject(String takenObject) {
		this.takenObject = takenObject;
	}

}
