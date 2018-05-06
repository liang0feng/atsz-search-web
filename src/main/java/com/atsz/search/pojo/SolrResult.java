package com.atsz.search.pojo;

import java.util.List;

public class SolrResult {
    
    private Integer curPage;//当前页
    
    private Integer pageCount;//总页数
    
    private Integer recordCount;//总记录数

    private List<Product> productList;//结果集

	public Integer getCurPage() {
		return curPage;
	}

	public void setCurPage(Integer curPage) {
		this.curPage = curPage;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public Integer getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(Integer recordCount) {
		this.recordCount = recordCount;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public SolrResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SolrResult(Integer curPage, Integer pageCount, Integer recordCount,
			List<Product> productList) {
		super();
		this.curPage = curPage;
		this.pageCount = pageCount;
		this.recordCount = recordCount;
		this.productList = productList;
	}

	@Override
	public String toString() {
		return "SolrResult [curPage=" + curPage + ", pageCount=" + pageCount
				+ ", recordCount=" + recordCount + ", productList="
				+ productList + "]";
	}
    
}
