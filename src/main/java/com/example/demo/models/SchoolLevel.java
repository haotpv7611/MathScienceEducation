package com.example.demo.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class SchoolLevel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String description;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "schoolLevel")
	private List<School> schoolList;

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the schoolList
	 */
	public List<School> getSchoolList() {
		return schoolList;
	}

	/**
	 * @param schoolList the schoolList to set
	 */
	public void setSchoolList(List<School> schoolList) {
		this.schoolList = schoolList;
	}

}
