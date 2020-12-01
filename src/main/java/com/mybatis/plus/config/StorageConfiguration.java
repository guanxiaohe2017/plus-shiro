package com.mybatis.plus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties("conf")
@Data
public class StorageConfiguration {

    private String path;
    private String addressDemo;
    private String miscRelativePath;
    private Boolean autoTask;

}