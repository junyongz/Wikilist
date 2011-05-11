package com.tombyong.wikilist.project;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

public class Item implements java.io.Serializable, Comparable<Item> {

	private static final long serialVersionUID = -8265863943384083148L;

	private String id;

	private String name;

	private Integer count;

	private String url;

	private Date dueDate;

	private Boolean done;

	private String location;

	private String belongToProjectId;

	private Set<String> usernames;

	public String getBelongToProjectId() {
		return belongToProjectId;
	}

	public Integer getCount() {
		return count;
	}

	public Boolean getDone() {
		return done;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getId() {
		return id;
	}

	public String getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public Set<String> getUsernames() {
		return usernames;
	}

	public void setBelongToProjectId(String belongToProjectId) {
		this.belongToProjectId = belongToProjectId;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUsernames(Set<String> usernames) {
		this.usernames = usernames;
	}

	public void addUsername(String username) {
		if (this.usernames == null) {
			this.usernames = new HashSet<String>();
		}
		this.usernames.add(username);
	}

	public void removeUsername(String username) {
		this.usernames.remove(username);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Item [name=");
		builder.append(name);
		builder.append(", count=");
		builder.append(count);
		builder.append(", url=");
		builder.append(url);
		builder.append(", dueDate=");
		builder.append(dueDate);
		builder.append(", done=");
		builder.append(done);
		builder.append(", location=");
		builder.append(location);
		builder.append(", usernames=");
		builder.append(usernames);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Item that) {
		return this.name.compareTo(that.name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		}
		else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

}
