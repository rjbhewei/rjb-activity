package com.hewei.pojos.request;

import com.hewei.utils.JsonUtils;

/**
 * 
 * @author hewei
 * 
 * @date 2015/8/18  15:51
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SearchPojo {

	private String search;

	private long startTime = -1;

	private long endTime = -1;

	private int page;

	private int pageSize;

	private String urlType;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}

	public static void main(String[] args) {
		SearchPojo pojo=new SearchPojo();
		pojo.setSearch("");
		pojo.setEndTime(-1);
		pojo.setStartTime(-1);
		pojo.setPage(1);
		pojo.setPageSize(10);
		pojo.setUrlType("activityDetails");
		System.out.println(JsonUtils.toJson(pojo));

	}
}
