package com.sky.utils;

import org.springframework.lang.NonNull;
import org.springframework.util.StopWatch;

/**
 * @Author:
 * @Date: 2025/4/3 13:44
 * @Description:
 */

public class StopWatchSupport extends StopWatch {

    public StopWatchSupport(String id) {
        super(id);
    }

    @Override
    public void start() throws IllegalStateException {
        if (this.isRunning()) {
            this.stop();
        }
        super.start();
    }

    @Override
    public void start(@NonNull String taskName) throws IllegalStateException {
        if (this.isRunning()) {
            this.stop();
        }
        super.start(taskName);
    }

    @Override
    public void stop() throws IllegalStateException {
        if (this.isRunning()) {
            super.stop();
        }
    }
}
