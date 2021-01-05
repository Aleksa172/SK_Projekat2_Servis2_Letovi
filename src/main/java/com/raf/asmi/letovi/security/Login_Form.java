package com.raf.asmi.letovi.security;

public class Login_Form {
	private String email;
	private String password;

	public Login_Form() {
	}

	public Login_Form(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
