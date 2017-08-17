package com.trekiz.admin.common.exception.common;

import java.util.HashMap;
import java.util.Map;

public class ExceptionConstants {
	
	//产品线类型常量
	public static Map<Integer,String> productTypeMap=new HashMap<Integer,String>();
	public static Integer PRODUCT_TYPE_DANTUAN=1;//单团
	public static Integer PRODUCT_TYPE_SANPIN=2;//散拼
	public static Integer PRODUCT_TYPE_YOUXUE=3;//游学
	public static Integer PRODUCT_TYPE_DAKEHU=4;//大客户
	public static Integer PRODUCT_TYPE_ZIYOUXING=5;//自由行
	public static Integer PRODUCT_TYPE_YOULUN=6;//游轮
	public static Integer PRODUCT_TYPE_JIPIAO=7;//机票
	public static Integer PRODUCT_TYPE_QIANZHENG=8;//签证
	
	public static Integer PRODUCT_TYPE_FINANCE=50;//财务
	
	public static Integer PRODUCT_TYPE_QUAUQ = 100;//公司内部数据统计模块
	
	static{
		productTypeMap.put(PRODUCT_TYPE_DANTUAN, "单团");
		productTypeMap.put(PRODUCT_TYPE_SANPIN, "散拼");
		productTypeMap.put(PRODUCT_TYPE_YOUXUE, "游学");
		productTypeMap.put(PRODUCT_TYPE_DAKEHU, "大客户");
		productTypeMap.put(PRODUCT_TYPE_ZIYOUXING, "自由行");
		productTypeMap.put(PRODUCT_TYPE_YOULUN, "游轮");
		productTypeMap.put(PRODUCT_TYPE_JIPIAO, "机票");
		productTypeMap.put(PRODUCT_TYPE_QIANZHENG, "签证");
		productTypeMap.put(PRODUCT_TYPE_FINANCE, "财务");
		productTypeMap.put(PRODUCT_TYPE_QUAUQ, "QUAUQ数据统计");
	}
	
	//业务模块常量
	public static Map<Integer,String> moduleMap=new HashMap<Integer,String>();
	public static Integer MODULE_DINGDAN=1;//订单模块
	public static Integer MODULE_CHANPIN=2;//产品模块
	public static Integer MODULE_BAOMING=3;//预订报名模块
	
	public static Integer MODULE_CHENGBENFUKUAN=50;//成本付款
	public static Integer MODULE_CHENGBENFUKUANZFJL=51;//成本付款支付记录
	public static Integer MODULE_CHENGBENLURU=52;//成本录入
	public static Integer MODULE_DINGDANSHOUKUAN=53;//订单收款
	public static Integer MODULE_FAPIAOGUANLIDSHFP=54;//发票管理-待审核发票
	public static Integer MODULE_FAPIAOGUANLIKP=55;//发票管理-开票
	public static Integer MODULE_FAPIAOGUANLIYSHFP=56;//发票管理-已审核发票
	public static Integer MODULE_FANYONGFUKUAN=57;//返佣付款
	public static Integer MODULE_JIESUANDAN=58;//结算单
	public static Integer MODULE_JIEKUANFUKUAN=59;//借款付款
	public static Integer MODULE_JIEKUANFUKUANZFJL=60;//借款付款支付记录
	public static Integer MODULE_JIESUANDANLMT=61;//拉美图结算单
	public static Integer MODULE_YUBAODANLMT=62;//拉美途-预报单
	public static Integer MODULE_QITASHOURUSHOUKUAN=63;//其他收入收款
	public static Integer MODULE_QZDINGDANSHOUKUAN=64;//签证订单收款
	public static Integer MODULE_QZYAJINSHOUKUAN=65;//签证押金收款
	public static Integer MODULE_SHOUJUGUANLI=66;//收据管理
	public static Integer MODULE_TUIKUANFUKUAN=67;//退款付款
	public static Integer MODULE_YUBAODAN=68;//预报单
	
	public static Integer MODULE_SHENPI=100;//审批模块
	public static Integer MODULE_GAIJIASP=101;//改价
	public static Integer MODULE_FANYONGSP=102;//返佣
	public static Integer MODULE_TUITUANSP=103;//退团
	public static Integer MODULE_ZHUANTUANSP=104;//转团
	public static Integer MODULE_ZHUANKUANSP=105;//转款
	public static Integer MODULE_JIEKUANSP=106;//借款
	public static Integer MODULE_TUIKUANSP=107;//退款
	public static Integer MODULE_CHENGBENFUKUANSP=108;//成本付款审批
	public static Integer MODULE_TUIPIAO=109;//退票
	public static Integer MODULE_GAIQIAN=110;//改签
	public static Integer MODULE_TUIQIANZHENGYJ=111;//退签证押金
	public static Integer MODULE_YUSUANCHENGBENSP=112;//预算成本审批
	public static Integer MODULE_SHIJICHENGBENSP=113;//实际成本审批
	
	public static Integer MODULE_YOULUNKUCUN=114;//游轮库存
	
	public static Integer MODULE_QUAUQ_ORDER = 115;//订单信息统计

	public static Integer MODULE_DANBAOSP=116;//担保变更审批
	
	static{
		moduleMap.put(MODULE_DINGDAN,"订单模块");
		moduleMap.put(MODULE_CHANPIN,"产品模块");
		moduleMap.put(MODULE_BAOMING,"预订报名模块");
		
		moduleMap.put(MODULE_CHENGBENFUKUAN,"成本付款");
		moduleMap.put(MODULE_CHENGBENFUKUANZFJL,"成本付款支付记录");
		moduleMap.put(MODULE_CHENGBENLURU,"成本录入");
		moduleMap.put(MODULE_DINGDANSHOUKUAN,"订单收款");
		moduleMap.put(MODULE_FAPIAOGUANLIDSHFP,"发票管理-待审核发票");
		moduleMap.put(MODULE_FAPIAOGUANLIKP,"发票管理-开票");
		moduleMap.put(MODULE_FAPIAOGUANLIYSHFP,"发票管理-已审核发票");
		moduleMap.put(MODULE_FANYONGFUKUAN,"返佣付款");
		moduleMap.put(MODULE_JIESUANDAN,"结算单");
		moduleMap.put(MODULE_JIEKUANFUKUAN,"借款付款");
		moduleMap.put(MODULE_JIEKUANFUKUANZFJL,"借款付款支付记录");
		moduleMap.put(MODULE_JIESUANDANLMT,"拉美图结算单");
		moduleMap.put(MODULE_YUBAODANLMT,"拉美途-预报单");
		moduleMap.put(MODULE_QITASHOURUSHOUKUAN,"其他收入收款");
		moduleMap.put(MODULE_QZDINGDANSHOUKUAN,"签证订单收款");
		moduleMap.put(MODULE_QZYAJINSHOUKUAN,"签证押金收款");
		moduleMap.put(MODULE_SHOUJUGUANLI,"收据管理");
		moduleMap.put(MODULE_TUIKUANFUKUAN,"退款付款");
		moduleMap.put(MODULE_YUBAODAN,"预报单");
		
		moduleMap.put(MODULE_SHENPI,"审批模块");
		moduleMap.put(MODULE_GAIJIASP,"改价");
		moduleMap.put(MODULE_FANYONGSP,"返佣");
		moduleMap.put(MODULE_TUITUANSP,"退团");
		moduleMap.put(MODULE_ZHUANTUANSP,"转团");
		moduleMap.put(MODULE_ZHUANKUANSP,"转款");
		moduleMap.put(MODULE_JIEKUANSP,"借款");
		moduleMap.put(MODULE_TUIKUANSP,"退款");
		moduleMap.put(MODULE_CHENGBENFUKUANSP,"成本付款审批");
		moduleMap.put(MODULE_TUIPIAO,"退票");
		moduleMap.put(MODULE_GAIQIAN,"改签");
		moduleMap.put(MODULE_TUIQIANZHENGYJ,"退签证押金");
		moduleMap.put(MODULE_YUSUANCHENGBENSP,"预算成本审批");
		moduleMap.put(MODULE_SHIJICHENGBENSP,"实际成本审批");
		moduleMap.put(MODULE_QUAUQ_ORDER,"订单信息统计");
		
		moduleMap.put(MODULE_YOULUNKUCUN,"游轮库存");
		moduleMap.put(MODULE_DANBAOSP,"担保变更审批");
	}
	
	//操作类型常量
	public static Map<Integer,String> optTypeMap=new HashMap<Integer,String>();
	
	//基础操作
	public static Integer OPT_TYPE_ADD=1;//增加
	public static Integer OPT_TYPE_UPDATE=2;//修改
	public static Integer OPT_TYPE_DELETE=3;//删除
	public static Integer OPT_TYPE_SHOW=4;//查看详情
	public static Integer OPT_TYPE_SORT=5;//排序
	public static Integer OPT_TYPE_BATCHDELETE=6;//批量删除
	public static Integer OPT_TYPE_QUERY=7;//查询
	
	//产品信息
	public static Integer OPT_TYPE_XIAJIA=20;//下架
	public static Integer OPT_TYPE_PILIANGXIAJIA=21;//批量下架
	public static Integer OPT_TYPE_TUANQIFABU=22;//团期发布
	public static Integer OPT_TYPE_ZHANKAITUANQI=23;//展开团期
	public static Integer OPT_TYPE_TUANQIXIUGAI=24;//团期修改
	public static Integer OPT_TYPE_TUANQISHANCHU=25;//团期删除
	public static Integer OPT_TYPE_XINZENGTUANQI=26;//新增团期
	public static Integer OPT_TYPE_XIUGAITUANQI=27;//修改团期
	
	public static Integer OPT_TYPE_DANCHENGCHANPIN=28;//单程产品发布
	public static Integer OPT_TYPE_WANGFANCHANPIN=29;//往返产品发布
	public static Integer OPT_TYPE_DUODUANCHANPIN=30;//多段产品发布
	public static Integer OPT_TYPE_XINZENGHANDUAN=31;//新增航段
	public static Integer OPT_TYPE_ZHANKAIHANGDUAN=32;//展开航段
	
	public static Integer OPT_TYPE_XIAZAIZILIAOMOBAN=33;//下载资料模板
	
	public static Integer OPT_TYPE_QIANZHENGCHANPINFABU=34;//签证产品发布
	
	//订单操作
	public static Integer OPT_TYPE_QUERENZHANWEI=51;//确认占位
	public static Integer OPT_TYPE_CHEXIAOZHANWEI=52;//撤销占位
	public static Integer OPT_TYPE_QUXIAO=53;//取消
	public static Integer OPT_TYPE_JIESUO=54;//解锁
	public static Integer OPT_TYPE_SUODING=55;//锁死
	public static Integer OPT_TYPE_SHANGCHUANZILIAO=56;//上传资料
	public static Integer OPT_TYPE_SHANGCHUANQUERENDAN=57;//上传确认单
	public static Integer OPT_TYPE_QIANZHENGXINXI=58;//签证信息
	public static Integer OPT_TYPE_FAPIAOSHENQING=59;//发票申请
	public static Integer OPT_TYPE_SHOUJUSHENQING=60;//收据申请
	public static Integer OPT_TYPE_YOUKEZILIAO=61;//游客资料
	public static Integer OPT_TYPE_MIANQIANTONGZHI=62;//面签通知
	public static Integer OPT_TYPE_CHUTUANTONGZHI=63;//出团通知
	public static Integer OPT_TYPE_QUERENDAN=64;//确认单
	public static Integer OPT_TYPE_ZHIFUJILU=65;//支付记录
	public static Integer OPT_TYPE_FUQUANKUAN=66;//付全款
	public static Integer OPT_TYPE_FUDINGJIN=67;//付订金
	public static Integer OPT_TYPE_FUWEIKUAN=68;//付尾款
	public static Integer OPT_TYPE_FAPIAOMINGXI=69;//发票明细
	public static Integer OPT_TYPE_SHOUJUMINGXI=70;//收据明细
	
	public static Integer OPT_TYPE_CHAKANYUYUEBIAO=71;//查看预约表
	public static Integer OPT_TYPE_ZHANKAIKEHU=72;//展开客户
	public static Integer OPT_TYPE_HUZHAOLINGQUDAN=73;//护照领取单
	public static Integer OPT_TYPE_YUSHENBIAO=74;//预审表
	public static Integer OPT_TYPE_YUYUEBIAO=75;//预约表
	public static Integer OPT_TYPE_HUANSHOUJU=76;//还收据
	public static Integer OPT_TYPE_JIEKUANMINGXI=77;//借款明细
	public static Integer OPT_TYPE_XIUGAIYAJIN=78;//修改押金
	public static Integer OPT_TYPE_JIEHUZHAO=79;//借护照
	public static Integer OPT_TYPE_HUANHUZHAO=80;//还护照
	public static Integer OPT_TYPE_QIANSHOUZILIAO=81;//签收资料
	
	public static Integer OPT_TYPE_YOUKELIEBIAOBAOCUN=82;//游客列表保存修改
	
	public static Integer OPT_TYPE_PILIANGCAOZUOJILULIBIAO=83;//批量操作记录列表
	public static Integer OPT_TYPE_PILIANGCAOZUOJILUTIJIAO=84;//批量操作记录提交
	public static Integer OPT_TYPE_PILIANGCAOZUOJILUHUANSHOUJU=85;//批量操作记录还收据
	public static Integer OPT_TYPE_PILIANGCAOZUOJILUPILIANGHUAN=86;//批量操作记录批量还收据
	public static Integer OPT_TYPE_PILIANGCAOZUOYOUKELIEBIAO=87;//批量操作记录游客列表
	public static Integer OPT_TYPE_PILIANGCAOZUOYOUKEXIANGQING=88;//批量操作记录游客列表查看详情
	public static Integer OPT_TYPE_PILIANGCAOZUOYOUKESHANCHU=89;//批量操作记录游客列表删除
	
	public static Integer OPT_TYPE_PILIANGJIEHUZHAO=90;//批量借护照
	public static Integer OPT_TYPE_PILIANGHUANHUZHAO=91;//批量还护照
	public static Integer OPT_TYPE_PILIANGHUANSHOUJU=92;//批量还收据
	public static Integer OPT_TYPE_PILIANGCAOZUOQIANZHENG=93;//批量操作签证
	public static Integer OPT_TYPE_PILIANGCAOZUOHUZHAO=94;//批量操作护照
	
	public static Integer OPT_TYPE_PILIANGFUKUAN=95;//游客列表批量付款
	public static Integer OPT_TYPE_PILIANGSHANCHUYOUKE=96;//游客列表批量删除游客
	public static Integer OPT_TYPE_CANTUAN=97;//游客列表参团
	public static Integer OPT_TYPE_YOUKELIEBIAOSHANCHU=98;//游客列表删除
	public static Integer OPT_TYPE_YOUKELIEBIAOFK=99;//游客列表付款
	
	//财务操作
	public static Integer OPT_TYPE_FUKUAN=100;//付款
	public static Integer OPT_TYPE_QUERENFUKUAN=101;//确认付款
	public static Integer OPT_TYPE_CHEXIAOFUKUAN=102;//撤销付款
	public static Integer OPT_TYPE_KAIPIAO=103;//开票
	public static Integer OPT_TYPE_KAISHOUJU=104;//开收据
	public static Integer OPT_TYPE_BAOMINGSHOUKUANDANDY=105;//客人报名收款单-打印
	public static Integer OPT_TYPE_LINGQU=106;//领取
	public static Integer OPT_TYPE_PILIANGQUERENFK=107;//批量确认付款
	public static Integer OPT_TYPE_SHOUKUANQUERENDJ=108;//收款确认定金
	public static Integer OPT_TYPE_SHOUKUANQUERENQK=109;//收款确认全款
	public static Integer OPT_TYPE_SHOUKUANQUERENSK=110;//收款确认收款
	public static Integer OPT_TYPE_SHOUKUANQUERENSKQR=111;//收款确认收款确认
	public static Integer OPT_TYPE_SHOUKUANQUERENWK=112;//收款确认尾款
	public static Integer OPT_TYPE_ZHICHUPINGDANDY=113;//支出凭单打印
	
	public static Integer OPT_TYPE_SHENQING=200;//提交申请
	public static Integer OPT_TYPE_PILIANGSHENPI=201;//批量审批
	public static Integer OPT_TYPE_PILIANGSHENPIBH=202;//批量审批驳回
	public static Integer OPT_TYPE_PILIANGSHENPITG=203;//批量审批通过
	public static Integer OPT_TYPE_QIXIAOSHENPI=204;//取消审批
	public static Integer OPT_TYPE_SHENPITONGGUO=205;//审批通过
	public static Integer OPT_TYPE_BOHUI=206;//驳回
	public static Integer OPT_TYPE_CHEXIAO=207;//撤销
	
	
	public static Integer OPT_TYPE_STOCK_KOUJIAN=208;//库存订单报名扣减余位
	public static Integer OPT_TYPE_STOCK_BIANGENG=209;//库存订单修改变更余位
	public static Integer OPT_TYPE_STOCK_GUIHUAN=210;//库存订单删除归还余位
	static{
		//基础操作
		optTypeMap.put(OPT_TYPE_ADD,"增加");
		optTypeMap.put(OPT_TYPE_UPDATE,"修改");
		optTypeMap.put(OPT_TYPE_DELETE,"删除");
		optTypeMap.put(OPT_TYPE_SHOW,"查看详情");
		optTypeMap.put(OPT_TYPE_SORT,"排序");
		optTypeMap.put(OPT_TYPE_BATCHDELETE,"批量删除");
		optTypeMap.put(OPT_TYPE_QUERY,"查询");
		
		//产品信息
		optTypeMap.put(OPT_TYPE_XIAJIA,"下架");
		optTypeMap.put(OPT_TYPE_PILIANGXIAJIA,"批量下架");
		optTypeMap.put(OPT_TYPE_TUANQIFABU,"团期发布");
		optTypeMap.put(OPT_TYPE_ZHANKAITUANQI,"展开团期");
		optTypeMap.put(OPT_TYPE_TUANQIXIUGAI,"团期修改");
		optTypeMap.put(OPT_TYPE_TUANQISHANCHU,"团期删除");
		optTypeMap.put(OPT_TYPE_XINZENGTUANQI,"新增团期");
		optTypeMap.put(OPT_TYPE_XIUGAITUANQI,"修改团期");
		
		optTypeMap.put(OPT_TYPE_DANCHENGCHANPIN,"单程产品发布");
		optTypeMap.put(OPT_TYPE_WANGFANCHANPIN,"往返产品发布");
		optTypeMap.put(OPT_TYPE_DUODUANCHANPIN,"多段产品发布");
		optTypeMap.put(OPT_TYPE_XINZENGHANDUAN,"新增航段");
		optTypeMap.put(OPT_TYPE_ZHANKAIHANGDUAN,"展开航段");
		
		optTypeMap.put(OPT_TYPE_XIAZAIZILIAOMOBAN,"下载资料模板");
		
		optTypeMap.put(OPT_TYPE_QIANZHENGCHANPINFABU,"签证产品发布");
		
		//订单操作
		optTypeMap.put(OPT_TYPE_QUERENZHANWEI,"确认占位");
		optTypeMap.put(OPT_TYPE_CHEXIAOZHANWEI,"撤销占位");
		optTypeMap.put(OPT_TYPE_QUXIAO,"取消");
		optTypeMap.put(OPT_TYPE_JIESUO,"解锁");
		optTypeMap.put(OPT_TYPE_SUODING,"锁死");
		optTypeMap.put(OPT_TYPE_SHANGCHUANZILIAO,"上传资料");
		optTypeMap.put(OPT_TYPE_SHANGCHUANQUERENDAN,"上传确认单");
		optTypeMap.put(OPT_TYPE_QIANZHENGXINXI,"签证信息");
		optTypeMap.put(OPT_TYPE_FAPIAOSHENQING,"发票申请");
		optTypeMap.put(OPT_TYPE_SHOUJUSHENQING,"收据申请");
		optTypeMap.put(OPT_TYPE_YOUKEZILIAO,"游客资料");
		optTypeMap.put(OPT_TYPE_MIANQIANTONGZHI,"面签通知");
		optTypeMap.put(OPT_TYPE_CHUTUANTONGZHI,"出团通知");
		optTypeMap.put(OPT_TYPE_QUERENDAN,"确认单");
		optTypeMap.put(OPT_TYPE_ZHIFUJILU,"支付记录");
		optTypeMap.put(OPT_TYPE_FUQUANKUAN,"付全款");
		optTypeMap.put(OPT_TYPE_FUDINGJIN,"付订金");
		optTypeMap.put(OPT_TYPE_FUWEIKUAN,"付尾款");
		optTypeMap.put(OPT_TYPE_FAPIAOMINGXI,"发票明细");
		optTypeMap.put(OPT_TYPE_SHOUJUMINGXI,"收据明细");
		
		optTypeMap.put(OPT_TYPE_CHAKANYUYUEBIAO,"查看预约表");
		optTypeMap.put(OPT_TYPE_ZHANKAIKEHU,"展开客户");
		optTypeMap.put(OPT_TYPE_HUZHAOLINGQUDAN,"护照领取单");
		optTypeMap.put(OPT_TYPE_YUSHENBIAO,"预审表");
		optTypeMap.put(OPT_TYPE_YUYUEBIAO,"预约表");
		optTypeMap.put(OPT_TYPE_HUANSHOUJU,"还收据");
		optTypeMap.put(OPT_TYPE_JIEKUANMINGXI,"借款明细");
		optTypeMap.put(OPT_TYPE_XIUGAIYAJIN,"修改押金");
		optTypeMap.put(OPT_TYPE_JIEHUZHAO,"借护照");
		optTypeMap.put(OPT_TYPE_HUANHUZHAO,"还护照");
		optTypeMap.put(OPT_TYPE_QIANSHOUZILIAO,"签收资料");
		
		optTypeMap.put(OPT_TYPE_YOUKELIEBIAOBAOCUN,"游客列表保存修改");
		
		optTypeMap.put(OPT_TYPE_PILIANGCAOZUOJILULIBIAO,"批量操作记录列表");
		optTypeMap.put(OPT_TYPE_PILIANGCAOZUOJILUTIJIAO,"批量操作记录提交");
		optTypeMap.put(OPT_TYPE_PILIANGCAOZUOJILUHUANSHOUJU,"批量操作记录还收据");
		optTypeMap.put(OPT_TYPE_PILIANGCAOZUOJILUPILIANGHUAN,"批量操作记录批量还收据");
		optTypeMap.put(OPT_TYPE_PILIANGCAOZUOYOUKELIEBIAO,"批量操作记录游客列表");
		optTypeMap.put(OPT_TYPE_PILIANGCAOZUOYOUKEXIANGQING,"批量操作记录游客列表查看详情");
		optTypeMap.put(OPT_TYPE_PILIANGCAOZUOYOUKESHANCHU,"批量操作记录游客列表删除");
		
		optTypeMap.put(OPT_TYPE_PILIANGJIEHUZHAO,"批量借护照");
		optTypeMap.put(OPT_TYPE_PILIANGHUANHUZHAO,"批量还护照");
		optTypeMap.put(OPT_TYPE_PILIANGHUANSHOUJU,"批量还收据");
		optTypeMap.put(OPT_TYPE_PILIANGCAOZUOQIANZHENG,"批量操作签证");
		optTypeMap.put(OPT_TYPE_PILIANGCAOZUOHUZHAO,"批量操作护照");
		
		optTypeMap.put(OPT_TYPE_PILIANGFUKUAN,"游客列表批量付款");
		optTypeMap.put(OPT_TYPE_PILIANGSHANCHUYOUKE,"游客列表批量删除游客");
		optTypeMap.put(OPT_TYPE_CANTUAN,"游客列表参团");
		optTypeMap.put(OPT_TYPE_YOUKELIEBIAOSHANCHU,"游客列表删除");
		optTypeMap.put(OPT_TYPE_YOUKELIEBIAOFK,"游客列表付款");
		
		//财务操作
		optTypeMap.put(OPT_TYPE_FUKUAN,"付款");
		optTypeMap.put(OPT_TYPE_QUERENFUKUAN,"确认付款");
		optTypeMap.put(OPT_TYPE_CHEXIAOFUKUAN,"撤销付款");
		optTypeMap.put(OPT_TYPE_KAIPIAO,"开票");
		optTypeMap.put(OPT_TYPE_KAISHOUJU,"开收据");
		optTypeMap.put(OPT_TYPE_BAOMINGSHOUKUANDANDY,"客人报名收款单-打印");
		optTypeMap.put(OPT_TYPE_LINGQU,"领取");
		optTypeMap.put(OPT_TYPE_PILIANGQUERENFK,"批量确认付款");
		optTypeMap.put(OPT_TYPE_SHOUKUANQUERENDJ,"收款确认定金");
		optTypeMap.put(OPT_TYPE_SHOUKUANQUERENQK,"收款确认全款");
		optTypeMap.put(OPT_TYPE_SHOUKUANQUERENSK,"收款确认收款");
		optTypeMap.put(OPT_TYPE_SHOUKUANQUERENSKQR,"收款确认收款确认");
		optTypeMap.put(OPT_TYPE_SHOUKUANQUERENWK,"收款确认尾款");
		optTypeMap.put(OPT_TYPE_ZHICHUPINGDANDY,"支出凭单打印");
		
		optTypeMap.put(OPT_TYPE_SHENQING,"提交申请");
		optTypeMap.put(OPT_TYPE_PILIANGSHENPI,"批量审批");
		optTypeMap.put(OPT_TYPE_PILIANGSHENPIBH,"批量审批驳回");
		optTypeMap.put(OPT_TYPE_PILIANGSHENPITG,"批量审批通过");
		optTypeMap.put(OPT_TYPE_QIXIAOSHENPI,"取消审批");
		optTypeMap.put(OPT_TYPE_SHENPITONGGUO,"审批通过");
		optTypeMap.put(OPT_TYPE_BOHUI,"驳回");
		optTypeMap.put(OPT_TYPE_CHEXIAO,"撤销");
		
		optTypeMap.put(OPT_TYPE_STOCK_KOUJIAN,"库存订单报名扣减余位");
		optTypeMap.put(OPT_TYPE_STOCK_BIANGENG,"库存订单修改变更余位");
		optTypeMap.put(OPT_TYPE_STOCK_GUIHUAN,"库存订单删除归还余位");
	}
	
		
}
