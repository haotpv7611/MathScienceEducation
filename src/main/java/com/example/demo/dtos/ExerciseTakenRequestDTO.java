package com.example.demo.dtos;

public class ExerciseTakenRequestDTO {
	private long accountId;
	private long exerciseId;
	private float mark;
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
	public float getMark() {
		return mark;
	}

	/**
	 * @param mark the mark to set
	 */
	public void setMark(float mark) {
		this.mark = mark;
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
