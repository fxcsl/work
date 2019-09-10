package com.sinovatio.iesi.model.entity;

public class NowStateBean {


    /**
     * isLeader : 0
     * workingState : 任务结束
     */

    private int isLeader;
    private String workingState;

    public int getIsLeader() {
        return isLeader;
    }

    public void setIsLeader(int isLeader) {
        this.isLeader = isLeader;
    }

    public String getWorkingState() {
        return workingState;
    }

    public void setWorkingState(String workingState) {
        this.workingState = workingState;
    }
}
