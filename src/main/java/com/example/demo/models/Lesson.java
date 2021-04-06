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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Lesson {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String lessonName;
	private String lessonUrl;
	private long unitId;
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

	public String getLessonName() {
		return lessonName;
	}

	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	public String getLessonUrl() {
		return lessonUrl;
	}

	public void setLessonUrl(String lessonUrl) {
		this.lessonUrl = lessonUrl;
	}

	public long getUnitId() {
		return unitId;
	}

	public void setUnitId(long unitId) {
		this.unitId = unitId;
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
