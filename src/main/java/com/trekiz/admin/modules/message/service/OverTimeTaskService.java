package com.trekiz.admin.modules.message.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.quauq.multi.tenant.datasource.DataSourceContainer;
import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.task.ScheduledTask;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.modules.message.entity.MsgAnnouncement;
import com.trekiz.admin.modules.message.repository.MsgAnnouncementDao;

/**
 * 过期公告/消息 定时清理
 * @author gao
 *  2015年3月11日
 */
@Service("overTimeTaskService")
//@Component("overTimeTaskService") 
public class OverTimeTaskService extends ScheduledTask{
	@Autowired
	public  MsgAnnouncementDao msgAnnouncementDao;
	
	public void overTimeTask(){
//		System.out.println("启动执行！"+new Date());
		List<MsgAnnouncement> list = msgAnnouncementDao.findByStatus(Context.MESSAGE_STATUS_ISSUE, new Date());
		if(list==null || list.isEmpty()){
			return;
		}
		Connection con = null;
		try{
			String driver = Global.getConfig("jdbc.driver");
			String username = Global.getConfig("jdbc.username");
			String password = Global.getConfig("jdbc.password");
			String url = Global.getConfig("jdbc.url");
			Class.forName(driver).newInstance();
			con = DriverManager.getConnection(url,username,password);
			Iterator<MsgAnnouncement> iter = list.iterator();
			while(iter.hasNext()){
				MsgAnnouncement msg = iter.next();
//				System.out.println("title:"+msg.getTitle());
//				System.out.println("status:"+msg.getStatus());
//				System.out.println("msgType:"+msg.getMsgType());
//				System.out.println("overTime:"+msg.getOverTime());
//				msgAnnouncementDao.updateMsgAnnouncement(Context.MESSAGE_STATUS_PAST, msg.getId());
				PreparedStatement pre = con.prepareStatement("update msg_announcement set status=? where id=?");
				pre.setInt(1, Context.MESSAGE_STATUS_PAST);
				pre.setLong(2, msg.getId());
				pre.executeUpdate();
			}
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	protected void task() {
//		System.out.println("启动执行！"+new Date());
//		System.out.println("overTimeTaskService["+FacesContext.getCurrentTenant()+"]");
		List<MsgAnnouncement> list = msgAnnouncementDao.findByStatus(Context.MESSAGE_STATUS_ISSUE, new Date());
		if(list==null || list.isEmpty()){
			return;
		}
		Connection con = null;
		try{
			DataSource dataSource = DataSourceContainer.getDataSource(FacesContext.getCurrentTenant());
			con= dataSource.getConnection();
			Iterator<MsgAnnouncement> iter = list.iterator();
			while(iter.hasNext()){
				MsgAnnouncement msg = iter.next();
//				System.out.println("title:"+msg.getTitle());
//				System.out.println("status:"+msg.getStatus());
//				System.out.println("msgType:"+msg.getMsgType());
//				System.out.println("overTime:"+msg.getOverTime());
//				msgAnnouncementDao.updateMsgAnnouncement(Context.MESSAGE_STATUS_PAST, msg.getId());
				PreparedStatement pre = con.prepareStatement("update msg_announcement set status=? where id=?");
				pre.setInt(1, Context.MESSAGE_STATUS_PAST);
				pre.setLong(2, msg.getId());
				pre.executeUpdate();
				pre.close();
			}
			con.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
