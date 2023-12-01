package me.main.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public class HTTP {
	
	private static final Logger logger = Logger.getLogger(HTTP.class.getName());
	
	public static String getAsString(String urlToRead) {
		
		StringBuilder result = new StringBuilder();
		
		try {
			readAsLines(getConnection("GET", urlToRead), result :: append);
		} catch (MalformedURLException e) {
			logger.throwing(HTTP.class.getName(), "getAsList", e);
			return "";
		}
		return result.toString();
	}
	
	public static List<String> getAsList(String urlToRead) {
		
		ArrayList<String> result = new ArrayList<>();
		
		try {
			readAsLines(getConnection("GET", urlToRead), result :: add);
		} catch (MalformedURLException e) {
			logger.throwing(HTTP.class.getName(), "getAsList", e);
			return new ArrayList<>();
		}
		
		return result;
	}
	
	@SuppressWarnings("SameParameterValue")
	private static HttpURLConnection getConnection(String requestMethod, String urlToRead) throws MalformedURLException {
		
		return getConnection(requestMethod, urlToRead, new HashMap<>());
	}
	
	private static HttpURLConnection getConnection(String requestMethod, String urlToRead, HashMap<String, String> headers) throws MalformedURLException {
		
		URL           url    = URI.create(urlToRead).toURL();
		
		HttpURLConnection conn;
		try {
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
			conn.setRequestMethod(requestMethod.toUpperCase());
			for (String key : headers.keySet()) {
				conn.setRequestProperty(key, headers.get(key));
			}
			
			return conn;
		} catch (IOException e) {
			logger.throwing(HTTP.class.getName(), "getConnection", e);
			return null;
		}
	}
	
	public static String post(String urlToPostTo, String content) {
		
		return post(urlToPostTo, content, new HashMap<>());
	}
	
	public static String post(String urlToPostTo, String content, HashMap<String, String> headers) {
		
		
		HttpURLConnection conn;
		try {
			conn = getConnection("POST", urlToPostTo, headers);
			if (conn == null) {
				return "";
			}
			conn.setDoOutput(true);
			
			try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
				wr.writeBytes(content);
			} catch (IOException e) {
				logger.throwing(HTTP.class.getName(), "post", e);
				return "";
			}
			
			StringBuilder result = new StringBuilder();
			
			readAsLines(conn, result :: append);
			return result.toString();
			
			
		} catch (MalformedURLException e) {
			logger.throwing(HTTP.class.getName(), "post", e);
			return "";
		}
		
	}
	
	private static void readAsLines(HttpURLConnection connection, Consumer<String> lineCallback) {
		
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			String line;
			
			while ((line = bufferedReader.readLine()) != null) {
				lineCallback.accept(line);
			}
			
		} catch (IOException e) {
			logger.throwing(HTTP.class.getName(), "readAsLines", e);
		}
		
	}
	
}
