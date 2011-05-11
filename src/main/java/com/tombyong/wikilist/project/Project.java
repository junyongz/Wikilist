package com.tombyong.wikilist.project;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Project implements java.io.Serializable {

	private static final long serialVersionUID = 8556588477786465792L;

	private String id;

	private String name;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;

	private Set<Item> items;

	public void addItem(Item... item) {
		if (this.items == null) {
			this.items = new HashSet<Item>();
		}
		this.items.addAll(Arrays.asList(item));
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public String getId() {
		return id;
	}

	public Set<Item> getItems() {
		return items;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public String getName() {
		return name;
	}

	public void removeItem(Item item) {
		this.items.remove(item);
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Project [name=");
		builder.append(name);
		builder.append(", createdBy=");
		builder.append(createdBy);
		builder.append(", items=");
		builder.append(items);
		builder.append("]");
		return builder.toString();
	}

}
