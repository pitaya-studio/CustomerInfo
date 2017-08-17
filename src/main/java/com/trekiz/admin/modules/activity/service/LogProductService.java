package com.trekiz.admin.modules.activity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.modules.activity.repository.LogProductDao;

/**
 * Created by quauq on 2016/9/9.
 */
@Service
@Transactional(readOnly = true)
public class LogProductService implements ILogProductService {

    @Autowired
    private LogProductDao logProductDao;

}
