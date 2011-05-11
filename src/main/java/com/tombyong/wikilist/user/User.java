package com.tombyong.wikilist.user;

public class User implements java.io.Serializable {

	private static final long serialVersionUID = 514403739634532762L;

	private String username;

	private String firstName;

	private String lastName;

	private String email;

	private transient String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("User [username=");
		buffer.append(username);
		buffer.append(", firstName=");
		buffer.append(firstName);
		buffer.append(", lastName=");
		buffer.append(lastName);
		buffer.append(", email=");
		buffer.append(email);
		buffer.append("]");
		return buffer.toString();
	}

}
