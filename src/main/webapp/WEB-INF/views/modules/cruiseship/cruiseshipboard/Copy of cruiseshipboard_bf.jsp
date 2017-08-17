<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>奢华之旅-看板</title>
    
	<meta name="decorator" content="wholesaler"/>
    <link rel="stylesheet" href="${ctxStatic}/css/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.css" />
    <link type="text/css" rel="stylesheet" href="${ctxStatic}/css/quauq.css" />
    
    <script type="text/javascript">
    	var $ctx = '${ctx}';
    </script>
	<script src="${ctxStatic}/js/little_logo.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery-ui.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.core.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.position.js"></script>
	<script src="${ctxStatic}/jqueryUI/ui/jquery.ui.datepicker.js"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script type="application/javascript" src="${ctxStatic}/jqueryUI/ui/jquery-ui.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/quauq.base64.js"></script>
	<script type="text/javascript" src="${ctxStatic}/js/common.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/jquery.nicescroll/jquery.nicescroll.plus.js"></script>
    <script type="text/javascript" src="${ctxStatic}/js/boxScroll.js"></script>
    <script type="text/javascript" src="${ctxStatic}/modules/store/billboard.js"></script>
    <script type="text/javascript">
    	$(document).ready(function() {
    		var $ctx = '${ctx}';
    	});
    	function save(){
    		alert();
    	}
    </script>
  </head>
  <body>
  	<input type="hidden" id="ctxInput" value="${ctx }">
    <!--右侧内容部分开始-->
      <div>
          <!--展示层容器-->
          <div class="plate-view-container">
            <div class="plate-set" isShow="true"></div>
            <!--内部展示层容器-->
              <div class="vessel">
                <!--左侧部分-->
                <div class="left">
                  <!--S---游轮名称&日期-->
                  <div class="title-container">
                    <!--S--刷新-->
                    <div class="refresh" id="refreshBoard">
                    </div>
                    <!--E--刷新-->
                    <!--S--游轮名称-->
                    <div class="boat">
                        <div class="copySelect boat"  name="slideList">
                            <div class="copySelect-text">
                                <span id="cruiseship">歌诗达邮轮—公主号</span><em></em>
                            </div>
                          <ul id="cruiseships" class="display-none">
                          </ul>
                        </div>
                    </div>
                    <!--E--游轮名称-->
                    <!--S--时间控件-月份-->
                    <div class="time-container">
                      <div class="month">
                        <label class="text">船期:</label>
                        <div class="copySelect"  name="slideList">
                          <div class="copySelect-text">
                            <span id="cruiseshipDate">2016-01-02</span>
                            <em></em>
                          </div>
                          <ul id="cruiseshipDates" class="display-none">
                          </ul>
                        </div>
                      </div>
                    </div>
                    <!--E--时间控件-月份-->
                  </div>
                  <!--E---游轮名称&日期-->
                  <!---S--展示产品-->
                  <div class="bottom-container">
                    <!--S--舱型&余位&库存-->
                    <div class="cabin-surplus-repertory">
                      <!--舱型库存销售-->
                      <div class="con">
                      </div>
                      <div class="con list" name="cabinTypes" id="cabinTypes">
                       <table>
                         <thead>
                          <tr>
                            <!-- <th ></th>
                            <th ></th>
                            <th ></th>
                            <th ></th>
                            <th ></th>
                            <th ></th> -->
                          </tr>
                         </thead>
                         <tbody>
                         <tr>
                           <!-- <td>
                             <div class="ullage ">
                               <p>SP</p>
                                <p><em>8</em>/<em>30</em></p>
                             </div>
                           </td>
                           <td>
                             <div class="ullage active">
                               <p>SP</p>
                               <p><em>8</em>/<em>30</em></p>
                             </div>
                           </td>
                           <td>
                             <div class="ullage ">
                               <p>SP</p>
                               <p><em>8</em>/<em>30</em></p>
                             </div>
                           </td>
                           <td>
                             <div class="ullage ">
                               <p>SP</p>
                               <p><em>8</em>/<em>30</em></p>
                             </div>
                           </td>
                           <td>
                             <div class="ullage ">
                               <p>SP</p>
                               <p><em>8</em>/<em>30</em></p>
                             </div>
                           </td>
                           <td>
                             <div class="ullage ">
                               <p>SP</p>
                               <p><em>8</em>/<em>30</em></p>
                             </div>
                           </td> -->
                         </tr>
                         </tbody>
                       </table>
                      </div>
                    </div>
                    <!--E--舱型&余位&库存-->
                    <!--S--销售&产品图示-->
                    <div class="sales-porduct-view">
                       <!--S--姓名部分-->
                       <div class="sales" name="sales" id="sales">
                         <ul>
                           <!-- <li class="active">张三</li>
                           <li>李四</li>
                           <li>王五</li>
                           <li>张三</li>
                           <li>李四</li>
                           <li>王五</li> -->
                         </ul>
                       </div>
                      <!--E--姓名部分-->
                      <!--S--产品查看部分-->
                      <div class="porduct-view" name="saleStatus" id="saleStatus">
                        <table>
                          <thead>
                          <tr>
                          </tr>
                          </thead>
                          <tbody>
                          <tr>
                            <td>
                              <li class="active">
                                  <div class="country country_01"><span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>北京</span></div>
                                  <div class="country country_02"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                <div class="view-all-pro-minification"></div>

                              </li>
                            </td>
                            <td>
                              <li>
                                <div class="country country_01"><span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_02"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="view-all-pro-minification"></div>
                                <div class="view-all-prolist">
                                  <div class="country country_01"><span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>北京</span></div>
                                  <div class="country country_02"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                  <div class="country country_04"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_01"><span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>北京</span></div>
                                  <div class="country country_02"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                  <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                </div>
                              </li>
                            </td>
                            <td>
                              <li class="active">
                                <div class="country country_01"><span><em>1</em><i>F<span class="tips-together"></span></i></span>+<span>上海</span></div>
                                <div class="country country_02"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="view-all-pro-minification"></div>
                              </li>
                            </td>
                            <td>
                              <li class="active">
                                <div class="country country_01"><span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_02"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="view-all-pro-minification"></div>
                              </li>
                            </td>
                            <td>
                              <li class="active">
                                <div class="country country_01"><span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_02"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="view-all-pro-minification"></div>
                              </li>
                            </td>
                            <td>
                              <li class="active">
                                <div class="country country_01"><span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_02"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="country country_03"><span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span><em>1</em><i>F</i></span>+<span>上海</span></div>
                                <div class="view-all-pro-minification"></div>
                              </li>
                            </td>

                          </tr>
                          </tbody>
                        </table>
                      </div>
                      <!--E--产品查看部分-->
                    </div>
                    <!--E--销售&产品图示-->
                  </div>
                  <!---E--展示产品-->
                </div>
                <!--S--右侧部分-->
                <div class="right">
                  <!--S--按钮-->
                  <div class="function-part-f">
                    <div class="butn-sign" id="cruiseshipApply"><em></em>占位记录</div>
                    <div class="butn-order" id="myOrder"><em></em>我的订单</div>
                  </div>
                  <!--E--按钮-->
                  <!--S--余位库存信息-->
                  <div class="surplus-repertory margin-bottom-div">
                    <div class="title">
                      <span><em>15</em>/<em>30</em></span>
                      <span><em>余位和库存</em></span>
                    </div>
                    <div class="bottom prs_prn" id="activetyListArea">
                      <!--S--改变后调用样式-->
                      <ul>
                        <li ><span class="checkbox-made marrten active" id="checkAll"></span><label for="">全选</label></li>
                        <!-- <li>
                          <em class="country_01"></em>
                          [<label for="">超长产品名称</label>]
                          <span>
                          <span class="checkbox-made marrten active" name="checkbox"></span>
                          <div class="product-name">异11哥十大歌诗达邮轮—公异11哥十</div>
                          </span>
                        </li>
                        <li>
                          <em class="country_02"></em>
                          [<label for="">超长产品名称</label>]
                          <span>
                          <span class="checkbox-made marrten "></span>
                          <div class="product-name">异11哥十大歌诗达邮轮—公异11哥十</div>
                          </span>
                        </li> -->
                      </ul>
                      <!--E--改变后调用样式-->
                      <!--S--改变前调用样式-->
                      <!--<ul>-->
                        <!--<li ><input type="checkbox"/> <label for="">全选</label></li>-->
                        <!--<li><input type="checkbox"/><em class="country_01"></em> <label for="">超长产品文字测试</label></li>-->
                        <!--<li><input type="checkbox"/><em class="country_02"></em> <label for="">四国游</label></li>-->
                        <!--<li><input type="checkbox"/><em class="country_03"></em> <label for="">极地</label></li>-->
                        <!--<li><input type="checkbox"/><em class="country_04"></em> <label for="">两国</label></li>-->
                      <!--</ul>-->
                      <!--E--改变前调用样式-->
                    </div>
                  </div>
                  <!--E--余位库存信息-->

                  <!--S--出发地-->
                  <!-- <div class="surplus-repertory margin-bottom-div">
                    <div class="bottom place">
                      <ul>
                        <li><input type="checkbox"/> <label for="">出发地：</label></li>
                        <li><input type="checkbox"/><label for="">北京</label></li>
                        <li><input type="checkbox"/><label for="">上海</label></li>
                        <li><input type="checkbox"/><label for="">广州</label></li>
                      </ul>
                    </div>
                  </div> -->
                  <!--E--出发地-->

                  <!--S--拼部分-->
                  <div class="surplus-repertory margin-bottom-div">
                    <div class="bottom man">
                      <ul>
                        <li><label for=""><i class="tips-together pr"></i></label><label for="">拼</label></li>
                        <li><label for="">F</label><label for="">女</label></li>
                        <li><label for="">M</label><label for="">男</label></li>
                      </ul>
                    </div>
                  </div>
                  <!--S--拼部分-->
                  <!--S--确定按钮-->
                  <div class="function-part-last">
                    <div class="butn-confirm" id="filterByActivety">确定</div>
                  </div>
                  <!--S--确定按钮-->
                </div>
                <!--E--右侧部分-->


              </div>
            <div class="masking-out-layer-shade display-none" id="mask">

            </div>
            <!--S--确认窗口-->
            <div class="masking-out-layer-tips delete display-none" id="confirmPop">
              <div class="masking-out-layer-header">
                <div class="close" name="closePop"></div>
              </div>
              <div class="masking-out-layer-content tips">
                <p><em></em><span id="confirmInfo"><span></p>
              </div>
              <div class="masking-out-layer-footer">
                <div class="butn-cancel"  id="cancelOperate"><em></em>取消</div>
                <div class="butn-confirm" id="confirmOperate"><em></em>确定</div>
              </div>
            </div>
            <!--E--确认窗口-->
			
			<!--S--提示窗口-->
            <div class="operate-tips-pop display-none"  id="infoPop">
              <em></em>
              <span name="infoText"></span>
            </div>
            <!--E--提示窗口-->
            <!--S--报名提示-->
	        <div class="masking-out-layer-tips sign-up display-none" id="cruiseshipApplyPop">
		              <div class="masking-out-layer-header">
		                <h1>报名</h1>
		                <div class="close" id="closeApplyPop"></div>
		              </div>
		              <div class="masking-out-layer-content sign-up tips">
		                <ul>
		                  <li><label for="">游轮名称：</label><p id="popCruiseship"></p></li>
		              <!--                            库存Uuid -->
<!--                                 <input value="" id="cruiseshipStockUuid  "name="cruiseshipStockUuid">   -->
<!--                            库存明细Uuid -->
<!--                                 <input value="" id="cruiseshipStockDetailUuid" name="cruiseshipStockDetailUuid">   -->
		                  
		                  <li><label for="">船期：</label><p id="popCruiseshipDate"></p></li>
		                </ul>
		                  <ul>
		                    <li>
		                      <label for="">选择产品：</label>
<!-- 		                      <select class="sex-count-sel" id="popActivetySelect" name="popActivetySelect"> -->
<!-- 		                      </select> -->
								<div class="copySelect product" name="slideList">
			                        <div class="copySelect-text">
			                          <span id="popActivetySelected" ></span>
			                          <em></em>
			                        </div>
			                        <ul class="display-none" id="popActivetySelect">
		                          	</ul>
                      			</div>
		                    </li>
		                    <li>
		                      <label for="">选择舱型：</label>
<!-- 		                      <select class="sex-count-sel" id="popCabinSelect" name="cruiseshipCabinName"> -->
<!-- 		                        <option selected="selected" value="gmyx1_gmyx1">哥斯达游轮-公主号</option> -->
<!-- 		                      </select> -->
                      		<div class="copySelect product" name="slideList">
		                        <div class="copySelect-text">
		                          <span id="popCabinSelected"></span>
		                          <em></em>
		                        </div>
		                        <ul class="display-none"  id="popCabinSelect"></ul>
		                      </div>
		                    </li>
		                    <li><label for="">F女：</label><p><input  class="sex-count-in" id="fnum" name="fnum" data-type="number" type="text"/>人</p><p><input  class="sex-count-check"  id="fpiece" name="fpiece"  value="0" type="checkbox"/><span class="pr">拼<i class="tips-together"></i></span></p></li>
		                    <li><label for="">M男：</label><p><input class="sex-count-in"  id="mnum" name="mnum" data-type="number" type="text"/>人</p><p><input  class="sex-count-check"  id="mpiece" name="mpiece"  value="0" type="checkbox"/><span class="pr">拼<i class="tips-together"></i></span></p></li>
		                  </ul>
		              </div>
		              <div class="masking-out-layer-footer">
		                <div class="butn-save active"><em></em> <a onclick="saveApply();">保存</a> </div>
		              </div>
	        </div>
            <!--E--报名提示-->

            <!--S--我的订单-->
            <div class="masking-out-layer-tips order display-none" id="myOrderPop">
              <div class="masking-out-layer-header">
                <h1>我的订单</h1>
                <div class="title-container-mini">
                  <span>|</span>
                  <!--S--游轮名称-->
                  <div class="boat">
                    <div class="copySelect boat-order" name="slideList">
                      <div class="copySelect-text">
                        <span class="ellipsis-text" id="mdCruiseship">歌诗达邮轮-公主号</span>
                        <em></em>
                      </div>
                        <ul id="mdCruiseships" class="display-none" style="width: 145px;">
                        </ul>
                    </div>
                  </div>
                  <!--E--游轮名称-->
                  <!--S--时间控件--月份-->
                  <div class="time-container order">
                    <div class="month">
                      <label class="text">船期:</label>
                      <div class="copySelect"  name="slideList">
                        <div class="copySelect-text">
                          <span id="mdCruiseshipDate">2015-06-28</span>
                          <em></em>
                        </div>
                        <ul class="display-none" id="mdCruiseshipDates" style="width: 100px;">
                          <li uuid="52223e0b3ff940999618870ca1760b52">2015-06-28</li><li uuid="f940999618870ca152223e0b3f760b52">2015-07-28</li>
                        </ul>
                      </div>
                    </div>
                  </div>
                  <!--E--时间控件--月份-->
                </div>
                <div class="close" id="closeMyOrderPop"></div>
              </div>
              <div class="masking-out-layer-content order tips">
                <table id="mdOrderTable">
                  <thead>
                    <tr>
                      <th class="cabin" id="cabin">舱型<span class="pull-down-own"></span>
                          <div class="copySelect"  name="slideList">
                              <div class="copySelect-text display-none">
                                  <span></span>
                                  <em></em>
                              </div>
                              <ul class="display-none" name="filterUl">
                                  <li uuid="52223e0b3ff940999618870ca1760b52"><em class="checkbox-made marrten active"></em><label for="">AP</label></li>
                                  <li uuid="f940999618870ca152223e0b3f760b52"><em class="checkbox-made marrten "></em><label for="">SP</label></ul>
                          </div>
                      </th>
                      <th class="counter">余位</th>
                      <th class="product-sel" id="product-sel">产品
                        <span class="pull-down-own"></span>
                        <div class="copySelect"  name="slideList">
                          <div class="copySelect-text display-none">
                            <span></span>
                            <em></em>
                          </div>
                          <ul class="display-none" name="filterUl">
                            <li uuid="52223e0b3ff940999618870ca1760b52"><em class="checkbox-made marrten active"></em><label for="">歌诗达邮轮</label></li>
                            <li uuid="f940999618870ca152223e0b3f760b52"><em class="checkbox-made marrten "></em><label for="">歌诗达邮轮</label></ul>
                        </div>
                      </th>
                      <th class="f-group">F/<span class="pr">拼<i class="tips-together"></i></span></th>
                      <th class="m-group">M/<span class="pr">拼<i class="tips-together"></i></span></th>
                      <th class="start" id="start">出发地<span class="pull-down-own"></span>
                          <div class="copySelect"  name="slideList">
                              <div class="copySelect-text display-none">
                                  <span></span>
                                  <em></em>
                              </div>
                              <ul class="display-none" name="filterUl">
                                  <li uuid="52223e0b3ff940999618870ca1760b52"><em class="checkbox-made marrten active"></em><label for="">北京</label></li>
                                  <li uuid="f940999618870ca152223e0b3f760b52"><em class="checkbox-made marrten "></em><label for="">上海</label></ul>
                          </div>
                      </th>
                      <th class="operation"></th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>AP</td>
                      <td>20</td>
                      <td class="product ">歌诗达邮轮—公主号</td>
                      <td>
                        <div class="man-counter">
                          <div class="copySelect number">
                            <div class="copySelect-text">
                              <span>99</span>
                              <em></em>
                            </div>
                            <ul class="display-none">
                              <li uuid="52223e0b3ff940999618870ca1760b522">1</li>
                              <li uuid="f940999618870ca152223e0b3f760b5234f">2</li>
                            </ul>
                          </div>
                          <em class="checkbox-made "></em>
                        </div>
                      </td>
                      <td>
                        <div class="man-counter">
                          <div class="copySelect number">
                            <div class="copySelect-text">
                              <span>99</span>
                              <em></em>
                            </div>
                            <ul id="" class="display-none">
                              <li uuid="52223e0b3ff940999618870ca1760b522">1</li>
                              <li uuid="f940999618870ca152223e0b3f760b5234f">2</li>
                            </ul>
                          </div>
                          <em class="checkbox-made active "></em>
                        </div>
                      </td>
                      <td class="start ">北京</td>
                      <td><div class="operator"><i></i><em></em></div></td>
                    </tr>
                  </tbody>
                  <tbody>
                    <tr>
                      <td>AP</td>
                      <td>20</td>
                      <td class="product ">歌诗达邮轮—公主号</td>
                      <td>
                        <div class="man-counter">
                          <div class="copySelect number">
                            <div class="copySelect-text">
                              <span>99</span>
                              <em></em>
                            </div>
                            <ul class="display-none">
                              <li uuid="52223e0b3ff940999618870ca1760b522">1</li>
                              <li uuid="f940999618870ca152223e0b3f760b5234f">2</li>
                            </ul>
                          </div>
                          <em class="checkbox-made active "></em>
                        </div>
                      </td>
                      <td>
                        <div class="man-counter">
                          <div class="copySelect number">
                            <div class="copySelect-text">
                              <span>99</span>
                              <em></em>
                            </div>
                            <ul class="display-none">
                              <li uuid="52223e0b3ff940999618870ca1760b522">1</li>
                              <li uuid="f940999618870ca152223e0b3f760b5234f">2</li>
                            </ul>
                          </div>
                          <em class="checkbox-made active "></em>
                        </div>
                      </td>
                      <td class="start ">北京</td>
                      <td><div class="operator"><i></i><em></em></div></td>
                    </tr>
                </tbody>
                  <tbody>
                    <tr>
                      <td>AP</td>
                      <td>20</td>
                      <td class="product ">歌诗达邮轮—公主号</td>
                      <td></td>
                      <td></td>
                      <td class="start ">北京</td>
                      <td><div class="operator"><i></i><em></em></div></td>
                    </tr>
                </tbody>
                  <tbody>
                    <tr>
                      <td>AP</td>
                      <td>20</td>
                      <td class="product ">歌诗达邮轮—公主号</td>
                      <td></td>
                      <td></td>
                      <td class="start ">北京</td>
                      <td><div class="operator"><i></i><em></em></div></td>
                    </tr>
                </tbody>
                  <tbody>
                    <tr>
                      <td>AP</td>
                      <td>20</td>
                      <td class="product ">歌诗达邮轮—公主号</td>
                      <td></td>
                      <td></td>
                      <td class="start ">北京</td>
                      <td><div class="operator"><i></i><em></em></div></td>
                    </tr>
                </tbody>
                  <tbody>
                    <tr>
                      <td>AP</td>
                      <td>9999999999999999</td>
                      <td class="product ">歌诗达邮轮—公主号</td>
                      <td></td>
                      <td></td>
                      <td class="start ">北京</td>
                      <td><div class="operator"><i></i><em></em></div></td>
                    </tr>
                </tbody>
                  <tbody>
                    <tr>
                      <td>AP</td>
                      <td>20</td>
                      <td class="product ">歌诗达邮轮—公主号</td>
                      <td></td>
                      <td></td>
                      <td class="start ">北京北京北京北京北京北京</td>
                      <td><div class="operator"><i></i><em></em></div></td>
                    </tr>
                </tbody>
                  <tbody>
                    <tr>
                      <td>AP</td>
                      <td>20</td>
                      <td class="product ">歌诗达邮轮—公主号歌诗达邮轮—公主号</td>
                      <td></td>
                      <td></td>
                      <td class="start ">北京</td>
                      <td><div class="operator"><i></i><em></em></div></td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <div class="masking-out-layer-footer">
                <div class="butn-cancel "  id="cancelMD"><em></em>取消</div>
                <div class="butn-confirm active" id="saveMD"><em></em>保存</div>
              </div>
            </div>
            <!--E--我的订单-->
      </div>
      </div>
      <!--右侧内容部分结束--> 
      <div class="display-none" name="foolishWay">
    </div>
  </body>
</html>
