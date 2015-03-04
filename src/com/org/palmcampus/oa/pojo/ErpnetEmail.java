package com.org.palmcampus.oa.pojo;

import java.sql.Timestamp;

/**
 * ErpnetEmail entity. @author MyEclipse Persistence Tools
 */

public class ErpnetEmail implements java.io.Serializable {

	// Fields

	private Integer id;
	private String emailTitle;
	private Timestamp timeStr;
	private String emailContent;
	private String fuJian;
	private String fromUser;
	private String toUser;
	private String emailState;

	// Constructors

	/** default constructor */
	public ErpnetEmail() {
	}

	/** full constructor */
	public ErpnetEmail(String emailTitle, Timestamp timeStr,
			String emailContent, String fuJian, String fromUser, String toUser,
			String emailState) {
		this.emailTitle = emailTitle;
		this.timeStr = timeStr;
		this.emailContent = emailContent;
		this.fuJian = fuJian;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.emailState = emailState;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmailTitle() {
		return this.emailTitle;
	}

	public void setEmailTitle(String emailTitle) {
		this.emailTitle = emailTitle;
	}

	public Timestamp getTimeStr() {
		return this.timeStr;
	}

	public void setTimeStr(Timestamp timeStr) {
		this.timeStr = timeStr;
	}

	public String getEmailContent() {
		return this.emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public String getFuJian() {
		return this.fuJian;
	}

	public void setFuJian(String fuJian) {
		this.fuJian = fuJian;
	}

	public String getFromUser() {
		return this.fromUser;
	}

	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}

	public String getToUser() {
		return this.toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getEmailState() {
		return this.emailState;
	}

	public void setEmailState(String emailState) {
		this.emailState = emailState;
	}

}