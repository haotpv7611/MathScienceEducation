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
public class OptionQuestion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String optionText;
	private String optionImageUrl;
	private String optionInputType;
	private boolean isCorrect;
	private long questionId;
	private boolean isDisable;

	@CreatedDate
	private LocalDateTime createdDate;
//	@CreatedBy
	private String createdBy;
	@LastModifiedDate
	private LocalDateTime modifiedDate;
//	@LastModifiedBy
	private String modifiedBy;

	@PrePersist
	public void onCreate() {
		this.createdDate = LocalDateTime.now(ZoneId.of("UTC+7"));
		this.modifiedDate = LocalDateTime.now(ZoneId.of("UTC+7"));
	}

	@PreUpdate
	public void onUpdate() {
		this.modifiedDate = LocalDateTime.now(ZoneId.of("UTC+7"));
	}

	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(long questionId) {
		this.questionId = questionId;
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

	public long getId() {
		return id;
	}

	/**
	 * @return the optionImageUrl
	 */
	public String getOptionImageUrl() {
		return optionImageUrl;
	}

	/**
	 * @param optionImageUrl the optionImageUrl to set
	 */
	public void setOptionImageUrl(String optionImageUrl) {
		this.optionImageUrl = optionImageUrl;
	}

	/**
	 * @return the optionInputType
	 */
	public String getOptionInputType() {
		return optionInputType;
	}

	/**
	 * @param optionInputType the optionInputType to set
	 */
	public void setOptionInputType(String optionInputType) {
		this.optionInputType = optionInputType;
	}

	public OptionQuestion() {
	}

	public OptionQuestion(String optionText, boolean isCorrect, long questionId, boolean isDisable) {
		super();
		this.optionText = optionText;
		this.isCorrect = isCorrect;
		this.questionId = questionId;
		this.isDisable = isDisable;
	}

	public OptionQuestion(String optionText, String optionInputType, long questionId, boolean isDisable) {
		super();
		this.optionText = optionText;
		this.optionInputType = optionInputType;
		this.questionId = questionId;
		this.isDisable = isDisable;
	}

	public OptionQuestion(String optionText, long questionId, boolean isDisable) {
		super();
		this.optionText = optionText;
		this.questionId = questionId;
		this.isDisable = isDisable;
	}

}
