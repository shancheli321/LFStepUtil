package com.lf.steputil.mi;

/**
 * @date: 2024/7/12
 */
public class XMStepEntity {

    private int id; // 记录在sqlite的id
    private long mBeginTime; // 计步开始时间
    private long mEndTime; // 计步结束时间
    private int mMode; // 计步模式: 0:不支持模式, 1:静止, 2:走路, 3:跑步, 11:骑车, 12:交通工具
    private int mSteps; // 总步数

    public XMStepEntity(int id, long mBeginTime, long mEndTime, int mMode, int mSteps) {
        this.id = id;
        this.mBeginTime = mBeginTime;
        this.mEndTime = mEndTime;
        this.mMode = mMode;
        this.mSteps = mSteps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getmBeginTime() {
        return mBeginTime;
    }

    public void setmBeginTime(long mBeginTime) {
        this.mBeginTime = mBeginTime;
    }

    public long getmEndTime() {
        return mEndTime;
    }

    public void setmEndTime(long mEndTime) {
        this.mEndTime = mEndTime;
    }

    public int getmMode() {
        return mMode;
    }

    public void setmMode(int mMode) {
        this.mMode = mMode;
    }

    public int getmSteps() {
        return mSteps;
    }

    public void setmSteps(int mSteps) {
        this.mSteps = mSteps;
    }


    @Override
    public String toString() {
        return "XMStepEntity{" +
                "id=" + id +
                ", mBeginTime=" + mBeginTime +
                ", mEndTime=" + mEndTime +
                ", mMode=" + mMode +
                ", mSteps=" + mSteps +
                '}';
    }
}
