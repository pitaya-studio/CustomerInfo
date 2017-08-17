package com.trekiz.admin.modules.distribution.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.trekiz.admin.common.config.Global;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.distribution.dao.QrCodeInfoDao;
import com.trekiz.admin.modules.distribution.dao.QrCodeParamDao;
import com.trekiz.admin.modules.distribution.entity.QrCodeInfo;
import com.trekiz.admin.modules.distribution.entity.QrCodeParam;
import com.trekiz.admin.modules.distribution.service.QrCodeService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
public class QrCodeServiceImpl implements QrCodeService {
	
	@Autowired
	private QrCodeInfoDao qrCodeInfoDao;
	@Autowired
	private QrCodeParamDao qrCodeParamDao;
	
	
	@Override
	public String generateQrCodeInfo4Batch(List<Map<String, Object>> list, boolean isMulti) {
		// 数据准备（uuid,url,userId,companyId）
		String uuid = UUID.randomUUID().toString().replace("-", "");
		String url = "";
		if (isMulti) {/** 多产品生成二维码请求路径  */

//			url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
//					"appid="+Context.WEIXIN_APP_ID+"&redirect_uri="+Context.QUAUQ_WEIXIN_INDEX_URL+uuid+
//					"&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			//将php版本修改成java版本的redirect_uri，需要进行URLEncode转换



			/*url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
					"appid="+Context.WEIXIN_APP_ID+"&redirect_uri="+Context.QUAUQ_WEIXIN_INDEX_URL_JAVA + uuid +
					"&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect";
*/
			url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
					"appid="+ Global.getConfig("WEIXIN_APP_ID")+"&redirect_uri="+Global.getConfig("QUAUQ_WEIXIN_INDEX_URL_JAVA") + uuid +
					"&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect";

		} else {/** 单产品生成二维码请求路径  */
//			url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
//					"appid="+Context.WEIXIN_APP_ID+"&redirect_uri="+Context.QUAUQ_WEIXIN_DETAIL_URL+ uuid  +
//					"&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
			//将php版本修改成java版本的redirect_uri，需要进行URLEncode转换
			/*url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
					"appid="+Context.WEIXIN_APP_ID+"&redirect_uri="+Context.QUAUQ_WEIXIN_DETAIL_URL_JAVA + uuid +
					"&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect";*/
			url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
					"appid="+Global.getConfig("WEIXIN_APP_ID")+"&redirect_uri="+Global.getConfig("QUAUQ_WEIXIN_DETAIL_URL_JAVA") + uuid +
					"&response_type=code&scope=snsapi_userinfo&state=1&connect_redirect=1#wechat_redirect";
		}
		Long userId = UserUtils.getUser().getId();
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		// 二维码信息存储（url,uuid）
		QrCodeInfo qci = new QrCodeInfo();
		qci.setUrl(url);
		System.out.print(url);
		qci.setCompanyId(companyId);
		qci.setCreateBy(userId.intValue());
		qci.setCreateDate(new Date());
		qci.setUuid(uuid);
		qrCodeInfoDao.save(qci);
		
		
		//保存二维码参数信息
		for (Map<String, Object> map : list) {
			QrCodeParam qcp = new QrCodeParam();
			qcp.setQrCodeUuid(uuid);
			qcp.setCompanyId(companyId);
			qcp.setCreateBy(userId.intValue());
			qcp.setCreateDate(new Date());
			qcp.setGroupId(Integer.parseInt(map.get("id").toString()));
			qcp.setProductId(Integer.parseInt(map.get("acitivityId").toString()));
			qcp.setOrderType(Integer.parseInt(map.get("activityKind").toString()));
			qrCodeParamDao.save(qcp);
		}
		System.out.println(url);
		return url;
	}

	/**
	 * 生成URL编码
	 * @param source
	 * @return
	 */
	private String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
}
