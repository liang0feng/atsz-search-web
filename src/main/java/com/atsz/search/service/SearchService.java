package com.atsz.search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atsz.search.pojo.Product;
import com.atsz.search.pojo.SolrResult;

@Service
public class SearchService {
	@Autowired
	private HttpSolrServer httpSolrService;

	public SolrResult doSearch(String queryString, String cid, String price,String page,
			Integer sort) throws Exception {
		
		try {
			Long minPrice = 0L;
			Long maxPrice = Long.MAX_VALUE;

			String[] split = StringUtils.split(price, "-");
			if (split != null && split.length == 2) {
				minPrice = Long.parseLong(split[0]);
				maxPrice = Long.parseLong(split[1]);
			}
			
			SolrQuery params = new SolrQuery();
			//设置查询条件
			if (queryString == null || "".equals(queryString)) {
				queryString = "";
			}
			if (cid == null || "".equals(cid)) {
				cid = "";
			}
			if (page == null || "".equals(page)) {
				page = "1";
			}
			params.setQuery(queryString);
			//设置过滤条件
//			params.setFilterQueries("price:["+minPrice +" To "+maxPrice+"]");
//			params.setFilterQueries("cid:"+cid);
			//设置排序
			params.setSort("id", ORDER.desc);
			//设置分页显示条数
			params.setStart(0);
			params.setRows(20);
			//设置高亮显示
			params.setHighlight(true);
			params.setHighlightSimplePre("");
			params.setHighlightSimplePost("");
			
			
			QueryResponse queryResponse = httpSolrService.query(params);
			
			SolrDocumentList solrDocumentList = queryResponse.getResults();
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			
			ArrayList<Product> productList = new ArrayList<Product>();
			for (SolrDocument solrDocument : solrDocumentList) {
				Product product = new Product();
//			new Product(id, title, sellpoint, price, num, image, cid, status, images);
				product.setId(Long.parseLong(solrDocument.get("id").toString()));
				if (cid != null && "".equals(cid)) {
					product.setCid(Long.parseLong(cid));
				}
				product.setSellpoint(solrDocument.get("sellpoint").toString());
				product.setPrice(Long.parseLong(solrDocument.get("price").toString()));
				product.setImage(solrDocument.get("image").toString());
				product.setNum(Integer.parseInt(solrDocument.get("num").toString()));
				if ("1".equals(solrDocument.get("status").toString())) {
					
					product.setStatus(true);
				}else {
					product.setStatus(false);
				}
				product.setTitle(solrDocument.get("title").toString());
				productList.add(product);
			}
			
//		new SolrResult(curPage, pageCount, recordCount, productList);
			
			SolrResult solrResult = new SolrResult(Integer.parseInt(page), ((int)solrDocumentList.getNumFound() + 20 -1)/20, (int)solrDocumentList.getNumFound(), productList);
			return solrResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	 
    @Autowired
    private HttpSolrServer httpSolrServer;

    public SolrResult search(String queryString, String cid, String price, String page, String sort) throws SolrServerException {
        
       
        SolrQuery params = new SolrQuery();
        
        // 设置查询条件
        if (queryString!=null && !"".equals(queryString)) {
            params.setQuery(queryString);
        }else{
            params.setQuery("*:*");
        }
      
        // 设置过滤条件
        if (cid!=null && !"".equals(cid)) {
            cid = "cid:"+cid;
            params.setFilterQueries(cid);
        }
      
        //设置价格范围 0-9
        if (price!=null && !"".equals(price)) {
            String[] split = price.split("-");
            if (split.length>0) {
                params.setFilterQueries("price:["+split[0]+" TO "+split[1]+"]");
            }
           
        }

        // 设置排序
        if (!"".equals(sort) && sort!=null) {
            if ("0".equals(sort)) {
                params.setSort("id", ORDER.desc);
            }else{
                params.setSort("id", ORDER.asc);
            }
        }
        
        // 设置分页
        if (page==null || "".equals(page)) {
            page = "1";
        }
        params.setStart((Integer.parseInt(page)-1)* 20);
        params.setRows(20);
     

        // 设置高亮显示
        params.setHighlight(true);
        params.setHighlightSimplePre("<font color='red'>");
        params.setHighlightSimplePost("</font>");

        QueryResponse queryResponse = httpSolrServer.query(params);
        
        //获取高亮数据
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();

        // 获取记录集
        SolrDocumentList results = queryResponse.getResults();

         long total = results.getNumFound();

        List<Product> productList = new ArrayList<Product>();
        for (SolrDocument solrDocument : results) {
            Product product = new Product();
            product.setId(Long.parseLong(solrDocument.get("id").toString()));
            
            
            //显示高亮标题
            List<String> list = highlighting.get(solrDocument.get("id")).get("title");
            if (list !=null  &&  list.size()>0) {
                product.setTitle(list.get(0));
            }else{
                product.setTitle(solrDocument.get("title").toString());
            }
            product.setImage(solrDocument.get("image").toString());
            if (cid!=null && !"".equals(cid)) {
                product.setCid(Long.parseLong(cid));
            }
            product.setNum(Integer.parseInt(solrDocument.get("num").toString()));
            product.setPrice(Long.parseLong(solrDocument.get("price").toString()));
            if (solrDocument.get("status")=="1") {
                product.setStatus(true);
            }else{
                product.setStatus(false);
            }
            productList.add(product);
        }

        SolrResult result = new SolrResult(Integer.parseInt(page), (int)(total+20-1)/20, (int)total, productList);
        
        return result;
    }

}
