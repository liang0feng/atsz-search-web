package com.atsz.search.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.atsz.search.pojo.SolrResult;
import com.atsz.search.service.SearchService;

@Controller
public class SearchController {
	@Autowired
	SearchService searchService;

	@RequestMapping(value="search.html")
	public ModelAndView search(String queryString,String cid,String price,String page,String sort){
		ModelAndView mv = new ModelAndView("search");
		try {
			SolrResult results = searchService.search(queryString, cid, price, page, sort);
			mv.addObject("cid", cid);
			mv.addObject("price", price);
			mv.addObject("sort", sort);
			mv.addObject("result", results);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mv;
	}
}
