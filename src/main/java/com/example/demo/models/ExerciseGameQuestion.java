package com.example.demo.models;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class ExerciseGameQuestion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long questionId;
	private long exerciseId;
	private long gameId;
	private boolean isExerciseQuestions;
	private boolean isDisable;

	@CreatedDate
	private LocalDateTime createdDate;
	@CreatedBy
	private String createdBy;
	@LastModifiedDate
	private LocalDateTime modifiedDate;
	@LastModifiedBy
	private String modifiedBy;

	@PrePersist
	public void onCreate() {
		this.createdDate = LocalDateTime.now(ZoneId.of("UTC+7"));
		this.modifiedDate = null;
		this.modifiedBy = null;
	}
	
	@PreUpdate
    public void onUpdate() {
        this.modifiedDate = LocalDateTime.now(ZoneId.of("UTC+7"));
    }

	/**
	 * @return the isExerciseQuestions
	 */
	public boolean isExerciseQuestions() {
		return isExerciseQuestions;
	}

	/**
	 * @param isExerciseQuestions the isExerciseQuestions to set
	 */
	public void setExerciseQuestions(boolean isExerciseQuestions) {
		this.isExerciseQuestions = isExerciseQuestions;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @return the questionId
	 */
	public long getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(long questionId) {
		this.questionId = questionId;
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
	 * @return the isDisable
	 */
	public boolean isDisable() {
		return isDisable;
	}

	/**
	 * @param isDisable the isDisable to set
	 */
	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	public ExerciseGameQuestion() {
	}

	public ExerciseGameQuestion(long questionId, long exerciseId, long gameId, boolean isExerciseQuestions,
			boolean isDisable) {
		super();
		this.questionId = questionId;
		this.exerciseId = exerciseId;
		this.gameId = gameId;
		this.isExerciseQuestions = isExerciseQuestions;
		this.isDisable = isDisable;
	}

}
