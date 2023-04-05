package com.javaexpress.user.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="USER_TB")
public class User {
	@Id
	private int id;
	private String userName;
	
	public User() {
		
	}
	
	
	public User(int id, String userName) {
		super();
		this.id = id;
		this.userName = userName;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}

