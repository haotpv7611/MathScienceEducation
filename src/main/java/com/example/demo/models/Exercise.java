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
public class Exercise {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private int exerciseName;
	private String description;
	private long lessonId;
	private long progressTestId;
	private boolean isProgressTest;
	private String status;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExerciseName() {
		return exerciseName;
	}

	public void setExerciseName(int exerciseName) {
		this.exerciseName = exerciseName;
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

	public long getLessonId() {
		return lessonId;
	}

	public void setLessonId(long lessonId) {
		this.lessonId = lessonId;
	}

	public long getProgressTestId() {
		return progressTestId;
	}

	public void setProgressTestId(long progressTestId) {
		this.progressTestId = progressTestId;
	}

	public boolean isProgressTest() {
		return isProgressTest;
	}

	public void setProgressTest(boolean isProgressTest) {
		this.isProgressTest = isProgressTest;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public long getId() {
		return id;
	}

	public Exercise() {
	}

	public Exercise(int exerciseName, String description, long lessonId, long progressTestId, boolean isProgressTest,
			String status) {
		super();
		this.exerciseName = exerciseName;
		this.description = description;
		this.lessonId = lessonId;
		this.progressTestId = progressTestId;
		this.isProgressTest = isProgressTest;
		this.status = status;
	}

}
