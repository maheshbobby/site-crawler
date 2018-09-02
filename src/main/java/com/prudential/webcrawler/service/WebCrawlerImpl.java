package com.prudential.webcrawler.service;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.prudential.webcrawler.processor.PageProcessor;
import com.prudential.webcrawler.processor.PageProcessorImpl;
import com.prudential.webcrawler.util.DataContainer;
import com.prudential.webcrawler.util.URLValidator;

public class WebCrawlerImpl implements WebPageCrawler {
	
	private Set<String> synchronizedVisitedURLsSet;
	private BlockingQueue<String> unvisitedUrlsBlockingQueue;
	
	private PageProcessor pageProcessor;
	
	private DataContainer dataContainer;

	public WebCrawlerImpl(DataContainer dataContainer) {
		this.dataContainer=dataContainer;
		populateReferences();
	}

	private void populateReferences() {
		synchronizedVisitedURLsSet = dataContainer.getSynchronizedVisitedURLsSet();
		unvisitedUrlsBlockingQueue = dataContainer.getUnvisitedUrlsBlockingQueue();
	}

	@Override
	public void scanPage() {
		
		String URL;

		try {
			URL = unvisitedUrlsBlockingQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new RuntimeException("No crawler avaiable for this site");
		}
		
		String domain = dataContainer.getDomain();
		int responseCode;
		do {
			if (!URL.split("/")[2].equals(domain)) {
				URL = getNextURL();
				continue;
			}
			if (!synchronizedVisitedURLsSet.contains(URL)) {
				synchronizedVisitedURLsSet.add(URL);
			} else {
				URL = getNextURL();
				continue;
			}
			pageProcessor = new PageProcessorImpl();
			boolean isProcessValid = false;
			try {
				isProcessValid = pageProcessor.processURL(URL, dataContainer.getResultMap());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			responseCode = pageProcessor.getResponseCode();
 
			if (!isProcessValid) {
				URL = getNextURL();
				continue;
			}

			if (responseCode == 301) {
				try {
					URL = getFinalURLAfterRedirects(URL);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (dataContainer.getCrawledPagesCounter().get() >= 0) {
				dataContainer.incrementCrawledPagesCounter();
			}
			
			unvisitedUrlsBlockingQueue.addAll(pageProcessor.getContainedLinks());
			URL = getNextURL();
		} while (!unvisitedUrlsBlockingQueue.isEmpty() && dataContainer.getCrawledPagesCounter().get() < dataContainer.getCrawledPagesCounterThreshold());
	}

	private String getNextURL() {
		String URL = null;

		if (!unvisitedUrlsBlockingQueue.isEmpty()) {
			try {
				URL = unvisitedUrlsBlockingQueue.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		URL = URLValidator.getFixedUrl(URL);
		return URL;
	}

	String getFinalURLAfterRedirects(String URL) throws IOException {
		if (URL == null) {
			return null;
		}
		Response response = Jsoup.connect(URL).followRedirects(true).execute();
		return response.url().toString();
	}
}
