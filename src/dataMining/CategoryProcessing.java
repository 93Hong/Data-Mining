package dataMining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryProcessing {
	private List<HashMap<String, Integer>> catList = new ArrayList<HashMap<String, Integer>>();

	public void counting(String fileName, int numOfCategory, int numOfFeature, ArrayList<String> featureList) throws IOException {
		File file = null;
		BufferedReader bReader = null;

		for (int i = 0; i < numOfCategory; i++) {
			String tmpFName = String.valueOf(i) + fileName + ".txt";
			file = new File(tmpFName);
			bReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

			HashMap<String, Integer> initialMap = new HashMap<String, Integer>();

			for (int j = 0; j < numOfFeature; j++)
				initialMap.put(featureList.get(j), 0);
			String line = null;
			String[] words = null;

			while ((line = bReader.readLine()) != null) {
				words = line.split(" ");

				for (int k = 0; k < words.length; k++)
					if (initialMap.containsKey(words[k])) {
						int cnt = initialMap.get(words[k]) + 1;
						initialMap.put(words[k], cnt);
					}
			}
			catList.add(i, initialMap);
		}
	}

	// getter
	public List<HashMap<String, Integer>> getCatList() {
		return catList;
	}
}