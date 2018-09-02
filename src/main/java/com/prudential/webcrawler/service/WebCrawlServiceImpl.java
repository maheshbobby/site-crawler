package com.prudential.webcrawler.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prudential.webcrawler.util.DataContainer;


@Service
public class WebCrawlServiceImpl implements WebCrawlService {

	ExecutorService executor = Executors.newFixedThreadPool(100);

	@Override
	public String crawler(String urlDomain, Integer maxExtent) throws  InterruptedException, JsonProcessingException {
		final DataContainer dataContainer = new DataContainer(0, maxExtent, urlDomain.split("/")[2]);
		dataContainer.getUnvisitedUrlsBlockingQueue().add(urlDomain);
		String result = "";
		Future future = executor.submit(() -> {
			 new WebCrawlerImpl(dataContainer).scanPage();
			 });
		 try {
	            future.get(2000000, TimeUnit.SECONDS);
	        } catch (InterruptedException | ExecutionException | TimeoutException e1) {
	            e1.printStackTrace();
	        }
		 
		 if (future.isDone() && !future.isCancelled()) {
	   			 result =  dataContainer.getProcessedPagesStringInJsonFormat();
	        }
		 
		 return result;
		 
	}
	
	

	
}
