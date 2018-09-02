package com.prudential.webcrawler.util;

import java.util.ArrayList;
import java.util.List;

public class ExternalSites {

	public static  List<String> exludeDomainList  = null;
	static {

		 exludeDomainList = new ArrayList<String>() {
			private static final long serialVersionUID = 1L;
			{
				add("www.google.com");
				add("www.twitter.com");
				add("http://www.google.com");
				add("http://www.twitter.com");
			}
		};

	}
	
}
