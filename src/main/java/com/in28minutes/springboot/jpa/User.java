package com.in28minutes.springboot.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity //it specifies that class is an entity i.t. in the form of tabble
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String name;
	
	@Column(name="Role")
	private String role;
	public Long getId() {
		return id;
	}
	
	protected User() {
	}


	public User( String name,String role) {
		super();
		this.role = role;
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return String.format("User [id=%s, role=%s, name=%s]", id, role, name);
	}
	
	
	
}
