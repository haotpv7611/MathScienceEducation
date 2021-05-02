package com.example.demo.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Grade {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int gradeName;

	@OneToMany(mappedBy = "grade")
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "grade")
	private Set<SchoolGrade> schoolGrade;

	public int getGradeName() {
		return gradeName;
	}

	public void setGradeName(int gradeName) {
		this.gradeName = gradeName;
	}

	public int getId() {
		return id;
	}

}
