package com.trekiz.admin.modules.rebatesupplier.service;

/**
 * 可以被返佣的对象
 * @author yanzhenxing
 * @date 2016/1/7
 */
public interface Rebatedable {

    /**
     * 被返佣对象类型：渠道
     */
    Integer REBATEDABLE_TYPE_AGENT=1;

    /**
     * 被返佣对象类型：供应商
     */
    Integer REBATEDABLE_TYPE_SUPPLIER=2;

    /**
     * 获取被返佣对象的类型
     * @return
     */
    Integer getRebatedableType();
}
