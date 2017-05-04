package dataMining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class RemoveDuplication {
	private HashMap<Integer, Integer> articleNum = new HashMap<Integer, Integer>();
	private Set<String> allFeature = new HashSet<String>();
	int counter = 0;

	// in each category, remove duplication of words
	public void removeDup(String fileName) throws IOException {
		File outputFile = null;
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream(fileName + ".txt"), "UTF-8"));
			FileWriter fWriter = null;
			for (int i = 0; i < 8; i++)
				articleNum.put(i, 0);

			// initialize
			String line = null;
			String category = null;
			String[] splitWords = null;
			Set<String> catList = null;

			// for each line in output file
			while ((line = reader.readLine()) != null) {
				if (line.length() == 0)
					continue;
				counter++;
				splitWords = line.split(" ");
				
				// store each words depending on category
				if (category == null || !category.equals(splitWords[0])) {
					category = splitWords[0];
					outputFile = new File(category + fileName + ".txt");
					fWriter = new FileWriter(outputFile, true);
				}

				catList = new HashSet<String>();
				// count duplicate words in category
				articleNum.put(Integer.parseInt(category), articleNum.get(Integer.parseInt(category)) + 1);
				
				for (int i = 0; i < splitWords.length - 1; i++) {
					catList.add(splitWords[i + 1]);
					allFeature.add(splitWords[i + 1]);
				}
				
				// write category file
				for (String s : catList) {
					if (s.length() == 0)
						continue;
					fWriter.write(s + " ");
					fWriter.flush();
				}
				fWriter.write("\r\n");
				fWriter.flush();
			}
			fWriter.close();
			reader.close();

		} catch (

		FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// getter
	
	public int getAllArticleNum() {
		int count = 0;
		for (int i = 0; i < articleNum.size(); i++) {
			count += articleNum.get(i);
		}
		return count;
	}

	public HashMap<Integer, Integer> getArticleNum() {
		return articleNum;
	}

	public int getAllFeatureNum() {
		return allFeature.size();
	}

	public ArrayList<String> getAllFeature() {
		ArrayList<String> strSet = new ArrayList<String>();
		for (String s : allFeature)
			strSet.add(s);
		return strSet;
	}
}