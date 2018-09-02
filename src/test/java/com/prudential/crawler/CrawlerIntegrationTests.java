package com.prudential.crawler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.prudential.webcrawler.processor.PageProcessor;
import com.prudential.webcrawler.processor.PageProcessorImpl;
import com.prudential.webcrawler.service.WebCrawlerImpl;
import com.prudential.webcrawler.util.DataContainer;

public class CrawlerIntegrationTests {


	@Test
	public void testHTTPProtocol() throws IOException {
		PageProcessor pageProcessor = new PageProcessorImpl();
		HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
		String url = "https://www.prudential.co.uk/";
		boolean isProcessedURLValid = pageProcessor.processURL(url,hashMap);
		assertTrue("\"" + url + "\" should return true", isProcessedURLValid);
		assertNotNull(pageProcessor.getContainedLinks());
		assertEquals(200, pageProcessor.getResponseCode());
	}

	@Test
	public void testValidEndpoint()
			throws IOException, InterruptedException {
		HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
		String url = "https://www.prudential.co.uk/";
		String urlDomain = url.split("/")[2];
		// setting crawledPagesCounter to 0, to enable counting crawled pages
		// and crawledPagesCounterThreshold to 10, to set the threshold
		// of crawled pages to a limit.
		DataContainer holder = new DataContainer(0,10, urlDomain);
		WebCrawlerImpl pageCrawler = new WebCrawlerImpl(holder);
		holder.getUnvisitedUrlsBlockingQueue().add(url);
		pageCrawler.scanPage();
		String json = holder.getProcessedPagesStringInJsonFormat();
		assertNotNull("A valid URL should return a Not Null json string representation.", json);
	}
	
	

	@Test
	public void testValidEndpointWithConcurrency()
			throws IOException, InterruptedException {
		String url = "https://www.prudential.co.uk/";
		String urlDomain = url.split("/")[2];
		// setting crawledPagesCounter to 0, to enable counting crawled pages
		// and crawledPagesCounterThreshold to 10, to set the threshold
		// of crawled pages to a limit.
		DataContainer holder = new DataContainer(0,10, urlDomain);
		holder.getUnvisitedUrlsBlockingQueue().add(url);

		ExecutorService executor = Executors.newFixedThreadPool(3);

		Future future = executor.submit(() -> {
			 new WebCrawlerImpl(holder).scanPage();
			 });
		executor.shutdown();
		executor.awaitTermination(5, TimeUnit.MINUTES);

		String json = holder.getProcessedPagesStringInJsonFormat();
		assertNotNull("A valid URL should return a Not Null json string representation.", json);
	}

}
