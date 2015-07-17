package com.zhlc.common.codec;

import java.util.List;

/**
 * @author anquan
 * @Desc 分页对象
 * @date 2014-9-12 下午03:34:06
 */
public class Page{
	
	/**
	 * 以下三个参数用来操作sql的必需参数,不能删减
	 */
	private int currentPage = 1; //当前页
	private int pageSize = 15; //每页显示数
	private int pageCount = 0; //总页数
	
	/**
	 * 使用jquery分页,只用到这两个参数,其他的都是内部封装了计算方法
	 */
	private int total = 0; //总记录数
	private List<?> rows = null; //结果集
	
	public Page() {}
	
	/**
	 * @param currentPage
	 *            当前页
	 * @param pageSize
	 *            页大小
	 * @param total
	 *            总记录数
	 * @param rows
	 *            分页数据
	 */
	public Page(int currentPage, int pageSize, int total, List<?> rows) {
		setCurrentPage(currentPage);
		setPageSize(pageSize);
		setTotal(total);
		setRows(rows);
	}

	/**
	 * 当前页
	 * @return
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(final int currentPage) {
		if (currentPage < 1) {
			this.currentPage = 1;
		} else {
			this.currentPage = currentPage;
		}
	}
	
	/**
	 * 页大小
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(final int pageSize) {
		if (pageSize < 1) {
			this.pageSize = 1;
		} else {
			this.pageSize = pageSize;
		}
	}
	
	/**
	 * 设置总记录数.
	 * @return
	 */
	public int getTotal() {
		return total;
	}
	public void setTotal(final int total) {
		this.total = total;
		//分页可能传入错误页码，如果传入页数 >总页数，默认设置为最大页
		pageCount = getPageCount();//传入总记录数后，可以计算出总页数
		if (currentPage > pageCount) {
			currentPage = pageCount;
		}
	}
	
	/**
	 * 计算总页数.
	 * @return
	 */
	public int getPageCount() {
		if (total < 1) {
			return 0;
		}
		return (total - 1) / pageSize + 1;
	}

	/**
	 * 取得页内的记录列数.
	 * @return
	 */
	public List<?> getRows() {
		return rows;
	}

	public void setRows(final List<?> rows) {
		this.rows = rows;
	}
}
