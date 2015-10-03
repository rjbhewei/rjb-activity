package com.hewei.pojos;

import com.hewei.enums.SearchErr;
import com.hewei.pojos.request.SearchPojo;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/16  17:02
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class TimeRanger {

	private SearchErr searchErr = SearchErr.NO;

	private String[] indices;

	private long startTime = -1;

	private long endTime = -1;

	public TimeRanger(String[] indices) {
		this.indices = indices;
	}

	public TimeRanger(SearchErr searchErr, String[] indices) {
		this(indices);
		this.searchErr = searchErr;
	}

	public TimeRanger(SearchErr searchErr, String[] indices, long startTime, long endTime) {
		this(indices, startTime, endTime);
		this.searchErr = searchErr;
	}

	public TimeRanger(String[] indices, long startTime, long endTime) {
		this(indices);
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public SearchErr getSearchErr() {
		return searchErr;
	}

	public String[] getIndices() {
		return indices;
	}

	public void setIndices(String[] indices) {
		this.indices = indices;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public SearchPojo wrap(SearchPojo pojo) {
		pojo.setStartTime(startTime);
		pojo.setEndTime(endTime);
		return pojo;
	}
}
