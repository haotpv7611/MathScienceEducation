package com.example.demo.dtos;

public class ProgressTestResponseDTO {
	private long id;
	private String progressTestName;
	private long unitAfterId;
	private int unitAfterName;
	private String description;
	private boolean isDone;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	 * @return the progressTestName
	 */
	public String getProgressTestName() {
		return progressTestName;
	}

	/**
	 * @param progressTestName the progressTestName to set
	 */
	public void setProgressTestName(String progressTestName) {
		this.progressTestName = progressTestName;
	}

	/**
	 * @return the unitAfterId
	 */
	public long getUnitAfterId() {
		return unitAfterId;
	}

	/**
	 * @param unitAfterId the unitAfterId to set
	 */
	public void setUnitAfterId(long unitAfterId) {
		this.unitAfterId = unitAfterId;
	}

	/**
	 * @return the unitAfterName
	 */
	public int getUnitAfterName() {
		return unitAfterName;
	}

	/**
	 * @param unitAfterName the unitAfterName to set
	 */
	public void setUnitAfterName(int unitAfterName) {
		this.unitAfterName = unitAfterName;
	}

	/**
	 * @return the isDone
	 */
	public boolean isDone() {
		return isDone;
	}

	/**
	 * @param isDone the isDone to set
	 */
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

}
