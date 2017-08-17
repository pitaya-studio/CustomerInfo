package com.trekiz.admin.modules.sys.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通用平台银行账号dao
 * @author yanzhenxing
 * @date 2016/1/7
 */
public interface PlatBankDao extends PlatBankDaoCustom,CrudRepository<PlatBankInfo,Long>{

    /**
     * 根据平台id和平台类型查询银行账户
     * @param platId
     * @param platType
     * @return
     */
    List<PlatBankInfo> findByBeLongPlatIdAndPlatType(Long platId,Integer platType);
}

/**
 * 自定义接口
 */
interface PlatBankDaoCustom extends BaseDao<PlatBankInfo>{
    /**
     * 根据供应商ID和类型查询银行账户
     */
    List<String> getPlatBankInfo(Long id,Integer type);

    /**
     * 更具批发商ID和银行名称进行过滤
     */
    List<String> getPlatBankInfo(Long id,String bankName,Integer type) ;
    /**
     * 根据供应商ID和类型查询银行账户
     */
    List<String> getPlatBankInfoNameById(Long id,Integer type);

    /**
     * 删除银行账户
     */
    boolean deletePlatBankInfo(Long id, Integer type);

    /**
     * 插入银行账户
     */
    boolean insertPlatBankInfo(String defaultFlag, String accountName, String bankName,
                                     String bankAddr, String bankAccountCode, String remarks, Integer type, Long beLongPlatId, String belongType);

    /**
     * 插入境外银行账户
     */
    boolean insertPlatBankInfo(String defaultFlag, String accountName, String bankName, String bankAddr,
                                            String bankAccountCode, String remarks, Integer type, Long beLongPlatId, String rounting, String swiftNum, String phoneNum, String belongType);
}

@Repository
class PlatBankDaoImpl extends BaseDaoImpl<PlatBankInfo> implements PlatBankDaoCustom{
    /**
     * 根据供应商ID和类型查询银行账户
     */
    public List<String> getPlatBankInfo(Long id,Integer type) {
        String deleteOfficeContactsSql = "select id, belongType, defaultFlag,accountName,bankName,bankAddr,bankAccountCode,remarks, rounting, swiftNum, phoneNum from plat_bank_info where beLongPlatId = ? and platType = ? order by id";
        List<String> platBankInfo = findBySql(deleteOfficeContactsSql, id,type);
        return platBankInfo;
    }
    /**
     * 名称不能重复，结果集只包含bankName
     */
    public List<String> getPlatBankInfoNameById(Long id,Integer type) {
        String deleteOfficeContactsSql = "select distinct bankName from plat_bank_info where beLongPlatId = ? and platType = ? order by defaultFlag";
        List<String> platBankInfo = findBySql(deleteOfficeContactsSql, id,type);
        return platBankInfo;
    }

    /**
     * 更具批发商ID和银行名称进行过滤
     */
    public List<String> getPlatBankInfo(Long id,String bankName,Integer type) {
        String deleteOfficeContactsSql = "select id,defaultFlag,accountName,bankName,bankAddr,bankAccountCode,remarks from plat_bank_info where beLongPlatId = ? and bankName=? and platType = ? order by defaultFlag";
        List<String> platBankInfo = findBySql(deleteOfficeContactsSql, new Object[]{id,bankName,type});
        return platBankInfo;
    }
    /**
     * 根据批发商ID删除银行账户
     */
    public boolean deletePlatBankInfo(Long id,Integer type) {
        String deleteOfficePlatBankInfo = "DELETE FROM plat_bank_info WHERE beLongPlatId = ? and platType = ?";
        int count = updateBySql(deleteOfficePlatBankInfo, id,type);
        return count == 0?false:true;
    }

    /**
     * 根据批发商ID插入银行账户
     */
    public boolean insertPlatBankInfo(String defaultFlag, String accountName, String bankName,
                                            String bankAddr, String bankAccountCode, String remarks,Integer type, Long beLongPlatId, String belongType) {
        String insertOfficePlatBankInfoSql = "INSERT INTO plat_bank_info (" +
                " defaultFlag," +
                " accountName," +
                " bankName," +
                " bankAddr," +
                " bankAccountCode," +
                " remarks," +
                " platType," +
                " beLongPlatId," +
                " belongType) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int count = updateBySql(insertOfficePlatBankInfoSql, defaultFlag, accountName, bankName,
                bankAddr, bankAccountCode, remarks,type, beLongPlatId, belongType);
        return count == 0?false:true;
    }
    /**
     * 根据批发商ID插入境外银行账户
     */
    public boolean insertPlatBankInfo(String defaultFlag, String accountName, String bankName, String bankAddr,
                                            String bankAccountCode, String remarks, Integer type,Long beLongPlatId, String rounting, String swiftNum, String phoneNum, String belongType){
        String insertOfficePlatBankInfoSql = "INSERT INTO plat_bank_info (" +
                " defaultFlag," +
                " accountName," +
                " bankName," +
                " bankAddr," +
                " bankAccountCode," +
                " remarks," +
                " platType," +
                " beLongPlatId," +
                " rounting, "+
                " swiftNum, " +
                " phoneNum, " +
                " belongType)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int count = updateBySql(insertOfficePlatBankInfoSql, defaultFlag, accountName, bankName,
                bankAddr, bankAccountCode, remarks,type, beLongPlatId, rounting, swiftNum, phoneNum, belongType);
        return count == 0?false:true;
    }
}
