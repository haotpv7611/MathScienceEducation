package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class LessonRequestDTO {

	private long id;
	
	@NotNull(message = "Lesson Name Must Be Not Null !")
	@Length(max = 20, message = "Lesson Name length must be less than 20!")
	private String lessonName;
	@NotNull(message = "Lesson URL Must Be Not Null !")
	private String lessonUrl;
	private long unitId;
	
	
	

	public String getLessonName() {
		return lessonName;
	}
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
