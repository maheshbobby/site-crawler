package com.prudential.webcrawler.processor;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.prudential.webcrawler.util.ExternalSites;

public class PageProcessorImpl implements PageProcessor {
	private Document document;
	private List<String> containedLinks;
	private List<String> staticElementsList;
	private int responseCode=0;

	public PageProcessorImpl() {
		document = null;
		staticElementsList = new LinkedList<>();
		containedLinks = new LinkedList<>();
	}

	public boolean processURL(String URL, HashMap<String, List<String>> resultMap) throws IOException {
		try {
			Response response = Jsoup.connect(URL).timeout(10000).userAgent(USER_AGENT).execute();
			document = response.parse();
			responseCode = response.statusCode();	
			Elements media = document.select("[src]");
			Elements links = document.select("a[href]");
			for (Element src : media) {
				if(!src.attr("abs:src").contains("js")){
				staticElementsList.add(src.attr("abs:src"));
				}
			}
			getResult(URL, resultMap,staticElementsList);
			
			for (Element link : links) {
				String containedLink = link.attr("abs:href");

				if (containedLink == null || containedLink.isEmpty()) {
					continue;
				}
				if(!ExternalSites.exludeDomainList.contains(link.attr("abs:href"))){
				containedLinks.add(containedLink);
				}
			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
			return false;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public List<String> getContainedLinks() {
		return containedLinks;
	}

	public int getResponseCode() {
		return responseCode;
	}

	
	private static void getResult(String URL, HashMap<String, List<String>> resultMap, List<String> assets) {
		String s = "";
		String partialURLs = "";
		boolean found=false;
		String domainURL = URL.split("//")[1];
		String[] splitsURLs = domainURL.split("/");
		int lenOfURLs = splitsURLs.length;
		for (int index = lenOfURLs; index > 0; index--) {
				s = splitsURLs[index-1] + "/" + s;
				partialURLs="";
				for ( int i=0 ; i < index ; i++){
					if (i==0){
						partialURLs =   splitsURLs[i] ;
					}else {
						partialURLs =  partialURLs + "/"+ splitsURLs[i] ;
					}
				}
				if (resultMap.get(partialURLs) != null) {
					found = true;
					resultMap.put(partialURLs, resultMap.get(partialURLs));
					break;
				} 
		}
		
		if (found==false || resultMap.get(URL) == null){
			resultMap.put(URL, assets);
		}
		
	/*	if (resultMap.get(partialURLs) == null) {
			resultMap.put(partialURLs, assets);
		} */


	}
	/*private static void getResult(String URL, HashMap<String, Map<String, List>> resultMap, List<String>  assets) {
		String s = "";
		String partialURLs = "";
		boolean found=false;
		String domainURL = URL.split("/")[2];
		String[] splitsURLs = domainURL.split("/");
		int lenOfURLs = splitsURLs.length;
		for (int index = lenOfURLs; index > 0; index--) {
				s = splitsURLs[index-1] + "/" + s;
				//System.out.println("value of s ---  > " + s);
				partialURLs="";
				for ( int i=0 ; i < index ; i++){
					if (i==0){
						partialURLs =   splitsURLs[i] ;
					}else {
						partialURLs =  partialURLs + "/"+ splitsURLs[i] ;
					}
				}
				if (resultMap.get(partialURLs) != null) {
					found = true;
					Map<String, List> values = resultMap.get(partialURLs);
					values.put(partialURLs, assets);
					resultMap.put(partialURLs, values);
					break;
				} 
		}
		
		if (found==false){
			Map<String, List> values = new HashMap<String, List>();
			values.put(partialURLs, assets);
			resultMap.put(URL, values);
		}
		
		if (resultMap.get(URL) == null) {
			Map<String, List> values = new HashMap<String, List>();
			values.put(partialURLs, assets);
			resultMap.put(URL, values);
		} 


	}*/
}
