package com.prudential.webcrawler.presentation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prudential.webcrawler.service.WebCrawlService;
import com.prudential.webcrawler.util.URLValidator;

@RestController
public class WebCrawlerController {

	@Autowired
	WebCrawlService service;
	
	@RequestMapping(value = "/crawl/extent/{maxExtent}", method = RequestMethod.GET, produces = "application/json")
	public String crawltoDepth(@RequestParam(value = "uri", required = true) String uri,
			@PathVariable Integer maxExtent)  throws IOException, InterruptedException {
		URLValidator.validateURL(uri);
		return service.crawler(uri,maxExtent);
}
}