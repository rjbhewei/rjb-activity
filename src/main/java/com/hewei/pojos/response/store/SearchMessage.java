package com.hewei.pojos.response.store;

/**
 *
 * @author hewei
 *
 * @date 2015/9/8  21:30
 *
 * @version 5.0
 *
 * @desc
 *
 */
public class SearchMessage {

	private String title;

	private String time;//时间

	private String location;//地址

	private String expense;//费用

	private String type;//类型

	private String initiator;//发起人

	private String initiatorUrl;//发起人

	private String desc;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getExpense() {
		return expense;
	}

	public void setExpense(String expense) {
		this.expense = expense;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public String getInitiatorUrl() {
		return initiatorUrl;
	}

	public void setInitiatorUrl(String initiatorUrl) {
		this.initiatorUrl = initiatorUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
