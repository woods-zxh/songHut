package com.songhut.songhut.model;


/**
 * 哼唱任务模型
 * @author Kun
 */
public class MelodyTask {
    private String filePath;
    private String token;
    private Integer rid;
    private Integer fid;
    private Integer type;


    public MelodyTask(String filePath, String token, Integer rid, Integer fid, Integer type) {
        this.filePath = filePath;
        this.token = token;
        this.rid = rid;
        this.fid = fid;
        this.type = type;
    }
}
