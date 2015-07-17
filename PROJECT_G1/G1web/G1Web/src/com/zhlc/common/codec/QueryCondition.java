package com.zhlc.common.codec;


/**
 * @author anquan
 * @desc: (1)从页面传入Controller的参数集(避免方法中写多个参数)
 * 		  (2)其中参数名称不规范是配置jquery UI使用
 * @date 2015-1-2 下午4:18:33
 */
public class QueryCondition {
	private Integer page; // 当前页
	private Integer rows; // 页大小
	private String sort; // 排序字段
	private String order; // 排序方式
	
	public QueryCondition() {
		super();
	}
	/**
	 * @param page
	 * @param rows
	 * @param sort
	 * @param order
	 */
	public QueryCondition(Integer page, Integer rows, String sort, String order) {
		super();
		this.page = page;
		this.rows = rows;
		this.sort = sort;
		this.order = order;
	}
	public Integer getPage() {
		return page;
	}
	public void setPage(Integer page) {
		if(page == null || page == 0){
			this.page = 1;
		}else{
			this.page = page;
		}
	}
	public Integer getRows() {
		return rows;
	}
	public void setRows(Integer rows) {
		if(page == null || page == 0){
			this.rows = 15;
		}else{
			this.rows = rows;
		}
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
}
