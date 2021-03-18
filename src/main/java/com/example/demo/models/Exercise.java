package com.example.demo.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Exercise {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private int exerciseName;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	
	private Long lessonId;
	private Long progressTestId;
	private boolean isProgressTest;
	private boolean isDisable;
	public int getExerciseName() {
		return exerciseName;
	}
	public void setExerciseName(int exerciseName) {
		this.exerciseName = exerciseName;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Long getLessonId() {
		return lessonId;
	}
	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}
	public Long getProgressTestId() {
		return progressTestId;
	}
	public void setProgressTestId(Long progressTestId) {
		this.progressTestId = progressTestId;
	}
	public boolean isProgressTest() {
		return isProgressTest;
	}
	public void setProgressTest(boolean isProgressTest) {
		this.isProgressTest = isProgressTest;
	}
	public boolean isDisable() {
		return isDisable;
	}
	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}
	public Long getId() {
		return id;
	}
	
	
}
