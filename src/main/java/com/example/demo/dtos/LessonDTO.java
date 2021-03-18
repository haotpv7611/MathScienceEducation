package com.example.demo.dtos;

public class LessonDTO {

	private Long id;
	private int lessonName;
	private String lessonUrl;
	private Long unitId;
	public LessonDTO() {
	
	}
	public LessonDTO(Long id, int lessonName, String lessonUrl, Long unitId) {
		super();
		this.id = id;
		this.lessonName = lessonName;
		this.lessonUrl = lessonUrl;
		this.unitId = unitId;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
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
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	
	
	
	
}
