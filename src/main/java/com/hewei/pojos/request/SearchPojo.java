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

    private int specificDay = -1;

    private long startTime = -1;

    private long endTime = -1;

    private String appName;

    private String appVersion;

    private String phase;

    private String env;

    private String className;

    private String methodName;

    private int lineNum = -1;

    private String threadName;

    private int page;

    private int pageSize;

    private String prefixType;

    private String urlType;

    private String containerID;

    public String getSearch() {
        return search;
    }

    public int getSpecificDay() {
        return specificDay;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getPhase() {
        return phase;
    }

    public String getEnv() {
        return env;
    }

    public String getClassName() {
        return className;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getLineNum() {
        return lineNum;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public void setSpecificDay(int specificDay) {
        this.specificDay = specificDay;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
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

    public String getPrefixType() {
        return prefixType;
    }

    public void setPrefixType(String prefixType) {
        this.prefixType = prefixType;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public static void main(String[] args) {
        SearchPojo logMessage = new SearchPojo();
        logMessage.setAppName("应用名称");
        logMessage.setAppVersion("应用版本");
        logMessage.setPhase("应用phase");
        logMessage.setEnv("应用env");
        logMessage.setClassName("日志打印类名");
        logMessage.setMethodName("日志打印方法名");
        logMessage.setLineNum(10);
        logMessage.setThreadName("日志打印线程名");
        logMessage.setStartTime(1L);
        logMessage.setEndTime(2L);
        logMessage.setSpecificDay(1);
        logMessage.setSearch("搜索字符");
        System.out.println(JsonUtils.toJson(logMessage));
    }


}
