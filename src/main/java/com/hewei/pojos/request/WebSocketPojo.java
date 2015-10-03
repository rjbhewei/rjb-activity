package com.hewei.pojos.request;
/**
 * 
 * @author hewei
 * 
 * @date 2015/9/21  16:06
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class WebSocketPojo {

    private String appName;

    private String appVersion;

    private String phase;

    private String env;

    private String containerID;

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
}
