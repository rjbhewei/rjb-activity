package com.hewei.pojos.response.store;

import java.util.Date;

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

    private String appName;

    private String appVersion;

    private String phase;

    private String env;

    private String containerID;

    private String logPath;

    private String className;

    private String methodName;

    private int lineNum = -1;

    private String threadName;

    private Date storeLogTime;

    private String message;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public String getContainerID() {
        return containerID;
    }

    public void setContainerID(String containerID) {
        this.containerID = containerID;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public int getLineNum() {
        return lineNum;
    }

    public void setLineNum(int lineNum) {
        this.lineNum = lineNum;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Date getStoreLogTime() {
        return storeLogTime;
    }

    public void setStoreLogTime(Date storeLogTime) {
        this.storeLogTime = storeLogTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
