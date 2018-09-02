package com.prudential.webcrawler.threadpool;

import com.prudential.webcrawler.service.WebPageCrawler;
import com.prudential.webcrawler.service.WebCrawlerImpl;
import com.prudential.webcrawler.util.DataContainer;

public class ConcurrentScanner implements Runnable {

	private DataContainer resultsHolder;
	
	public ConcurrentScanner(DataContainer resultsHolder) {
		super();
		this.resultsHolder = resultsHolder;
	}

	@Override
	public void run() {
		WebPageCrawler pageCrawler = new WebCrawlerImpl(resultsHolder);
		try {
			pageCrawler.scanPage();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
