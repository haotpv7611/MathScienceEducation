package com.example.demo.dtos;

public class LessonResponseDTO {
	private long id;
	private int lessonName;
	private String lessonUrl;

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

}
