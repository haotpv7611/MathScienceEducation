package com.example.demo.dtos;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class IdAndStatusDTO {
	private long id;
	@NotNull(message = "Status must be not null!")
	@Length(max = 10, message = "Status length must be less than 10!")
	private String status;

	public IdAndStatusDTO() {
	}

	public IdAndStatusDTO(long id, String status) {
		super();
		this.id = id;
		this.status = status;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

}
