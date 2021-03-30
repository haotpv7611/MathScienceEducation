package com.example.demo.dtos;

import java.util.List;

import javax.validation.constraints.NotNull;

public class ListIdAndStatusDTO {
	List<Long> ids;
	@NotNull(message = "Status must be not null!")
	String status;

	/**
	 * @return the ids
	 */
	public List<Long> getIds() {
		return ids;
	}

	/**
	 * @param ids the ids to set
	 */
	public void setIds(List<Long> ids) {
		this.ids = ids;
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
