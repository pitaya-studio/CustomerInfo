package com.trekiz.admin.modules.sys.service;

import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.sys.repository.PlatBankDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 平台银行账号信息服务类
 * @author yanzhenxing
 * @date 2016/1/7
 */
@Service
public class PlatBankService {

    @Autowired
    private PlatBankDao platBankDao;

    /**
     * 根据平台id和类型获取银行账户信息列表
     * @param platId
     * @param platType
     * @return
     */
    public List<PlatBankInfo> obtainBanks(Long platId,Integer platType){
        Assert.notNull(platId,"platId should not be null!");
        Assert.notNull(platType,"platType should not be null!");
        return platBankDao.findByBeLongPlatIdAndPlatType(platId,platType);
    }

    public List<String> obtainBankInfos(Long platId,Integer platType){
        Assert.notNull(platId,"platId should not be null!");
        Assert.notNull(platType,"platType should not be null!");
        return platBankDao.getPlatBankInfo(platId,platType);
    }

    /**
     * 批量保存银行账号信息
     * @param platId
     * @param platType
     * @param belongTypes
     * @param defaultFlags
     * @param accountNames
     * @param bankNames
     * @param bankAddrs
     * @param bankAccountCodes
     * @param rountings
     * @param swiftNums
     * @param phoneNums
     * @param remarks
     */
    @Transactional
    public void batchSave(Long platId,Integer platType,String[] belongTypes,String[] defaultFlags,String[] accountNames,
                          String[] bankNames,String[] bankAddrs,String[] bankAccountCodes,String[] rountings,String[] swiftNums,
                          String[] phoneNums,String[] remarks){
        if (platId!=null) {
            // 获取银行账户
            List<String> banks = platBankDao.getPlatBankInfo(platId,platType);
            if (null != banks && banks.size() > 0) {
                // 删除现有银行账户
                platBankDao.deletePlatBankInfo(platId,platType);
            }

            for (int i = 0, m = 0; i < belongTypes.length; i++) {
                // 境内账户
                if ("1".equals(belongTypes[i])) {
                    if (StringUtils.isNotBlank(accountNames[i]) && StringUtils.isNotBlank(bankNames[i])
                            && StringUtils.isNotBlank(bankAddrs[i]) && StringUtils.isNotBlank(bankAccountCodes[i])) {
                        platBankDao.insertPlatBankInfo(defaultFlags[i], accountNames[i], bankNames[i],
                                bankAddrs[i], bankAccountCodes[i], remarks[i],platType, platId, belongTypes[i]);
                    }
                }
                // 境外账户
                if ("2".equals(belongTypes[i])) {
                    if (StringUtils.isNotBlank(accountNames[i]) && StringUtils.isNotBlank(bankNames[i])
                            && StringUtils.isNotBlank(bankAddrs[i]) && StringUtils.isNotBlank(bankAccountCodes[i])) {
                        platBankDao.insertPlatBankInfo(defaultFlags[i], accountNames[i], bankNames[i],
                                bankAddrs[i], bankAccountCodes[i], remarks[i],platType, platId, rountings[m],
                                swiftNums[m], phoneNums[m], belongTypes[i]);
                        m++;
                    }
                }

            }
        }
    }

    /**
     * 根据平台id和类型删除银行账号信息
     * @param platId
     * @param platType
     */
    @Transactional
    public void delete(Long platId,Integer platType){
        Assert.notNull(platId,"platId should not be null!");
        Assert.notNull(platType,"platType should not be null!");

        platBankDao.deletePlatBankInfo(platId,platType);
    }
}
