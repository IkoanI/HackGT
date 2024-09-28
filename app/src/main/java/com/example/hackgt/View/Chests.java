package com.example.hackgt.View;
public class Chests {
    private int startCount;
    private int endCount;
    private int reward;

    public Chests(int encounterStep) {
        this.startCount = encounterStep;
        this.endCount = startCount+500;
        this.reward = (int) (Math.random() * 99 + 1);
    }

    public int getReward() {
        return reward;
    }

    public int getEndCount() {
        return endCount;
    }
}
