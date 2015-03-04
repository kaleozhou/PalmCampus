

package com.org.palmcampus.email.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @类名 Email
 * @作者 Rolant
 * @版本 1.0
 */
public class Email implements Serializable {

    private static final long serialVersionUID = 1L;
    private int emailID;
    private String from;
    private String to;
    private String cc;
    private String bcc;
    private String title;
    //是否是新邮件，0代表已读，1代表未读
    private int isnew;
    public int getIsnew() {
		return isnew;
	}

	public void setIsnew(int isnew) {
		this.isnew = isnew;
	}

	//发送日期
    private String sentdate;
   

	public int getEmailID() {
		return emailID;
	}

	public void setEmailID(int emailID) {
		this.emailID = emailID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSentdate() {
		return sentdate;
	}

	public void setSentdate(String sentdate) {
		this.sentdate = sentdate;
	}

	private String content;
    private boolean replysign;
    private boolean html=true;
    private boolean news;
    //附件列表的名字
    private String attachments;
    private String charset;

   

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

  

    

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isReplysign() {
        return replysign;
    }

    public void setReplysign(boolean replysign) {
        this.replysign = replysign;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public boolean isNews() {
        return news;
    }

    public void setNews(boolean news) {
        this.news = news;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

	public String getAttachments() {
		return attachments;
	}

	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}

    

}
