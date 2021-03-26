package com.example.demo.dtos;

public class LessonDTO {

	private long id;
	private int lessonName;
	private String lessonUrl;
	private long unitId;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	public LessonDTO() {
		
	}
	public LessonDTO(long id, int lessonName, String lessonUrl, long unitId) {
		super();
		this.id = id;
		this.lessonName = lessonName;
		this.lessonUrl = lessonUrl;
		this.unitId = unitId;
	}
	
	
	
	
	
	
	
}