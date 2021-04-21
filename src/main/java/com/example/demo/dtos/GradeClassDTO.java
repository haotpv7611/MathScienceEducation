package com.example.demo.dtos;

import java.util.List;

public class GradeClassDTO {
	private int id;
	private String name;
	private List<ClassChangeDTO> classesList;

	public GradeClassDTO() {
	}

	public GradeClassDTO(int id, int name, List<ClassChangeDTO> classesList) {
		super();
		this.id = id;
		this.name = "Grade " + name;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
