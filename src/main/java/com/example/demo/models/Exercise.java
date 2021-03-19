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
	private long id;
	private int exerciseName;
	private Date createdDate;
	private String createdBy;
	private Date modifiedDate;
	private String modifiedBy;
	
	private long lessonId;
	private long progressTestId;
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
	public boolean isDisable() {
		return isDisable;
	}
	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}
	public long getId() {
		return id;
	}
	
	
	
	
}
