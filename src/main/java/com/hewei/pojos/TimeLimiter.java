package com.hewei.pojos;

import org.joda.time.DateTime;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/16  17:01
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class TimeLimiter {

    private DateTime upTime;

    private DateTime downTime;

    public TimeLimiter(DateTime downTime, DateTime upTime) {
        this.upTime = upTime;
        this.downTime = downTime;
    }

    public DateTime getUpTime() {
        return upTime;
    }

    public DateTime getDownTime() {
        return downTime;
    }

    public long getDownTimeMillis() {
        return downTime.getMillis();
    }

    public long getUpTimeMillis() {
        return upTime.getMillis();
    }
}
