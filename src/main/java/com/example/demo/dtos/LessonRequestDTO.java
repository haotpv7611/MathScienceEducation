package com.example.demo.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LessonRequestDTO {
	@Min(value = 1, message = "Game Name must be greater than 0!")
	private int lessonName;
	@NotNull(message = "Lesson URL must be not null!")
	private String lessonUrl;
	private long unitId;

	public int getLessonName() {
		return lessonName;
	}

	public void setLessonName(int lessonName) {
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

}
