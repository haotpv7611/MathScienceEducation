package com.example.demo.dtos;

public class LessonDTO {

	private long id;
	private String lessonName;
	private String lessonUrl;
	private long unitId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
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
	public LessonDTO() {
		
	}
	
}
