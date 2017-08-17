package com.trekiz.admin.review.configuration.extend;

import org.activiti.engine.impl.cfg.IdGenerator;

import java.util.UUID;

/**
 * @author yanzhenxing
 * @date 2015/12/31
 */
public class UuidGenerator implements IdGenerator {
    @Override
    public String getNextId() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }
}
