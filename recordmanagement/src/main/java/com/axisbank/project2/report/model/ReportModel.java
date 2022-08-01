package com.axisbank.project2.report.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ReportModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String username;
	private String email;
	private String downloadedItems;
	private String timestamp;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDownloadedItems() {
		return downloadedItems;
	}
	public void setDownloadedItems(String downloadedItems) {
		this.downloadedItems = downloadedItems;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public ReportModel(String username, String email, String downloadedItems, String timestamp) {
		super();
		this.username = username;
		this.email = email;
		this.downloadedItems = downloadedItems;
		this.timestamp = timestamp;
	}
	public ReportModel() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ReportModel(int id, String username, String email, String downloadedItems, String timestamp) {
		super();
		this.id = id;
		this.username = username;
		this.email = email;
		this.downloadedItems = downloadedItems;
		this.timestamp = timestamp;
	}
	

}
