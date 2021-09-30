package com.jni.cxy;

public class LaserMessage {

    public interface ScanListener {
        public void onScanListener(LaserMessage msg) throws Exception;
    }

    private Head head;
    private float[] ranges = new float[360];

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public float[] getRanges() {
        return ranges;
    }

    public void setRanges(float[] ranges) {
        this.ranges = ranges;
    }

    class Head {
        private int seq;
        private float[] pose = new float[3];

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public float[] getPose() {
            return pose;
        }

        public void setPose(float[] pose) {
            this.pose = pose;
        }
    }
}
