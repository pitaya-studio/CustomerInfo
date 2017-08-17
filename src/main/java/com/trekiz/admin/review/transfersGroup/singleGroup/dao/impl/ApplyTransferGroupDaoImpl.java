package com.trekiz.admin.review.transfersGroup.singleGroup.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.review.transfersGroup.singleGroup.dao.ApplyTransferGroupDao;
import com.trekiz.admin.review.transfersGroup.singleGroup.form.TransferForm;

@Repository
public class ApplyTransferGroupDaoImpl extends BaseDaoImpl<Map<String, Object>> 
implements ApplyTransferGroupDao{

	@Override
	public List<Map<String,Object>> getReviewNew(TransferForm form){
		StringBuffer sql = new StringBuffer();
		sql.append("Select ");
		sql.append(" rn.id as uuid,");// review_new UUID
		sql.append(" rn.batch_no as batch_no,"); // 批次号
		sql.append(" rn.proc_inst_id as proc_inst_id, ");// 流程实例id
		sql.append(" rn.process_key as process_key, "); // 流程key
		sql.append(" rn.process_version as process_version, "); // 流程版本
		sql.append(" rn.current_task_id as current_task_id, "); // 当前审批环节id
		sql.append(" rn.current_activity_id as current_activity_id, "); // 当前环节id
		sql.append(" rn.dept_id as dept_id, "); // 部门ID
		sql.append(" rn.process_type as process_type, "); // 流程类型
		sql.append(" rn.product_type as product_type, "); // 产品类型
		sql.append(" rn.business_key as business_key, "); // 业务key，具有业务规则的key，可以唯一定位一条审批记录
		sql.append(" rn.agent as agent, "); // 渠道，
		sql.append(" rn.saler as saler, "); // 销售，
		sql.append(" rn.saler_name as saler_name, "); // 销售姓名
		sql.append(" rn.operator as operator, "); // 计调，
		sql.append(" rn.operator_name as operator_name, "); // 计调姓名
		sql.append(" rn.order_creator as order_creator, "); // 下单人，多值时逗号分隔
		sql.append(" rn.order_creator_name as order_creator_name, "); // 下单人姓名
		sql.append(" rn.group_id as group_id, "); // 团期id
		sql.append(" rn.group_code as group_code, "); // 团号，
		sql.append(" rn.order_no as order_no, "); // 订单编号，
		sql.append(" rn.product_id as product_id, "); // 产品id，
		sql.append(" rn.product_uuid as product_uuid, "); // 产品uuid
		sql.append(" rn.product_name as product_name, "); // 产品名称
		sql.append(" rn.order_id as order_id, "); // 订单id
		sql.append(" rn.cost_record_uuid as cost_record_uuid, "); // 成本记录uuid
		sql.append(" rn.traveller_id as traveller_id, "); // 游客id
		sql.append(" rn.traveller_name as traveller_name, "); // 游客姓名
		sql.append(" rn.deny_reason as deny_reason, "); // 驳回理由
		sql.append(" rn.last_reviewer as last_reviewer, "); // 上一环节审批人，
		sql.append(" rn.current_reviewer as current_reviewer, "); // 当前环节审批人，
		sql.append(" rn.all_reviewer as all_reviewer, "); // 所有环节审批人
		sql.append(" rn.participate_group as participate_group, "); // 流程参与的组
		sql.append(" rn.participate_detail as participate_detail, "); // 参与者详细信息，json格式字符串，记录所有环节参与者
		sql.append(" rn.status as status, "); // 审批状态
		sql.append(" rn.is_current_countersign as is_current_countersign, "); // 当前环节是否是会签环节
		sql.append(" rn.pay_status as pay_status, "); // 付款状态
		sql.append(" rn.pay_confirm_date as pay_confirm_date, "); // 付款确认时间
		sql.append(" rn.print_status as print_status, "); // 打印状态
		sql.append(" rn.need_no_review_flag as need_no_review_flag, "); //无需审核标识，0：需要审核，1：无需审核
		sql.append(" rn.print_date as print_date, "); // 首次打印时间
		sql.append(" rn.critical_level as critical_level, "); // 重要程度
		sql.append(" rn.skipped_assignee as skipped_assignee, "); // 跳过的办理人
		sql.append(" rn.remark as remark, "); // 备注
		sql.append(" rn.company_id as company_id,"); // 公司id
		sql.append(" rn.create_by as create_by, "); // 创建者(审核发起者)
		sql.append(" rn.create_date as create_date, "); // 创建时间
		sql.append(" rn.update_by as update_by, "); // 更新者
		sql.append(" rn.update_date as update_date, "); // 更新时间
		sql.append(" rn.del_flag as del_flag, ");
		sql.append(" rn.extend_1 as extend_1, "); // 扩展字段1
		sql.append(" rn.extend_2 as extend_2 "); // 扩展字段2
		sql.append(" From ");
		sql.append(" review_new rn");
		sql.append(" where 1=1 ");
		if(StringUtils.isNoneBlank(form.getSelectInfo())){
			sql.append(" and (");
			sql.append(" rn.group_code like %"+form.getSelectInfo()+"%"); // 团号
			sql.append(" or rn.product_name like %"+form.getSelectInfo()+"%"); // 产品名称
			sql.append(" or rn.order_no like %"+form.getSelectInfo()+"%) "); // 订单no
		}
		if(StringUtils.isNoneBlank(form.getProductType())){ // 产品类型
			sql.append(" and rn.product_type = "+form.getProductType());
		}
		if(StringUtils.isNoneBlank(form.getAgnetID())){ // 渠道商
			sql.append(" and rn.agent = "+form.getAgnetID());
		}
		if(StringUtils.isNoneBlank(form.getTransferUserID())){ // 审批发起人
			sql.append(" and rn.create_by = "+form.getTransferUserID());
		}
		if(StringUtils.isNoneBlank(form.getTravelerID())){ // 游客
			sql.append(" and rn.traveller_id = "+form.getTravelerID());
		}
		if(form.getTransferStatus()!=null){ // 审批状态
			sql.append(" and rn.status = "+form.getTransferStatus());
		}
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		list = findBySql(sql.toString(),Map.class);
		
		return list;
	}
}
