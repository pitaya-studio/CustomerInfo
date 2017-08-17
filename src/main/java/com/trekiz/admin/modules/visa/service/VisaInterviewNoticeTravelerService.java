package com.trekiz.admin.modules.visa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.visa.entity.VisaInterviewNoticeTraveler;
import com.trekiz.admin.modules.visa.repository.VisaInterviewNoticeTravelerDao;


@Service
@Transactional(readOnly = true)
public class VisaInterviewNoticeTravelerService  extends BaseService {
    
    @Autowired
    private VisaInterviewNoticeTravelerDao visaInterviewNoticeTravelerDao;
    
    public int delete(Long interviewId){
    	return visaInterviewNoticeTravelerDao.deleteBySubId(interviewId);
    }
    
    public int add(List<VisaInterviewNoticeTraveler> travelers){
    	visaInterviewNoticeTravelerDao.save(travelers);
    	return travelers.size();
    }
    
}
