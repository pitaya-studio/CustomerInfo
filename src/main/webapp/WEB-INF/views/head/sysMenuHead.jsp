<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>

<script type="application/javascript">
    $(function(){
        var showType = '<sitemesh:getProperty property="showType" />';
        var mark = '';
        var markTail = '';
        switch (showType)
        {
            case 'travel_type' :
                mark = 'product_info';
                markTail = 'travel_type';
				
                break;
            case 'product_type' :
                mark = 'product_info';
                markTail = 'product_type';
                break;
            case 'product_level' :
                mark = 'product_info';
                markTail = 'product_level';
                break;
            case 'traffic_mode' :
                mark = 'traffic_info';
                markTail = 'traffic_mode';
                break;
            case 'airline' :
                mark = 'traffic_info';
                markTail = 'airline';
                break;
            case 'airport' :
                mark = 'traffic_info';
                markTail = 'airport';
                break;
            case 'fromarea' :
                mark = 'area_info';
                markTail = 'fromarea';
                break;
            case 'outarea' :
                mark = 'area_info';
                markTail = 'outarea';
                break;
            case 'area' :
                mark = 'area_info';
                markTail = 'area';
                break;
            case 'currency' :
                mark = 'currency';
                break;
                
           
            case 'hotel' :
                mark = 'hotel_info';
                markTail = 'hotel';
                break;

            case 'hotel_guest_type' :
				mark = 'hotel_info';
                markTail = 'hotel_guest_type';
                break;
            case 'hotel_facilities' :
                mark = 'hotel_info';
                markTail = 'hotel_facilities';
                break;
            case 'hotel_topic' :
                mark = 'hotel_info';
                markTail = 'hotel_topic';
                break;
            case 'hotel_star' :
                mark = 'hotel_info';
                markTail = 'hotel_star';
                break;
            case 'hotel_meal_type' :
                mark = 'hotel_info';
                markTail = 'hotel_meal_type';
                break;
            case 'hotel_room' :
                mark = 'hotel_info';
                markTail = 'hotel_room';
                break;
            case 'hotel_floor' :
                mark = 'hotel_info';
                markTail = 'hotel_floor';
                break;
            case 'hotel_meal' :
                mark = 'hotel_info';
                markTail = 'hotel_meal';
                break;
            case 'hotel_feature' :
                mark = 'hotel_info';
                markTail = 'hotel_feature';
                break;
            case 'hotel_type' :
                mark = 'hotel_info';
                markTail = 'hotel_type';
                break;
            case 'room_feature' :
                mark = 'hotel_info';
                markTail = 'room_feature';
                break;
            case 'hotel_bed_type' :
                mark = 'hotel_info';
                markTail = 'hotel_bed_type';
                break;
           
                
            case 'islands_manager' :
                mark = 'islands_info';
                markTail = 'islands_manager';
                break;
            case 'islands_topic' :
                mark = 'islands_info';
                markTail = 'islands_topic';
                break;
            case 'islands_type' :
                mark = 'islands_info';
                markTail = 'islands_type';
                break;
            case 'islands_way' :
                mark = 'islands_info';
                markTail = 'islands_way';
                break;
            
                
            case 'travel_agency_type' :
            	mark = 'travel_agency_info';
                markTail = 'travel_agency_type';
                break;
            case 'travel_agency_level' :
            	mark = 'travel_agency_info';
                markTail = 'travel_agency_level';
                break;
                
            case 'traveler_type' :
            	mark = 'traveler_info';
            	markTail = 'traveler_type';
            	break;
            case 'numberRule' :
                mark = 'numberRule';
                break;
        }
        if(markTail != ''){
            $('#' + markTail).addClass('active');
			//delete .children('a')
        }
        

		$('#'+showType).show().addClass("active");
		
    });
</script>

<content tag="three_level_menu" >
     <!-- <li id="product_info" class="ernav"><a href="javascript:void(0);">产品信息<i></i></a>
        <dl> -->
           <li id="travel_type" style="display:none;"><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=travel_type">旅游类型</a></li>
           <li id="product_type" style="display:none;"><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=product_type">产品类型</a></li>
           <li id="product_level" style="display:none;"><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=product_level">产品系列</a></li>
        <!-- </dl>
    </li> -->
    <!-- <li id="traffic_info" class="ernav"><a href="javascript:void(0);">交通信息<i></i></a>
        <dl> -->
           <li id="traffic_mode" style="display:none;"><a href="${ctx}/sys/CompanyDict/CompanyDictList?type=traffic_mode">交通方式</a></li>
           <li id="airline" style="display:none;"><a href="${ctx}/sys/airline/list/1">航空公司</a></li>
           <li id="airport" style="display:none;"><a href="${ctx}/sys/airport/list/1">机场信息</a></li>
       <!--  </dl>
    </li> -->
   <!--  <li id="area_info" class="ernav"><a href="javascript:void(0);">地域城市<i></i></a>
        <dl> -->
           <li id="fromarea" style="display:none;" style="display:none;"><a href="${ctx}/sys/dict/pagingList?type=fromarea">出发城市</a></li>
           <li id="outarea" style="display:none;"><a href="${ctx}/sys/dict/pagingList?type=outarea">离境城市</a></li>
           <li id="area" style="display:none;"><a href="${ctx}/sys/dict/normalList?type=area">目的地</a></li>
      <!--   </dl>
    </li> -->
    <li id="currency" style="display:none;"><a href="${ctx}/sys/currency/list">币种信息</a></li>
    
    <!-- <li id="hotel_info" class="ernav"><a href="javascript:void(0);">酒店信息<i></i></a>
        <dl> -->
			<li id="hotel" style="display:none;"><a href="${ctx}/hotel/list">酒店管理</a></li>
<!-- 			<li id="hotel_room"><a href="${ctx}/hotelRoom/list">酒店房型</a><span>丨</span></li> -->
<!-- 			<li id="hotel_floor"><a href="${ctx}/sysCompanyDictView/list?type=hotel_floor">酒店楼层</a><span>丨</span></li> -->
<!-- 			<li id="hotel_meal"><a href="${ctx}/hotelMeal/list">酒店餐型</a><span>丨</span></li> -->
			<li id="hotel_feature" style="display:none;"><a href="${ctx}/hotelFeature/list">酒店特色</a></li>
			<li id="hotel_type" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=hotel_type">酒店类别</a></li>
			<li id="room_feature" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=room_feature">房型特色</a></li>
			<li id="hotel_bed_type" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=hotel_bed_type">酒店床型</a></li>
			<li id="hotel_star" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=hotel_star">酒店星级</a></li>
			<li id="hotel_topic" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=hotel_topic">酒店主题</a></li>
			<li id="hotel_meal_type" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=hotel_meal_type">餐型类型</a></li>
			<li id="hotel_facilities" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=hotel_facilities">酒店设施</a></li>
			<li id="hotel_guest_type" style="display:none;"><a href="${ctx}/hotelGuestType/list">酒店住客类型</a></li>
			
     <!--    </dl>
    </li> -->
    
   <!--  <li id="islands_info" class="ernav"><a href="javascript:void(0);">岛屿信息<i></i></a>
        <dl> -->
        	<li id="islands_manager" style="display:none;"><a href="${ctx}/island/list">岛屿管理</a></li>
			<li id="islands_topic" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=islands_topic">岛屿主题</a></li>
			<li id="islands_type" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=islands_type">岛屿类型</a></li>
			<li id="islands_way" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=islands_way">岛屿上岛方式</a></li>
       <!--  </dl>
    </li> -->
    <!-- 
    <li id="travel_agency_info" class="ernav"><a href="javascript:void(0);">地接社信息<i></i></a>
        <dl> -->
        	<li id="travel_agency_type" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=travel_agency_type">地接社类型管理</a></li>
			<li id="travel_agency_level" style="display:none;"><a href="${ctx}/sysCompanyDictView/list?type=travel_agency_level">地接社等级管理</a></li>
    <!--     </dl>
    </li> -->
    
   <!--  <li id="traveler_info" class="ernav"><a href="javascript:void(0);">游客信息<i></i></a>
        <dl> -->
        	<li id="traveler_type" style="display:none;"><a href="${ctx}/travelerType/list">游客类型管理</a></li>
        <!-- </dl>
    </li> -->
    
    <li id="numberRule" style="display:none;"><a href="${ctx}/sys/numberRule/list">编号规则</a></li>
</content>