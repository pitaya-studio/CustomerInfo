package com.trekiz.admin.modules.rebatesupplier.web;

import com.quauq.review.core.utils.IdGenerator;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.rebatesupplier.entity.RebateSupplier;
import com.trekiz.admin.modules.rebatesupplier.service.RebateSupplierService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.PlatBankService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 返佣供应商管理controller
 * @author yanzhenxing
 * @date 2016/1/7
 */
@Controller
@RequestMapping(value="${adminPath}/rebatesupplier/manager")
public class RebateSupplierController {

    @Autowired
    private RebateSupplierService rebateSupplierService;

    @Autowired
    private AgentinfoService agentinfoService;

    @Autowired
    private PlatBankService platBankService;

    /**
     * 根据供应商id获取银行信息
     * @param supplierId
     * @return
     */
    @RequestMapping("bankinfo/{supplierId}")
    @ResponseBody
    public Map<String,List<Map<String,String>>> obtainBankInfo(@PathVariable("supplierId") String supplierId){

        List<PlatBankInfo> bankInfos=platBankService.obtainBanks(Long.parseLong(supplierId), Context.PLAT_TYPE_SUP);

        return assembleBankInfos(bankInfos);
    }

    /**
     * 组装银行信息
     * @param bankInfos
     * @return
     */
    private Map<String,List<Map<String,String>>> assembleBankInfos(List<PlatBankInfo> bankInfos){
        Map<String,List<Map<String,String>>> result=new HashMap<>();
        //增加境内境外key
        result.put("domestic",new ArrayList<Map<String, String>>());
        result.put("overseas",new ArrayList<Map<String, String>>());
        if(bankInfos!=null&&bankInfos.size()>0){
            for (PlatBankInfo bankInfo : bankInfos) {
                List<Map<String, String>> bnakList=null;
                if (PlatBankInfo.BELONG_TYPE_DOMESTIC.equals(bankInfo.getBelongType())){//境内账户
                    bnakList=result.get("domestic");
                }else if(PlatBankInfo.BELONG_TYPE_OVERSEAS.equals(bankInfo.getBelongType())){//境外账号
                    bnakList=result.get("overseas");
                }
                if (bnakList!=null){
                    Map<String, String> bankMap=new HashMap<>();
                    bankMap.put("id",bankInfo.getId().toString());
                    bankMap.put("bankName",bankInfo.getBankName());
                    bankMap.put("bankAccountCode",bankInfo.getBankAccountCode());
                    bnakList.add(bankMap);
                }
            }
        }
        return result;
    }

    /**
     * 进入新增页面
     * @param model
     * @return
     */
    @RequestMapping(value="firstForm",method= RequestMethod.GET)
    public String firstForm(Model model,@RequestParam(required = false,value = "id") String id){

        User user= UserUtils.getUser();
        Long companyId=user.getCompany().getId();

        //判断是否是进入修改页面
        if (StringUtils.isNotBlank(id)){
            model.addAttribute("supplier",rebateSupplierService.obtain(Long.parseLong(id)));
        }
        model.addAttribute("supplierOperators",agentinfoService.findInnerOperator(companyId));

        return "modules/rebatesupplier/firstForm";
    }

    /**
     * 第一步：保存基本信息
     * @param rebateSupplier
     * @param model
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "saveFirstForm",method = RequestMethod.POST)
    public String saveFirstForm(RebateSupplier rebateSupplier,Model model, RedirectAttributes redirectAttributes) {

        User user= UserUtils.getUser();
        Long companyId=user.getCompany().getId();
        String companyUuid=user.getCompany().getUuid();

        RebateSupplier savedRebateSupplier=null;

        //判断是否是新增还是修改
        Long supplierId=rebateSupplier.getId();
        if(supplierId!=null){//修改基本信息
            RebateSupplier supplier= rebateSupplierService.obtain(supplierId);
            //更新各个域的值
            supplier.setAddress(rebateSupplier.getAddress());
            supplier.setBrand(rebateSupplier.getBrand());
            supplier.setDescription(rebateSupplier.getDescription());
            supplier.setEmail(rebateSupplier.getEmail());
            supplier.setEnName(rebateSupplier.getEnName());
            supplier.setFax(rebateSupplier.getFax());
            supplier.setName(rebateSupplier.getName());
            if (rebateSupplier.getOperatorId()!=null&&!rebateSupplier.getOperatorId().equals(supplier.getOperatorId())){
                User operator=UserUtils.getUser(rebateSupplier.getOperatorId());
                if (operator!=null){
                    supplier.setOperatorName(operator.getName());
                }
            }
            supplier.setOperatorId(rebateSupplier.getOperatorId());
            supplier.setPersonInCharge(rebateSupplier.getPersonInCharge());
            supplier.setPostcode(rebateSupplier.getPostcode());
            supplier.setTelephone(rebateSupplier.getTelephone());
            supplier.setUpdateBy(user.getId());
            supplier.setUpdateDate(new Date());
            try {
            	savedRebateSupplier=rebateSupplierService.save(supplier);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }else{
            rebateSupplier.setUuid(IdGenerator.generate32BitUuid());
            rebateSupplier.setCompanyId(companyId);
            rebateSupplier.setCompanyUuid(companyUuid);
            rebateSupplier.setCreateBy(user.getId());
            rebateSupplier.setCreateDate(new Date());
            rebateSupplier.setStatus(RebateSupplier.STATUS_DRAFT);
            if (rebateSupplier.getOperatorId()!=null){
                User operator=UserUtils.getUser(rebateSupplier.getOperatorId());
                if (operator!=null){
                    rebateSupplier.setOperatorName(operator.getName());
                }
            }
            try {
            	savedRebateSupplier=rebateSupplierService.save(rebateSupplier);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

        //保存供应商ID
        model.addAttribute("id", savedRebateSupplier.getId());
        addMessage(redirectAttributes, "保存供应商'" + savedRebateSupplier.getName() + "'成功");
        // 获取银行账户
        List<String> banks = platBankService.obtainBankInfos(savedRebateSupplier.getId(),Context.PLAT_TYPE_SUP);
        if (null != banks && banks.size() > 0) {
            model.addAttribute("banks", banks);
        }

        return "modules/rebatesupplier/secondForm";
    }

    /**
     * 保存银行账号信息，并保存供应商的提交状态
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "savePlatBankInfoForm")
    public String savePlatBankInfoForm(HttpServletRequest request, Model model) {
        String[] belongTypes = request.getParameterValues("belongType");// 所属类型
        String[] defaultFlags = request.getParameterValues("realDefaultFlag");// 默认选择
        String[] accountNames = request.getParameterValues("accountName");// 账户名
        String[] bankNames = request.getParameterValues("bankName");// 开户行名称
        String[] bankAddrs = request.getParameterValues("bankAddr");// 银行地址
        String[] bankAccountCodes = request.getParameterValues("bankAccountCode");// 银行账户
        String[] rountings = request.getParameterValues("Rounting");
        String[] swiftNums = request.getParameterValues("swiftNum");
        String[] phoneNums = request.getParameterValues("phoneNum");
        String[] remarks = request.getParameterValues("remarks");

        String id = request.getParameter("id");
        model.addAttribute("id", id);

        // 获取belongType（1 境内账户 2 境外账户）
        String belongTypeStr = request.getParameter("belongType");
        model.addAttribute("belongTypeStr", belongTypeStr);

        rebateSupplierService.saveBankInfoAndSubmit(Long.parseLong(id), Context.PLAT_TYPE_SUP,belongTypes,defaultFlags,accountNames,
                bankNames,bankAddrs,bankAccountCodes, rountings, swiftNums,phoneNums,remarks);

        return "redirect:" + Global.getAdminPath() + "/rebatesupplier/manager/list";
    }

    /**
     * 列表页
     * @param model
     * @return
     */
    @RequestMapping(value = {"list",""},method = {RequestMethod.GET,RequestMethod.POST})
    public String list(Model model, HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "name", required = false) String name, @RequestParam(value = "operatorId", required = false) String operatorId){
        User user= UserUtils.getUser();
        Long companyId=user.getCompany().getId();
        Map<String,String> queryParams=new HashMap<>();
        /**
         * 构造查询参数
         */
        queryParams.put("companyId",companyId.toString());
        if (StringUtils.isNotBlank(name)){
            queryParams.put("name",name);
            model.addAttribute("name",name);
        }
        if (StringUtils.isNotBlank(operatorId)){
            queryParams.put("operatorId",operatorId);
            model.addAttribute("operatorId",operatorId);
        }

        Page<Map<Object, Object>> supplierPage =rebateSupplierService.getSupplierPage(new Page<Map<Object, Object>>(request,response),queryParams);
        model.addAttribute("operators",agentinfoService.findInnerOperator(companyId));
        model.addAttribute("page",supplierPage);

        return "modules/rebatesupplier/supplierList";
    }

    /**
     * 详情页
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "detail/{id}",method = RequestMethod.GET)
    public String detail(@PathVariable String id, Model model){

        RebateSupplier supplier=rebateSupplierService.obtain(Long.parseLong(id));
        if (supplier!=null){
            List<PlatBankInfo> platBankInfos=platBankService.obtainBanks(Long.parseLong(id),Context.PLAT_TYPE_SUP);
            model.addAttribute("supplier",supplier);
            model.addAttribute("domesticBanks",obtainClassifiedBanks(platBankInfos,1L));
            model.addAttribute("overseasBanks",obtainClassifiedBanks(platBankInfos,2L));
        }

        return "modules/rebatesupplier/detail";
    }

    /**
     * 根据id删除供应商
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value="delete/{id}")
    public String delete(@PathVariable String id,Model model){
        User user=UserUtils.getUser();
        rebateSupplierService.delete(Long.parseLong(id),user.getId());
        return "redirect:" + Global.getAdminPath() + "/rebatesupplier/manager/list";
    }

    /**
     * 获取不同类型的银行账户
     * @param banks
     * @param type
     * @return
     */
    private List<PlatBankInfo> obtainClassifiedBanks(List<PlatBankInfo> banks,Long type){
        List<PlatBankInfo> bankInfos=new ArrayList<>();
        if (banks!=null&&banks.size()>0){
            for (PlatBankInfo bank : banks) {
                if (bank.getBelongType()==type){//根据境内境外分类
                    bankInfos.add(bank);
                }
            }
        }
        return bankInfos;
    }

    /**
     * 添加Flash消息
     */
    protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
        StringBuilder sb = new StringBuilder();
        for (String message : messages){
            sb.append(message).append(messages.length>1?"<br/>":"");
        }
        redirectAttributes.addFlashAttribute("message", sb.toString());
    }
}
