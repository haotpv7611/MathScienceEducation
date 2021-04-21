package com.example.demo.dtos;

import java.util.List;

public class GradeClassDTO {
	private int id;
	private String gradeName;
	private List<ClassChangeDTO> classesList;

	public GradeClassDTO() {
	}

	public GradeClassDTO(int id, int gradeName, List<ClassChangeDTO> classesList) {
		super();
		this.id = id;
		this.gradeName = "Grade " + gradeName;
		this.classesList = classesList;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the gradeName
	 */
	public String getGradeName() {
		return gradeName;
	}

	/**
	 * @param gradeName the gradeName to set
	 */
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	/**
	 * @return the classesList
	 */
	public List<ClassChangeDTO> getClassesList() {
		return classesList;
	}

	/**
	 * @param classesList the classesList to set
	 */
	public void setClassesList(List<ClassChangeDTO> classesList) {
		this.classesList = classesList;
	}

}
