package com.dy.common;

/**
 * @Author: dy
 * @Date: 2024/4/26 19:55
 * @Description:
 */
public enum ChartStatus {

    WAIT("wait"),
    RUNNING("running"),
    SUCCEED("succeed"),
    FAILED("failed");

    private final String status;

    ChartStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
