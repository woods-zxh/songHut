package com.songhut.songhut.util;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

public class RestTemplateUtil {


    /**
     * 创建指定字符集的RestTemplate
     *
     * @param charset
     * @return
     */
    public static RestTemplate getInstance(String charset) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName(charset)));
        return restTemplate;
    }

}

