package com.tombyong.chat;

public class Message implements java.io.Serializable {

	private static final long serialVersionUID = -4950652404986629169L;

	private String id;

	private String fromUser;

	private String toUser;

	private String contents;

	private long timestamp = System.currentTimeMillis();
	
	public Message() {
	}

	public Message(String fromUser, String toUser, String contents) {
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.contents = contents;
	}
	
	public Message(String fromUser, String toUser, String contents, long timestamp) {
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.contents = contents;
		this.timestamp = timestamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromUser() {
		return fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
