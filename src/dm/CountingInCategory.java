package dm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CountingInCategory {
	private List<HashMap<String, Integer>> list = new ArrayList<HashMap<String, Integer>>();

	public void counting(String fileName, int size, int featureNum, ArrayList<String> allFeature) throws IOException {
		File Rfile = null;
		BufferedReader reader = null;

		for (int i = 0; i < size; i++) {
			String tmpFName = String.valueOf(i) + fileName + ".txt";
			Rfile = new File(tmpFName);
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(Rfile), "UTF-8"));

			HashMap<String, Integer> map = new HashMap<String, Integer>();

			for (int j = 0; j < featureNum; j++) {
				map.put(allFeature.get(j), 0);
			}
			list.add(i, map);
			String line = null;
			String[] splitWord = null;

			while ((line = reader.readLine()) != null) {
				splitWord = line.split(" ");

				for (int k = 0; k < splitWord.length; k++) {
					if (map.containsKey(splitWord[k])) {
						int num = map.get(splitWord[k]) + 1;
						map.put(splitWord[k], num);
					}
				}
			}
			list.add(i, map);
		}
	}

	public List<HashMap<String, Integer>> getList() {
		return list;
	}
}