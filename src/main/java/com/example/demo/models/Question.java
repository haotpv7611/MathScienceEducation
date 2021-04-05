package com.example.demo.models;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String questionTitle;
	private String description;
	private String questionImageUrl;
	private String questionAudioUrl;
	private float score;
	private long questionTypeId;

	@CreatedDate
	private LocalDateTime createdDate;
//	@CreatedBy
	private String createdBy;
	@LastModifiedDate
	private LocalDateTime modifiedDate;
//	@LastModifiedBy
	private String modifiedBy;

	private boolean isDisable;
	private long unitId;

	public String getQuestionTitle() {
		return questionTitle;
	}

	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQuestionImageUrl() {
		return questionImageUrl;
	}

	public void setQuestionImageUrl(String questionImageUrl) {
		this.questionImageUrl = questionImageUrl;
	}

	public String getQuestionAudioUrl() {
		return questionAudioUrl;
	}

	public void setQuestionAudioUrl(String questionAudioUrl) {
		this.questionAudioUrl = questionAudioUrl;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public long getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(long questionTypeId) {
		this.questionTypeId = questionTypeId;
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

	public boolean isDisable() {
		return isDisable;
	}

	public void setDisable(boolean isDisable) {
		this.isDisable = isDisable;
	}

	public long getUnitId() {
		return unitId;
	}

	public void setUnitId(long unitId) {
		this.unitId = unitId;
	}

	public long getId() {
		return id;
	}

	public Question(long id, String questionTitle, String description, String questionImageUrl, String questionAudioUrl,
			float score, long questionTypeId, LocalDateTime createdDate, String createdBy, LocalDateTime modifiedDate,
			String modifiedBy, boolean isDisable, long unitId) {
		super();
		this.id = id;
		this.questionTitle = questionTitle;
		this.description = description;
		this.questionImageUrl = questionImageUrl;
		this.questionAudioUrl = questionAudioUrl;
		this.score = score;
		this.questionTypeId = questionTypeId;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.modifiedDate = modifiedDate;
		this.modifiedBy = modifiedBy;
		this.isDisable = isDisable;
		this.unitId = unitId;
	}

	public Question(String questionTitle, String description, float score, long unitId, boolean isDisable) {
		super();
		this.questionTitle = questionTitle;
		this.description = description;
		this.score = score;
		this.unitId = unitId;
		this.isDisable = isDisable;
	}

	public Question() {

	}

}
