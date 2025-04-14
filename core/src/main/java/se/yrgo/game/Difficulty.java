package se.yrgo.game;

public class Difficulty {
    public enum Diff {
        EASY,
        MEDIUM,
        HARD
    }

    private Diff difficulty;
    private int speed;
    private int gap;

    public Difficulty(Diff difficulty) {
        this.difficulty = difficulty;
        setParams();
    }

    private void setParams() {
        switch(difficulty) {
            case EASY: {
                speed = -3;
                gap = 300;
                break;
            }
            case MEDIUM: {
                speed = -5;
                gap = 250;
                break;
            }
            case HARD: {
                speed = -7;
                gap = 200;
                break;
            }
            default: {
                speed = 5;
                gap = 250;
            }
            return;
        }
    }

    public Diff getDifficulty() {
        return difficulty;
    }

    public int getSpeed() {
        return speed;
    }

    public int getGap() {
        return gap;
    }

    @Override
    public String toString() {
        return difficulty.name();
    }
}
