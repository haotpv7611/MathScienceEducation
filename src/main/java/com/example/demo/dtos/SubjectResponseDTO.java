package com.example.demo.dtos;

public class SubjectResponseDTO {
	private long id;
	private String subjectName;
	private String imageUrl;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

//	public long getGradeId() {
//		return gradeId;
//	}
//
//	public void setGradeId(long gradeId) {
//		this.gradeId = gradeId;
//	}

}
