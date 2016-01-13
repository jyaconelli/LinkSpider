package com.josephyaconelli.linkspider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GrabHTML {
	
	
	
	//code based off snippet from http://www.javaprogrammingforums.com/java-networking-tutorials/185-how-grab-html-source-code-website-url-index-page.html
	public static ArrayList<String> Connect(String site, String identifier, String start, String end) throws Exception {

		ArrayList<String> links = new ArrayList<>();

		// Set URL
		URL url = new URL(site);
		URLConnection spoof = url.openConnection();
		
		// Spoof the connection so we look like a web browser
		spoof.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 5.0; H010818)");
		BufferedReader in = new BufferedReader(new InputStreamReader(spoof.getInputStream()));
		String strLine = "";

		// Loop through every line in the source
		while ((strLine = in.readLine()) != null) {
			
			if(strLine.contains(identifier)){
				links.add(site + strLine.substring(strLine.indexOf(start) + start.length(), strLine.indexOf(end)));
			}
			
			//prints out original line
			System.out.println(strLine);
		}

		System.out.println("End of page.");
		
		return links;
	
	}
}
