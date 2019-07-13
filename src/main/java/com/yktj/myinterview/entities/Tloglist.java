package com.yktj.myinterview.entities;

public class Tloglist {
    private String key;
    private String recordcount;
    private String filepath;

    public Tloglist() {
    }

    public Tloglist(String key, String recordcount, String filepath) {
        super();
        this.key = key;
        this.recordcount = recordcount;
        this.filepath = filepath;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRecordcount() {
        return recordcount;
    }

    public void setRecordcount(String recordcount) {
        this.recordcount = recordcount;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
