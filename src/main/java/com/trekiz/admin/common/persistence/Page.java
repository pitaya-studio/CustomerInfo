/**

 *

 */
package com.trekiz.admin.common.persistence;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页类
 * @author zj
 * @version 2013-11-19
 * @param <T>
 */
public class Page<T> {

	private int pageNo = 1; // 当前页码
	private int pageSize = Integer.valueOf(Global.getConfig("page.pageSize")); // 页面大小，设置为“-1”表示不进行分页（分页无效）

	private long count;// 总记录数，设置为“-1”表示不查询总数

	private int first;// 首页索引
	private int last;// 尾页索引
	private int prev;// 上一页索引
	private int next;// 下一页索引

	private boolean firstPage;//是否是第一页
	private boolean lastPage;//是否是最后一页

	private int length = 8;// 显示页面长度
	private int slider = 1;// 前后显示页面长度

	private List<T> list = new ArrayList<T>();

	private String orderBy = ""; // 标准查询有效， 实例： updatedate desc, name asc

	private String funcName = "page"; // 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。

	private String message = ""; // 设置提示消息，显示在“共n条”之后
	
	private String toStringFlag = "default"; // 默认为0

	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 */
	public Page(HttpServletRequest request, HttpServletResponse response){
		this(request, response, -2);
	}

	/**
	 * 构造方法
	 * @param request 传递 repage 参数，来记住页码
	 * @param response 用于设置 Cookie，记住页码
	 * @param pageSize 分页大小，如果传递 -1 则为不分页，返回所有数据
	 */
	public Page(HttpServletRequest request, HttpServletResponse response, int pageSize){
		// 设置页码参数（传递repage参数，来记住页码）
		String no = request.getParameter("pageNo");
		int num = 1;
		if (StringUtils.isNumeric(no)){
			try{
				num = Integer.parseInt(no);
			}catch (NumberFormatException e){
				num = 1;
			}
			CookieUtils.setCookie(response, "pageNo", String.valueOf(num));
			this.setPageNo(num);
		}else if (request.getParameter("repage")!=null){
			no = CookieUtils.getCookie(request, "pageNo");
			if (StringUtils.isNumeric(no)){
				try{
					num = Integer.parseInt(no);
				}catch (NumberFormatException e){
					num = 1;
				}
				this.setPageNo(num);
			}
		}
		// 设置页面大小参数（传递repage参数，来记住页码大小）
		String size = request.getParameter("pageSize");
		int pageSizeNum = pageSize;
		if (StringUtils.isNumeric(size)){
			try{
				pageSizeNum = Integer.parseInt(size);
			}catch (NumberFormatException e){
				pageSizeNum = pageSize;
			}
			CookieUtils.setCookie(response, "pageSize", String.valueOf(pageSizeNum));
			this.setPageSize(pageSizeNum);
		}else if (request.getParameter("repage")!=null){
			size = CookieUtils.getCookie(request, "pageSize");
			if (StringUtils.isNumeric(size)){
				try{
					pageSizeNum = Integer.parseInt(size);
				}catch (NumberFormatException e){
					pageSizeNum = pageSize;
				}
				this.setPageSize(pageSizeNum);
			}
		}
		if (pageSize != -2){
			this.pageSize = pageSize;
		}
		// 设置排序参数
		String orderBy = request.getParameter("orderBy");
		if (StringUtils.isNotBlank(orderBy)){
			this.setOrderBy(orderBy);
		}
	}

	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 */
	public Page(int pageNo, int pageSize) {
		this(pageNo, pageSize, 0);
	}

	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 */
	public Page(int pageNo, int pageSize, long count) {
		this(pageNo, pageSize, count, new ArrayList<T>());
	}

	/**
	 * 构造方法
	 * @param pageNo 当前页码
	 * @param pageSize 分页大小
	 * @param count 数据条数
	 * @param list 本页数据对象列表
	 */
	public Page(int pageNo, int pageSize, long count, List<T> list) {
		this.setCount(count);
		this.setPageNo(pageNo);
		this.pageSize = pageSize;
		this.setList(list);
	}
	
	/**
	 * 初始化参数
	 */
	public void initialize(){
				
		//1
		this.first = 1;
		
		this.last = (int)(count / (this.pageSize < 1 ? 20 : this.pageSize) + first - 1);
		
		if (this.count % this.pageSize != 0 || this.last == 0) {
			this.last++;
		}

		if (this.last < this.first) {
			this.last = this.first;
		}
		
		if (this.pageNo <= 1) {
			this.pageNo = this.first;
			this.firstPage=true;
		}

		if (this.pageNo >= this.last) {
			this.pageNo = this.last;
			this.lastPage=true;
		}

		if (this.pageNo < this.last - 1) {
			this.next = this.pageNo + 1;
		} else {
			this.next = this.last;
		}

		if (this.pageNo > 1) {
			this.prev = this.pageNo - 1;
		} else {
			this.prev = this.first;
		}
		
		//2
		if (this.pageNo < this.first) {// 如果当前页小于首页
			this.pageNo = this.first;
		}

		if (this.pageNo > this.last) {// 如果当前页大于尾页
			this.pageNo = this.last;
		}
		
	}
	
	

/**
	 * 默认输出当前分页标签 
	 * <div class="page">${page}</div>
	 * @since 调整显示格式 on 2015-05-19
	 */
	@Override
	public String toString() {

		initialize();
		
		StringBuilder sb = new StringBuilder();
		
		if ("default".equals(toStringFlag)) {
			defaultToString(sb);
		} else if ("t1ForProductAbove".equals(toStringFlag)) {
			t1ToString(sb);
		} else if ("t1ForProductBottom".equals(toStringFlag)) {
			t14t2ToString(sb);
		}
		return sb.toString();
	}
	
	private void defaultToString(StringBuilder sb) {
		if (pageNo == first) {// 如果是首页
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">&#171; 上一页</a></li>\n");
		} else {
			sb.append("<li><a href=\"javascript:"+funcName+"("+prev+","+pageSize+");\">&#171; 上一页</a></li>\n");
		}

		int begin = pageNo - (length / 2);

		if (begin < first) {
			begin = first;
		}

		int end = begin + length - 1;

		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
			if (i < begin) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first)
						+ "</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
		}

		if (last - end > slider) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			end = last - slider;
		}

		for (int i = end + 1; i <= last; i++) {
			sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
					+ (i + 1 - first) + "</a></li>\n");
		}

		if (pageNo == last) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">下一页 &#187;</a></li>\n");
		} else {
			sb.append("<li><a href=\"javascript:"+funcName+"("+next+","+pageSize+");\">"
					+ "下一页 &#187;</a></li>\n");
		}

		sb.append("<li class=\"disabled controls\"><a href=\"javascript:\">第 ");
		sb.append("<input type=\"text\" value=\""+pageNo+"\" onkeypress=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13)");
		sb.append(funcName+"(this.value,"+pageSize+");\" onclick=\"this.select();\"/> / ");
		sb.append((count%pageSize==0)?(count/pageSize):(count/pageSize+1));
		sb.append(" 页 ， 每页 ");
		sb.append("<input type=\"text\" value=\""+pageSize+"\" onkeypress=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13)");
		sb.append(funcName+"("+pageNo+",this.value);\" onclick=\"this.select();\"/> 条，");
		sb.append("共 " + count + " 条"+(message!=null?message:"")+"</a></li>\n");

		sb.insert(0,"<ul>\n").append("</ul>\n");
		
		sb.append("<div style=\"clear:both;\"></div>");
	}
	
	private void t1ToString(StringBuilder sb) {
		sb.append("<li><span class=\"total\">共" + count + "条</span></li>");
		if (pageNo == first) {// 如果是首页
			sb.append("<li class=\"disabled\"><span href=\"javascript:\" style=\"padding: 0;border: 0;\"><em class=\"fa fa-angle-left_own \" style=\"margin: 0;\"></em></span></li>");
		} else {
			sb.append("<li><span onclick=\"javascript:"+funcName+"("+prev+","+pageSize+");\" style=\"padding: 0;border: 0;\"><em class=\"fa fa-angle-left_own \" style=\"margin: 0;\"></em></span></li>");
		}
		
		int begin = pageNo - (length / 2);

		if (begin < first) {
			begin = first;
		}

		int end = begin + length - 1;

		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<li><span onclick=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</span></li>\n");
			}
			if (i < begin) {
				sb.append("<li class=\"disabled\"><span onclick=\"javascript:\">...</span></li>\n");
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<li class=\"active\"><span onclick=\"javascript:\">" + (i + 1 - first)
						+ "</span></li>\n");
			} else {
				sb.append("<li><span onclick=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</span></li>\n");
			}
		}

		if (last - end > slider) {
			sb.append("<li class=\"disabled\"><span onclick=\"javascript:\">...</span></li>\n");
			end = last - slider;
		}

		for (int i = end + 1; i <= last; i++) {
			sb.append("<li><span onclick=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
					+ (i + 1 - first) + "</span></li>\n");
		}

		if (pageNo == last) {
			sb.append("<li class=\"disabled\"><span onclick=\"javascript:\" style=\"padding: 0;border: 0;\"><em class=\"fa fa-angle-right_own \" style=\"margin: 0;\"></em></span></li>");
		} else {
			sb.append("<li><span onclick=\"javascript:"+funcName+"("+next+","+pageSize+");\" style=\"padding: 0;border: 0;\"><em class=\"fa fa-angle-right_own \" style=\"margin: 0;\"></em></span></li>");
		}

		sb.append("<li><span class=\"skip\" href=\"javascript:page(2,10);\">跳至 <input type=\"text\" onkeydown=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13)" + funcName+"(this.value,"+pageSize+");\" onkeypress=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13)" + funcName+"(this.value,"+pageSize+");\"/>页</span></li>")
				.append("<li><span class=\"skip\" href=\"javascript:void(0);\">每页 <input type=\"text\" value=\""+pageSize+"\" onkeydown=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13)" + funcName+"("+pageNo+",this.value);\" onkeypress=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13)" + funcName+"("+pageNo+",this.value);\" onclick=\"this.select();\" />条</span></li>");

		sb.insert(0,"<ul>\n").append("</ul>\n");
		
		sb.append("<div style=\"clear:both;\"></div>");

	}
	
	private void t14t2ToString(StringBuilder sb) {
		sb.append("<span class=\"rank_product_right\">共 <span class=\"orange font_14\">" + count + "</span> 条</span>");
		if (pageNo == first) {// 如果是首页
			sb.append("<em class=\"orange_left t1_2\"></em>");
		} else {
			sb.append("<em class=\"orange_left t1_2\" onclick=\"javascript:"+funcName+"("+prev+","+pageSize+");\" ></em>");
		}

		int begin = pageNo - (length / 2);

		if (begin < first) {
			begin = first;
		}

		int end = begin + length - 1;

		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		long totalPageSize = (count%pageSize==0)?(count/pageSize):(count/pageSize+1);
		sb.append("<span class=\"orange\">" + pageNo + "</span><span>/</span><span>" + totalPageSize + "</span>");

		if (pageNo == last) {
			sb.append("<em class=\"orange_right t1_2\"></em>");
		} else {
			sb.append("<em class=\"orange_right t1_2\" onclick=\"javascript:"+funcName+"("+next+","+pageSize+");\" ></em>");
		}
	}
	
	/**
	 * 默认输出当前分页标签 
	 * <div class="page">${page}</div>
	 */
	
	public String toString_old() {

		initialize();
		
		StringBuilder sb = new StringBuilder();
		
		if (pageNo == first) {// 如果是首页
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">&#171; 上一页</a></li>\n");
		} else {
			sb.append("<li><a href=\"javascript:"+funcName+"("+prev+","+pageSize+");\">&#171; 上一页</a></li>\n");
		}

		int begin = pageNo - (length / 2);

		if (begin < first) {
			begin = first;
		}

		int end = begin + length - 1;

		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
			if (i < begin) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first)
						+ "</a></li>\n");
			} else {
				sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
		}

		if (last - end > slider) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			end = last - slider;
		}

		for (int i = end + 1; i <= last; i++) {
			sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
					+ (i + 1 - first) + "</a></li>\n");
		}

		if (pageNo == last) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">下一页 &#187;</a></li>\n");
		} else {
			sb.append("<li><a href=\"javascript:"+funcName+"("+next+","+pageSize+");\">"
					+ "下一页 &#187;</a></li>\n");
		}

		sb.append("<li class=\"disabled controls\"><a href=\"javascript:\">当前 ");
		sb.append("<input type=\"text\" value=\""+pageNo+"\" onkeypress=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13)");
		sb.append(funcName+"(this.value,"+pageSize+");\" onclick=\"this.select();\"/> / ");
		sb.append("<input type=\"text\" value=\""+pageSize+"\" onkeypress=\"var e=window.event||event||this;var c=e.keyCode||e.which;if(c==13)");
		sb.append(funcName+"("+pageNo+",this.value);\" onclick=\"this.select();\"/> 条，");
		sb.append("共 " + count + " 条"+(message!=null?message:"")+"</a></li>\n");

		sb.insert(0,"<ul>\n").append("</ul>\n");
		
		sb.append("<div style=\"clear:both;\"></div>");

//		sb.insert(0,"<div class=\"page\">\n").append("</div>\n");
		
		return sb.toString();
	}
	
	/**
	 * @author LiuXueLiang
	 * 新风格的分页（完成80%）
	 * @param type
	 * @return
	 */
	public String toString(String type) {

		initialize();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("<div class=\"page_ou\">\n<div class=\"paginations\">\n<div class=\"jump\">\n每页显示\n<select>");
		sb.append("<option>10</option>\n");
		sb.append("<option>20</option>\n");
		sb.append("</select>\n条\n</div>");
	
		sb.append("<div class=\"jump\">\n到第\n<input type=\"text\" class=\"\" /> 页\n<span class=\"btn_common\">确定</span>\n</div>");
		sb.append("<div class=\"jump\">\n共"+count+"条\n</div>");
		sb.append("<ul>\n");
		if (pageNo == first) {// 如果是首页
			sb.append("<li class=\"disabled\">\n<a href=\"javascript:\"><上一页</a></li>\n");
		} else {
			sb.append("<li>\n<a href=\"javascript:"+funcName+"("+prev+","+pageSize+");\"><上一页</a></li>\n");
		}

		int begin = pageNo - (length / 2);

		if (begin < first) {
			begin = first;
		}

		int end = begin + length - 1;

		if (end >= last) {
			end = last;
			begin = end - length + 1;
			if (begin < first) {
				begin = first;
			}
		}

		if (begin > first) {
			int i = 0;
			for (i = first; i < first + slider && i < begin; i++) {
				sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a></li>\n");
			}
			if (i < begin) {
				sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>\n");
			}
		}

		for (int i = begin; i <= end; i++) {
			if (i == pageNo) {
				sb.append("<li class=\"active\"><a href=\"javascript:\">" + (i + 1 - first)
						+ "</a></li>");
			} else {
				sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
						+ (i + 1 - first) + "</a></li>");
			}
		}

		if (last - end > slider) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">...</a></li>");
			end = last - slider;
		}

		for (int i = end + 1; i <= last; i++) {
			sb.append("<li><a href=\"javascript:"+funcName+"("+i+","+pageSize+");\">"
					+ (i + 1 - first) + "</a></li>");
		}

		if (pageNo == last) {
			sb.append("<li class=\"disabled\"><a href=\"javascript:\">下一页></a></li>\n");
		} else {
			sb.append("<li><a href=\"javascript:"+funcName+"("+next+","+pageSize+");\">"
					+ "下一页></a></li>\n");
		}
		sb.append("</ul>\n");
		
		sb.append("<div style=\"clear:both;\"></div>");
		sb.append("</div>\n");
		sb.append("</div>");
		
		return sb.toString();
	}
	
	
	
	
//	public static void main(String[] args) {
//		Page<String> p = new Page<String>(3, 3);
//		System.out.println(p);
//		System.out.println("首页："+p.getFirst());
//		System.out.println("尾页："+p.getLast());
//		System.out.println("上页："+p.getPrev());
//		System.out.println("下页："+p.getNext());
//	}

	/**
	 * 获取设置总数
	 * @return
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 设置数据总数
	 * @param count
	 */
	public void setCount(long count) {
		this.count = count;
		if (pageSize >= count){
			pageNo = 1;
		}
	}
	
	/**
	 * 获取当前页码
	 * @return
	 */
	public int getPageNo() {
		return pageNo;
	}
	
	/**
	 * 设置当前页码
	 * @param pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	/**
	 * 获取页面大小
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置页面大小（最大500）
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize <= 0 ? 10 : pageSize > 500 ? 500 : pageSize;
	}

	/**
	 * 首页索引
	 * @return
	 */
	public int getFirst() {
		return first;
	}

	/**
	 * 尾页索引
	 * @return
	 */
	public int getLast() {
		return last;
	}
	
	/**
	 * 获取页面总数
	 * @return getLast();
	 */
	public int getTotalPage() {
		return getLast();
	}

	/**
	 * 是否为第一页
	 * @return
	 */
	public boolean isFirstPage() {
		return firstPage;
	}

	/**
	 * 是否为最后一页
	 * @return
	 */
	public boolean isLastPage() {
		return lastPage;
	}
	
	/**
	 * 上一页索引值
	 * @return
	 */
	public int getPrev() {
		if (isFirstPage()) {
			return pageNo;
		} else {
			return pageNo - 1;
		}
	}

	/**
	 * 下一页索引值
	 * @return
	 */
	public int getNext() {
		if (isLastPage()) {
			return pageNo;
		} else {
			return pageNo + 1;
		}
	}
	
	/**
	 * 获取本页数据对象列表
	 * @return List<T>
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * 设置本页数据对象列表
	 * @param list
	 */
	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * 获取查询排序字符串
	 * @return
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置查询排序，标准查询有效， 实例： updatedate desc, name asc
	 */
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 获取点击页码调用的js函数名称
	 * function ${page.funcName}(pageNo){location="${ctx}/list-${category.id}${urlSuffix}?pageNo="+i;}
	 * @return
	 */
	public String getFuncName() {
		return funcName;
	}

	/**
	 * 设置点击页码调用的js函数名称，默认为page，在一页有多个分页对象时使用。
	 * @param funcName 默认为page
	 */
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	/**
	 * 设置提示消息，显示在“共n条”之后
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 分页是否有效
	 * @return this.pageSize==-1
	 */
	public boolean isDisabled() {
		return this.pageSize==-1;
	}
	
	/**
	 * 是否进行总数统计
	 * @return this.count==-1
	 */
	public boolean isNotCount() {
		return this.count==-1;
	}
	
	/**
	 * 获取 Hibernate FirstResult
	 */
	public int getFirstResult(){
		int firstResult = (getPageNo() - 1) * getPageSize();
		if (firstResult >= getCount()) {
			firstResult = 0;
		}
		return firstResult;
	}
	/**
	 * 获取 Hibernate MaxResults
	 */
	public int getMaxResults(){
		return getPageSize();
	}
	
	/**
	 * 设置最大值，用于Excel导出 add by shijun.liu 2015.12.11
	 */
	public void setMaxSize(int size){
		this.pageSize = size;
	}

	/**
	 * 获取 Spring data JPA 分页对象
	 */
	public Pageable getSpringPage(){
		List<Order> orders = new ArrayList<Order>();
		if (orderBy!=null){
			for (String order : StringUtils.split(orderBy, ",")){
				String[] o = StringUtils.split(order, " ");
				if (o.length==1){
					orders.add(new Order(Direction.ASC, o[0]));
				}else if (o.length==2){
					if ("DESC".equals(o[1].toUpperCase())){
						orders.add(new Order(Direction.DESC, o[0]));
					}else{
						orders.add(new Order(Direction.ASC, o[0]));
					}
				}
			}
		}
		return new PageRequest(this.pageNo - 1, this.pageSize, new Sort(orders));
	}
	
	/**
	 * 设置 Spring data JPA 分页对象，转换为本系统分页对象
	 */
	public void setSpringPage(org.springframework.data.domain.Page<T> page){
		this.pageNo = page.getNumber();
		this.pageSize = page.getSize();
		this.count = page.getTotalElements();
		this.list = page.getContent();
	}
	
	
	
	/**
	 * private int length = 8;// 显示页面长度
	 * private int slider = 1;// 前后显示页面长度
	 * 
	 * @Description: 设置页面分页的显示样式
	 * @author xinwei.wang
	 * @date 2015年12月1日下午8:02:17    
	 * @throws
	 */
	public void setLength(int length){
		this.length = length;
	}

	public String getToStringFlag() {
		return toStringFlag;
	}

	public void setToStringFlag(String toStringFlag) {
		this.toStringFlag = toStringFlag;
	}
	
}
