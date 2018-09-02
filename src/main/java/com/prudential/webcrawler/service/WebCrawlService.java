package com.prudential.webcrawler.service;


import java.io.IOException;

public interface WebCrawlService {


String crawler(String urlDomain, Integer maxExtent) throws IOException,
		InterruptedException;

}
