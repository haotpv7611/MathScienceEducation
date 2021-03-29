package com.example.demo.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Grade {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private int gradeName;
//	@OneToMany(mappedBy = "grade")
//	private Set<SchoolGrade> schoolGrade;

	public int getGradeName() {
		return gradeName;
	}

	public void setGradeName(int gradeName) {
		this.gradeName = gradeName;
	}

	public long getId() {
		return id;
	}
//
//	/**
//	 * @return the schoolGrade
//	 */
//	public Set<SchoolGrade> getSchoolGrade() {
//		return schoolGrade;
//	}
//
//	/**
//	 * @param schoolGrade the schoolGrade to set
//	 */
//	public void setSchoolGrade(Set<SchoolGrade> schoolGrade) {
//		this.schoolGrade = schoolGrade;
//	}

}
