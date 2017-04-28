package dm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MakeTrainningSet {

	public void makingSet(String fileName, HashMap<String, Integer> indexMap, float ResultChi[],
			List<Pair<String, Float>> ChiList, int size, String fileN) throws IOException {

		File outputFile = new File(fileN);
		FileWriter fw = new FileWriter(outputFile, false);
		File Rfile = null;
		BufferedReader reader = null;
		for (int i = 0; i < size; i++) {
			String tmpFName = String.valueOf(i) + fileName + ".txt";
			Rfile = new File(tmpFName);
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(Rfile), "UTF-8"));
			String line = null;
			String[] splitWord = null;
			while ((line = reader.readLine()) != null) {
				fw.write("\r\n" + (i + 1) + " ");
				fw.flush();
				splitWord = line.split(" ");
				String rbuffer = "";

				ArrayList<Integer> arr = new ArrayList<>();
				int cnt = 0;
				for (int k = 0; k < splitWord.length; k++)
					if (indexMap.containsKey(splitWord[k]))
						arr.add(indexMap.get(splitWord[k]));
					else
						cnt++;
				Collections.sort(arr);

				for (int k = 0; k < splitWord.length - cnt; k++) {
					rbuffer += (arr.get(k) + 1) + ":" + ResultChi[arr.get(k)] + " ";
				}
				fw.write(rbuffer);
				fw.flush();
			}
			reader.close();
		}
		fw.close();
	}
}