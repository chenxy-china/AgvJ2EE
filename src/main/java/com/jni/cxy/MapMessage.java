package com.jni.cxy;

import java.util.ArrayList;

public class MapMessage {
    private Head head;
    private ArrayList<Integer> datas;

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public ArrayList<Integer> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<Integer> datas) {
        this.datas = datas;
    }

    class Head {
        private int seq;
        private float resolution;
        private int width;
        private int height;
        private float[] origin = new float[3];

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }
        
        public float getResolution() {
            return resolution;
        }

        public void setResolution(float resolution) {
            this.resolution = resolution;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public float[] getOrigin() {
            return origin;
        }

        public void setOrigin(float[] origin) {
            this.origin = origin;
        }
    }
}
