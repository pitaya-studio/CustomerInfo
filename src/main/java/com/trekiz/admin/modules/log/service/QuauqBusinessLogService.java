package com.trekiz.admin.modules.log.service;

import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.hibernate.MyMultiTenantConnectionProviderImpl;
import com.trekiz.admin.common.utils.JDBCUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Service;

import java.sql.*;

/**
 * 系统中记录业务操作的日志(异步保存)
 * @author shijun.liu
 * @date    2016.08.09
 */
@Service
public class QuauqBusinessLogService {

    public void log(String message, String type){
        if(StringUtils.isBlank(message)){
           throw new RuntimeException("日志内容不能为空");
        }
        if(StringUtils.isBlank(type)){
            throw new RuntimeException("所属操作分类不能为空");
        }
        User user = UserUtils.getUser();
        final String companyUuid = user.getCompany().getUuid();
        Long agentId = -1L;
        if("1".equals(user.getIsQuauqAgentLoginUser())){
            agentId = user.getAgentId();
        }
        final Long finalAgentId = agentId;
        final String finalType = type;
        final String finalMessage = message;
        final Long userId = user.getId();
        final String tenantId = FacesContext.getCurrentTenant();
        final MyMultiTenantConnectionProviderImpl mtcp = new MyMultiTenantConnectionProviderImpl();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = null;
                PreparedStatement pstmt = null;
                try {
                    StringBuffer str = new StringBuffer();
                    str.append("INSERT INTO quauq_business_log (company_uuid, agent_id, type, content, ")
                            .append("create_by, create_date) VALUES (?,?,?,?,?,?)");
                    connection = mtcp.getConnection(tenantId);
                    pstmt = connection.prepareStatement(str.toString());
                    pstmt.setString(1, companyUuid);
                    pstmt.setLong(2, finalAgentId);
                    pstmt.setString(3, finalType);
                    pstmt.setString(4, finalMessage);
                    pstmt.setLong(5, userId);
                    pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }finally {
                    JDBCUtils.close(connection,pstmt, null);
                }
            }
        }).start();
    }
}
