import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebPageRanker {
	public static void main(String[] args) {
		String keyword = "Java";
		String[] urls = { "https://en.wikipedia.org/wiki/Java_(programming_language)",
				"https://docs.oracle.com/en/java/", "https://www.javaworld.com/",
				"https://www.tutorialspoint.com/java/index.htm" };

		Map<String, Integer> results = rankPages(keyword, urls);
		List<Map.Entry<String, Integer>> sortedResults = sortResults(results);

		for (Map.Entry<String, Integer> entry : sortedResults) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	public static Map<String, Integer> rankPages(String keyword, String[] urls) {
		Map<String, Integer> results = new HashMap<>();
		for (String url : urls) {
			try {
				String text = getPageText(url);
				int count = countOccurrences(keyword, text);
				results.put(url, count);
			} catch (IOException e) {
				System.err.println("Error accessing web page " + url + ": " + e.getMessage());
			}
		}
		return results;
	}

	public static String getPageText(String urlString) throws IOException {
		StringBuilder sb = new StringBuilder();
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		reader.close();
		return sb.toString();
	}

	public static int countOccurrences(String keyword, String text) {
		return text.toLowerCase().split(keyword.toLowerCase()).length - 1;
	}

	public static List<Map.Entry<String, Integer>> sortResults(Map<String, Integer> results) {
		List<Map.Entry<String, Integer>> sortedResults = new ArrayList<>(results.entrySet());
		Collections.sort(sortedResults, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
				return entry2.getValue().compareTo(entry1.getValue());
			}
		});
		return sortedResults;
	}
}
