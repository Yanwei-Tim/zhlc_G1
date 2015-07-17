package com.zhlc.common.codec;


import java.util.HashMap;
import java.util.Map;

/**
 * @author anquan
 * @Desc 查询参数
 * @date 2014-9-12 下午03:34:06
 */
public class SearchDTO {

	/**
	 * 当前页
	 */
	private int currentPage;
	/**
	 * 页大小
	 */
	private int pageSize;
	/**
	 * 排序字段名
	 */
	private String sortName = "";
	/**
	 * 排序方式
	 */
	private String sortOrder = "asc";
	
	
	private Object obj;//查询条件对象

	private Map<String, Object> filters = new HashMap<String, Object>();//查询条件

	private int total = 0; //总记录数
	
	private int startIndex;//起始索引(sql中用到)

	public SearchDTO() {}

	public SearchDTO(int currentPage, int pageSize, String sortName, String sortOrder, Object obj) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.sortName = sortName;
		this.sortOrder = sortOrder;
		this.obj = obj;
	}
	
	public SearchDTO(int currentPage, int pageSize, String sortName, String sortOrder, Map<String, Object> filters) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.sortName = sortName;
		this.sortOrder = sortOrder;
		this.filters = filters;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getStartIndex() {
		return startIndex;
	}
	
	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	/**
	 * 根据currentPage和pageSize计算当前页第一条记录在总结果集中的索引位置.
	 * @return
	 */
	public void setStartIndex(int startIndex) {
		if (currentPage < 1 || pageSize < 1){
			startIndex = 0;
		} else {
			startIndex = ((currentPage - 1) * pageSize) < total? ((currentPage - 1) * pageSize) : total;
		}
		this.startIndex = startIndex;
	}

	public Object getObj() {
		return obj;
	}
	
	public void setObj(Object obj) {
		this.obj = obj;
	}

}
