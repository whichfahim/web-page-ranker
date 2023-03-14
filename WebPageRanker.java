import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebPageRanker {
	public static void main(String[] args) {
		String keyword = "W3C";
		String directory = "src/resources/W3C Web Pages";

		Map<String, Integer> results = rankDirectory(keyword, directory);
		List<Map.Entry<String, Integer>> sortedResults = sortResults(results);

		for (Map.Entry<String, Integer> entry : sortedResults) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}

	/**
     * Ranks all files in the specified directory based on the number of occurrences of the given keyword.
     * @param keyword the keyword to search for
     * @param directory the path to the directory to search
     * @return a map of filenames to the number of occurrences of the keyword in each file
     */
	public static Map<String, Integer> rankDirectory(String keyword, String directory) {
		Map<String, Integer> results = new HashMap<>();
		File[] files = new File(directory).listFiles(); // get array of files and directories in directory
		for (File file : files) {
			if (file.isFile()) { // only process files, not directories
				try {
					String text = getFileText(file);
					int count = countOccurrences(keyword, text);
					results.put(file.getName(), count);
				} catch (IOException e) {
					System.err.println("Error reading file " + file.getName() + ": " + e.getMessage());
				}
			}
		}
		return results;
	}

	/**
	 * Reads the contents of a file into a string.
	 * 
	 * @param file the file to read
	 * @return the contents of the file as a string
	 * @throws IOException if there is an error reading the file
	 */
	public static String getFileText(File file) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		reader.close();
		return sb.toString();
	}

	/**
	 * Counts the number of occurrences of a keyword in a string.
	 * 
	 * @param keyword the keyword to search for
	 * @param text    the text to search
	 * @return the number of occurrences of the keyword in the text
	 */
	public static int countOccurrences(String keyword, String text) {
		return text.toLowerCase().split(keyword.toLowerCase()).length - 1;
	}

	/**
	 * Sorts the results map by value in descending order.
	 * 
	 * @param results the map to sort
	 * @return a list of entries in the map, sorted by value in descending order
	 */
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
