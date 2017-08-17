package com.trekiz.admin.modules.reviewreceipt.web;

import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.entity.ReviewReceiptConfiguration;
import com.trekiz.admin.modules.reviewreceipt.model.ReceiptProcessModel;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.SysJobNew;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 审批相关单据控制器
 * @author yanzhenxing
 * @date 2015/11/30
 */
@Controller
@RequestMapping("${adminPath}/review/receipt/")
public class ReviewReceiptController {

    @Autowired
    private ReviewReceiptService reviewReceiptService;

    @Autowired
    private SystemService systemService;

    /**
     * 单据配置主页面
     * @param model
     * @return
     */
    @RequestMapping("index")
    public String index(Model model){
        List<ReceiptProcessModel> receiptProcessModels= reviewReceiptService.obtainReceiptProcesses();
        model.addAttribute("receiptProcesses",receiptProcessModels);
        return "modules/reviewreceipt/index";
    }

    /**
     * 显示单据配置页面
     * @param receiptType
     * @return
     */
    @RequestMapping(value = "show/{receiptType}",method = RequestMethod.GET)
    public String showConfiguration(@PathVariable("receiptType")String receiptType,Model model){
        User user= UserUtils.getUser();
        String companyId=user.getCompany().getUuid();
        MultiValueMap<String,String> configurationMap= reviewReceiptService.obtainReceiptConfigurationMap(companyId,receiptType);
        List<SysJobNew> sysJobs=systemService.findByCompanyUuid(companyId);
        Map<Long,SysJobNew> sysJobNewMap=systemService.findSysJobMapByCompanyUuid(companyId);
        model.addAttribute("configurationMap",configurationMap);
        model.addAttribute("receiptType",receiptType);
        model.addAttribute("sysJobs",sysJobs);
        model.addAttribute("sysJobMap",convert(sysJobNewMap));
        return buildReceiptViewPath(receiptType);
    }

    private Map<String,SysJobNew> convert(Map<Long,SysJobNew> sysJobNewMap){
        Map<String,SysJobNew> result=new HashMap<>();
        if (sysJobNewMap!=null&&sysJobNewMap.size()>0){
            for (Long key:sysJobNewMap.keySet()){
                result.put(key.toString(),sysJobNewMap.get(key));
            }
        }

        return result;
    }

    /**
     * 保存配置
     * @param model
     * @param receiptType
     * @param configMap
     * @return
     */
    @RequestMapping(value="save",method = RequestMethod.POST)
    @ResponseBody
    public JSONObject save(Model model,@RequestParam("receiptType")String receiptType,@RequestParam("configMap")String configMapStr){

        JSONObject result=new JSONObject();

        User user= UserUtils.getUser();
        String companyId=user.getCompany().getUuid();

        Map<String,String> configMap= JSONObject.fromObject(configMapStr);
        Map<String,SysJobNew> sysJobMap=systemService.findSysJobUuidMapByCompanyUuid(companyId);

        List<ReviewReceiptConfiguration> configurations=buildReviewReceiptConfigurations(user.getId().toString(),companyId,receiptType,configMap,sysJobMap);

        reviewReceiptService.saveReviewReceiptConfigurations(user.getId().toString(),companyId,receiptType,configurations);
        result.put("code",0);

        return result;
    }

    private List<ReviewReceiptConfiguration> buildReviewReceiptConfigurations(String userId,String companyId,String receiptType,Map<String,String> configMap,Map<String,SysJobNew> sysJobMap){
        Assert.hasText(userId,"userId should not be empty!");
        Assert.hasText(companyId,"companyId should not be empty!");
        Assert.hasText(receiptType,"receiptType should not be empty!");

        List<ReviewReceiptConfiguration> result=new ArrayList<>();

        if (configMap!=null&&configMap.size()>0){
            Date currentDate=new Date();
            for (String key:configMap.keySet()) {
                String reviewerKey=configMap.get(key);
                if (StringUtils.isNotBlank(reviewerKey)){
                    if(reviewerKey.lastIndexOf(",")==(reviewerKey.length()-1)){
                        reviewerKey=reviewerKey.substring(0,reviewerKey.length()-1);
                    }
                    String[] reviewerKeys=reviewerKey.split(",");
                    for (int i=0;i<reviewerKeys.length;i++){
                        String realKey=reviewerKeys[i];
                        ReviewReceiptConfiguration reviewReceiptConfiguration=new ReviewReceiptConfiguration();
                        reviewReceiptConfiguration.setId(UuidUtils.generUuid());
                        reviewReceiptConfiguration.setCompanyId(companyId);
                        reviewReceiptConfiguration.setCreateBy(userId);
                        reviewReceiptConfiguration.setCreateDate(currentDate);
                        reviewReceiptConfiguration.setDelFlag(0);
                        reviewReceiptConfiguration.setReceiptType(receiptType);
                        reviewReceiptConfiguration.setReceiptDescription(ReviewReceiptContext.RECEIPT_TYPE_MAP.get(receiptType));
                        Integer keyI=Integer.parseInt(key);
//                reviewReceiptConfiguration.setReviewElement(key);
                        reviewReceiptConfiguration.setReviewElement(keyI);
                        reviewReceiptConfiguration.setReviewerKey(realKey);
                        if (sysJobMap!=null&&sysJobMap.containsKey(realKey)){
                            reviewReceiptConfiguration.setReviewerKeyName(sysJobMap.get(realKey).getName());
                        }
                        result.add(reviewReceiptConfiguration);
                    }
                }
            }
        }

        return result;
    }

    private String buildReceiptViewPath(String receiptType){
        Assert.hasText(receiptType,"receiptType should not be empty!");
        switch (receiptType){
            case ReviewReceiptContext.RECEIPT_TYPE_PAYMENT:
                return "modules/reviewreceipt/payment";
            case ReviewReceiptContext.RECEIPT_TYPE_REFUND:
                return "modules/reviewreceipt/refund";
            case ReviewReceiptContext.RECEIPT_TYPE_BORROW_MONEY:
                return "modules/reviewreceipt/visaBorrowMoney";
            case ReviewReceiptContext.RECEIPT_TYPE_VISA_RETURN_MONEY:
                return "modules/reviewreceipt/visaReturnMoney";
            default: return "modules/reviewreceipt/index";
        }
    }
}
