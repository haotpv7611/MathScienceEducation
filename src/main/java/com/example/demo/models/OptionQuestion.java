package com.example.demo.models;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
//@EntityListeners(AuditingEntityListener.class)
public class OptionQuestion {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String optionText;
	private String optionImageUrl;
	private String optionAudioUrl;
	private boolean isCorrect;
	private long questionId;
	
//	@CreatedDate
	private LocalDateTime createdDate;
//	@CreatedBy
	private String createdBy;
//	@LastModifiedDate
	private LocalDateTime modifiedDate;
//	@LastModifiedBy
	private String modifiedBy;
	
	private boolean isDisable;

	
	public String getOptionText() {
		return optionText;
	}

	public void setOptionText(String optionText) {
		this.optionText = optionText;
	}

	public String getOptionImageUrl() {
		return optionImageUrl;
	}

	public void setOptionImageUrl(String optionImageUrl) {
		this.optionImageUrl = optionImageUrl;
	}

	public String getOptionAudioUrl() {
		return optionAudioUrl;
	}

	public void setOptionAudioUrl(String optionAudioUrl) {
		this.optionAudioUrl = optionAudioUrl;
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
}
