package com.prudential.webcrawler.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataContainer {

	private String domain;
	private AtomicInteger crawledPagesCounter;
	private int crawledPagesCounterThreshold;
	private HashMap<String, List<String>> resultMap;  

	private Set<String> synchronizedVisitedURLsSet = Collections.synchronizedSet(new HashSet<>());
	private BlockingQueue<String> unvisitedUrlsBlockingQueue = new LinkedBlockingQueue<String>();


	public DataContainer(int crawledPagesCounter, int crawledPagesCounterThreshold, String urlDomain) {
		this.crawledPagesCounter = new AtomicInteger(crawledPagesCounter);
		this.crawledPagesCounterThreshold = crawledPagesCounterThreshold;
		this.domain = urlDomain;

		// Instantiate the set to import the unique URLs we have visited
		this.synchronizedVisitedURLsSet = Collections.synchronizedSet(new HashSet<>());
		// Instantiate the unvisited URLs queue
		this.unvisitedUrlsBlockingQueue = new LinkedBlockingQueue<String>();
		
		this.resultMap = new HashMap<String, List<String>>();
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public AtomicInteger getCrawledPagesCounter() {
		return crawledPagesCounter;
	}

	public void setCrawledPagesCounter(AtomicInteger crawledPagesCounter) {
		this.crawledPagesCounter = crawledPagesCounter;
	}

	public int getCrawledPagesCounterThreshold() {
		return crawledPagesCounterThreshold;
	}

	public void setCrawledPagesCounterThreshold(int crawledPagesCounterThreshold) {
		this.crawledPagesCounterThreshold = crawledPagesCounterThreshold;
	}

	public Set<String> getSynchronizedVisitedURLsSet() {
		return synchronizedVisitedURLsSet;
	}

	public BlockingQueue<String> getUnvisitedUrlsBlockingQueue() {
		return unvisitedUrlsBlockingQueue;
	}
	
	
	
	public HashMap<String, List<String>> getResultMap() {
		return resultMap;
	}

	public void setResultMap(HashMap<String, List<String>> resultMap) {
		this.resultMap = resultMap;
	}

	public String getProcessedPagesStringInJsonFormat() throws JsonProcessingException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(getResultMap());
		return json;
	}

	public void incrementCrawledPagesCounter() {
		this.crawledPagesCounter.incrementAndGet();
	}

}
