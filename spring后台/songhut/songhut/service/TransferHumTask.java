package com.songhut.songhut.service;

import com.songhut.songhut.model.MelodyTask;
import feign.RequestLine;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 外部调用接口
 * @author Kun
 */
public interface TransferHumTask {
    @RequestLine("POST /getMelody/")
    Integer tranferHum(@RequestBody MelodyTask melodyTask);
}
