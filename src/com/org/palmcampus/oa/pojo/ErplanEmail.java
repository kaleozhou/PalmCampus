package com.org.palmcampus.oa.pojo;

import java.sql.Timestamp;

/**
 * ErplanEmail entity. @author MyEclipse Persistence Tools
 */

public class ErplanEmail implements java.io.Serializable {

	// Fields

	private Integer id;
	private String emailTitle;
	private Timestamp timeStr;
	private String emailContent;
	private String fuJian;
	private String fromUser;
	private String toUser;
	private String emailState;
	private String cc;
	private String bcc;
	private String receipt;
	private String sendToList;

	// Constructors

	/** default constructor */
	public ErplanEmail() {
	}

	/** full constructor */
	public ErplanEmail(String emailTitle, Timestamp timeStr,
			String emailContent, String fuJian, String fromUser, String toUser,
			String emailState, String cc, String bcc, String receipt,
			String sendToList) {
		this.emailTitle = emailTitle;
		this.timeStr = timeStr;
		this.emailContent = emailContent;
		this.fuJian = fuJian;
		this.fromUser = fromUser;
		this.toUser = toUser;
		this.emailState = emailState;
		this.cc = cc;
		this.bcc = bcc;
		this.receipt = receipt;
		this.sendToList = sendToList;
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

	public String getCc() {
		return this.cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return this.bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getReceipt() {
		return this.receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getSendToList() {
		return this.sendToList;
	}

	public void setSendToList(String sendToList) {
		this.sendToList = sendToList;
	}

}