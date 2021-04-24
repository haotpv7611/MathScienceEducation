package com.example.demo.models;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class ExerciseTaken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long accountId;
	private long exerciseId;
	private long unitId;
	private long progressTestId;
	private float totalScore;
	private String takenObject;

	@CreatedDate
	private LocalDateTime createdDate;

	@PrePersist
	public void onCreate() {
		this.createdDate = LocalDateTime.now(ZoneId.of("UTC+7"));
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

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
	 * @return the totalScore
	 */
	public float getTotalScore() {
		return totalScore;
	}

	/**
	 * @param totalScore the totalScore to set
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
