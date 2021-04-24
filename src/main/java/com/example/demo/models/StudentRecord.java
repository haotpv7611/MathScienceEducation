package com.example.demo.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StudentRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long accountId;
	private long exerciseId;
	private long unitId;
	private long progressTestId;
	private String listExerciseTakenScore;
	private float averageScore;

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
	 * @return the unitId
	 */
	public long getUnitId() {
		return unitId;
	}

	/**
	 * @param unitId the unitId to set
	 */
	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	/**
	 * @return the progressTestId
	 */
	public long getProgressTestId() {
		return progressTestId;
	}

	/**
	 * @param progressTestId the progressTestId to set
	 */
	public void setProgressTestId(long progressTestId) {
		this.progressTestId = progressTestId;
	}

	/**
	 * @return the listExerciseTakenScore
	 */
	public String getListExerciseTakenScore() {
		return listExerciseTakenScore;
	}

	/**
	 * @param listExerciseTakenScore the listExerciseTakenScore to set
	 */
	public void setListExerciseTakenScore(String listExerciseTakenScore) {
		this.listExerciseTakenScore = listExerciseTakenScore;
	}

	/**
	 * @return the averageScore
	 */
	public float getAverageScore() {
		return averageScore;
	}

	/**
	 * @param averageScore the averageScore to set
	 */
	public void setAverageScore(float averageScore) {
		this.averageScore = averageScore;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

}
