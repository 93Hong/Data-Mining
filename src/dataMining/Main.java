package dataMining;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Main {
	private static HashMap<Integer, Integer> numOfCatFeature;
	private static List<HashMap<String, Integer>> catList;
	private static List<Pair<String, Float>> ChiList = new ArrayList<Pair<String, Float>>();
	private static HashMap<String, Integer> initialMap = new HashMap<String, Integer>();
	private static ArrayList<String> featureList = new ArrayList<String>(), wordList;
	private static int numOfFeature, numOfAllDoc, numOfWord, numOfCategory = 8;
	private static float[] chiValues;

	public static void main(String[] args) throws IOException {
		preProcessing();

		for (int i = 0; i < numOfWord; i++) {
			chiValues[i] = ChiList.get(i).getR();
			initialMap.put(ChiList.get(i).getL(), i);
		}

		// for text 1~4 to make train set
		MakeTrainningSet mkTrain = new MakeTrainningSet();
		mkTrain.makingSet("output", initialMap, chiValues, ChiList, numOfCategory, "train.txt");
		
		// for text 5 to make test set
		mkOutput2();
		MakeTrainningSet mkTest = new MakeTrainningSet();
		mkTest.makingSet("output2", initialMap, chiValues, ChiList, numOfCategory, "test.txt");
	}

	public static void preProcessing() {
		// make output file
		mkOutput();

		// fill map with all words
		fillMap();

		// calculate chisquare value for all words
		calculateChiSquare();

		// sort using second value (descending order)
		Collections.sort(ChiList, Comparator.comparing(p -> -p.getR()));
	}

	// make output file
	public static void mkOutput() {
		try {
			FileRead fRead = new FileRead();
			String fileName = "output";
			fRead.fRead("HKIB-20000_001.txt", false, fileName);
			fRead.fRead("HKIB-20000_002.txt", true, fileName);
			fRead.fRead("HKIB-20000_003.txt", true, fileName);
			fRead.fRead("HKIB-20000_004.txt", true, fileName);
			
			RemoveDuplication rDup = new RemoveDuplication();
			rDup.removeDup(fileName);

			// number of all feature
			numOfFeature = rDup.getAllFeatureNum();
			// store all features
			featureList = rDup.getAllFeature();

			// make category files
			CategoryProcessing catPro = new CategoryProcessing();
			catPro.counting(fileName, numOfCategory, numOfFeature, featureList);

			numOfWord = rDup.getAllFeatureNum();
			numOfAllDoc = rDup.getAllArticleNum();
			numOfCatFeature = rDup.getArticleNum();
			
			wordList = rDup.getAllFeature();
			catList = catPro.getCatList();
			chiValues = new float[numOfWord];

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// for text 5 to make test set
	public static void mkOutput2() {
		try {
			FileRead fr = new FileRead();
			String fileName = "output2";
			fr.fRead("HKIB-20000_005.txt", false, fileName);
			RemoveDuplication rd = new RemoveDuplication();
			rd.removeDup(fileName);

			CategoryProcessing catPro = new CategoryProcessing();
			catPro.counting(fileName, numOfCategory, numOfFeature, featureList);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// fill map
	public static void fillMap() {
		for (int i = 0; i < numOfWord; i++)
			initialMap.put(wordList.get(i), 0);
	}

	// calculating chi square on all words
	public static void calculateChiSquare() {

		for (int i = 0; i < numOfWord; i++) {
			int a = 0, b = 0, c = 0, d = 0;
			float maxChi = 0;

			// for all category
			for (int j = 0; j < numOfCategory; j++) {
				// initialize
				a = 0;
				b = 0;
				c = 0;
				d = 0;
				maxChi = 0;
				
				// t & c
				a = catList.get(j).get(wordList.get(i));
				// ~t & c
				b = numOfCatFeature.get(j) - a;

				// for all category
				for (int k = 0; k < numOfCategory; k++) {
					if (j == k)
						continue;
					// t & ~c
					c += catList.get(k).get(wordList.get(i));
				}
				// ~t & ~c
				d = numOfAllDoc - numOfCatFeature.get(j) - c;

				float chisquare = (float) (numOfAllDoc * (a * d - c * b) * (a * d - c * b))
						/ ((a + c) * (b + d) * (a + b) * (c + d));
				// get max chisquare
				maxChi = Math.max(maxChi, chisquare);
			}
			ChiList.add(new Pair<String, Float>(wordList.get(i), maxChi));
		}
	}

}
