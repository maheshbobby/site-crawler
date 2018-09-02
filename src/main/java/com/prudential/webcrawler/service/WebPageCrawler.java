package com.prudential.webcrawler.service;

import java.io.IOException;

public interface WebPageCrawler {
	void scanPage() throws IOException, InterruptedException;
}
