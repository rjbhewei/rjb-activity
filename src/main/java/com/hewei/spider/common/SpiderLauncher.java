package com.hewei.spider.common;

import com.hewei.spider.common.inits.SpiderRunner;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/10  14:59
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class SpiderLauncher {
    public static void main(String[] args) {
        new Thread() {
            @Override
            public void run() {
                SpiderRunner.start();
            }
        }.start();
    }
}
