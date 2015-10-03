package com.hewei;

import com.hewei.main.HttpLauncher;
import com.hewei.main.WebSocketLauncher;

/**
 * @author hewei
 * @version 5.0
 * @date 2015/8/18  15:39
 * @desc
 */
public class Launcher {

    public static void main(String[] args) throws InterruptedException {
        HttpLauncher.asynStart();
        WebSocketLauncher.asynStart();
    }
}
