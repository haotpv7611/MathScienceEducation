package com.example.demo.dtos;

public class LessonRequestDTO {

	private long id;
	private String lessonName;
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
