package com.jayce.comm.util;

import java.util.List;

public class PageHelper<T> {
	private Integer rows;

	private Integer totalRecord;

	private Integer totalPage;

	private Integer currentPage;

	private List<T> result;

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public PageHelper(Integer pageNo, Integer rows, List<T> sourceList) {
		if (sourceList == null) {
			return;
		}
		// 总记录数
		this.totalRecord = sourceList.size();

		// 每页显示多小条数据
		this.rows = rows;

		// 总页数
		this.totalPage = this.totalRecord % this.rows == 0 ? this.totalRecord / this.rows
				: this.totalRecord / this.rows + 1;

		// 当前第几页
		if (this.totalPage < pageNo) {
			this.currentPage = this.totalPage;
		} else {
			this.currentPage = pageNo;
		}

		// 起始索引
		Integer fromIndex = this.rows * (this.currentPage - 1);

		// 结束索引
		Integer endIndex = null;
		if (this.rows * this.currentPage > this.totalRecord) {
			endIndex = this.totalRecord;
		} else {
			endIndex = this.rows * this.currentPage;
		}

		this.result = sourceList.subList(fromIndex, endIndex);
	}
}
