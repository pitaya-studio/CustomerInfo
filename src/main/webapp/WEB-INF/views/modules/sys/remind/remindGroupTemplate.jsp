<%@ page contentType="text/html;charset=UTF-8" %>

<div id="remindGroupTemplate" style="display: none;">
		<input type="radio" name="groupSelect"id="allGroup_dantuan" checked="checked"><label>全部团期(包含未来生成的团期)</label>
		<input style="margin-left:60px"type="radio" name="groupSelect" id="specifyGroup_dantuan" ><label>指定团期</label>
		<div class="set-batch-op-butn-new" id="addSpecifyGroup_dantuan" onclick="selectGroup(this);"style="display:none">选择团期</div>

              		<div class="groupRequirement">
              			<div class="specifyGroupdate">
              				<table>
              					<tr>
              						<td><label>提醒起始时间：</label></td>
              						<td>
              							<span class="editDeadline" style="">
              								<span>还款日期</span>
                                          	<select class="bg-prev-after" style="width:50px;margin-bottom:0"><option selected="selected" class="prev">前</option><option class="after">后</option></select>
                                          	<input value="" name="acitivityName" class="txtPro inputTxt" flag="istips" onkeyup="this.value=this.value.replaceColonChars()" onafterpaste="this.value=this.value.replaceColonChars()" maxlength="20">天
              							</span>
              						</td>
              						<td><label style="text-align:right;width: 84px;">过期时间：</label></td>
              						<td>
              							<span class="editDeadline" style="">
              								<span>还款日期</span>
                                          	<select class="gq-prev-after" style="width:50px;margin-bottom:0"><option class="prev">前</option><option class="after">后</option></select>
                                          	<input value="" name="acitivityName" class="txtPro inputTxt" flag="istips" onkeyup="this.value=this.value.replaceColonChars()" onafterpaste="this.value=this.value.replaceColonChars()" maxlength="20">天
              							</span>
              						</td>
              					</tr>
              				</table>
              			</div>
              		</div>
              		
              		<div style="margin-top:10px">
              			<p class="hide">已选团期：</p>
              			<div class="tablescroll">
              				<form method="post" name="groupform" id="groupform">
              					<table class="table activitylist_bodyer_table selectedGroup selectedGroup" id="selectedGroup_dantuan">
                                      <thead style="background: #403738">
                                          <tr>
                                              <th width="5%">团号</th>
                                              <th width="10%">产品名称</th>
                                              <th width="10%">出团日期</th>
                                              <th width="8%">截团日期</th>
                                              <th width="10%">发布人</th>
										</tr>
                                      </thead>
                                      <tbody name="activityMainInfo">
										<tr>
                                           <td >xxxxxxxxxxx</td>
                                           <td>南美13天4国游</td>
                                           <td>2016-11-11</td>
                                           <td>2016-11-11</td>
                                           <td>AAA</td>
										</tr>
                                      </tbody>
                                  </table>
                                  <table class="table activitylist_bodyer_table hide specifyGroupList selectedGroup" id="specifyGroupList_dantuan">
                                      <thead style="background: #403738">
                                          <tr>
                                              <th width="5%">团号</th>
                                              <th width="10%">产品名称</th>
                                              <th width="10%">出团日期</th>
                                              <th width="8%">截团日期</th>
                                              <th width="10%">发布人</th>
                                          </tr>
                                      </thead>
                                      <tbody>
                                      </tbody>
                                  </table>
                              	<input type="hidden" value="admin" id="tempUserName">
              				</form>
              			</div>
                  	</div>
                  	
                    <div style="margin-top:10px">                               	
                    	<label style="margin-left:50px">提醒接收人：</label>
                    	<input class="btn" value="选择" onclick="selectReciever(this);" type="button"style="height: 27px;">
                    </div>
                    
                    <div class="approve-bill-list abl"></div>
</div>