package dm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class Main {
	private static ArrayList<String> allFeature = new ArrayList<String>(), allWords;
	private static int featureNum, numOfAllDoc, numOfWord, numOfCat = 8;
	private static float[] ResultChi;

	private static HashMap<Integer, Integer> catFeacherNum;
	private static List<HashMap<String, Integer>> list;
	private static List<Pair<String, Float>> ChiList = new ArrayList<Pair<String, Float>>();
	private static HashMap<String, Integer> indexMap = new HashMap<String, Integer>();

	public static void mkFile() {
		try {
			FileRead fr = new FileRead();
			String fileName = "output";
			fr.Fread("HKIB-20000_001.txt", false, fileName);
			fr.Fread("HKIB-20000_002.txt", true,fileName);
			fr.Fread("HKIB-20000_003.txt", true,fileName);
			fr.Fread("HKIB-20000_004.txt", true,fileName);
			RemoveDuplication rd = new RemoveDuplication();
			rd.removeDup(fileName);

			featureNum = rd.getAllFeatureNum();
			allFeature = rd.getAllFeature();

			CountingInCategory cic = new CountingInCategory();
			cic.counting(fileName,numOfCat, featureNum, allFeature);

			numOfWord = rd.getAllFeatureNum();
			numOfAllDoc = rd.getAllArticleNum();
			catFeacherNum = rd.getArticleNum();
			allWords = rd.getAllFeature();
			list = cic.getList();
			ResultChi = new float[numOfWord];

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void mkFile2() {
		try {
			FileRead fr = new FileRead();
			String fileName = "output2";
			fr.Fread("HKIB-20000_005.txt", false, fileName);
			RemoveDuplication rd = new RemoveDuplication();
			rd.removeDup(fileName);
			
			CountingInCategory cic = new CountingInCategory();
			cic.counting(fileName,numOfCat, featureNum, allFeature);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void initializeMap() {
		for (int i = 0; i < numOfWord; i++)
			indexMap.put(allWords.get(i), 0);
	}

	public static void calChiSquare() {
		for (int i = 0; i < numOfWord; i++) {
			int a = 0, b = 0, c = 0, d = 0;
			float chiSquare = 0;

			for (int j = 0; j < numOfCat; j++) {
				a = list.get(j).get(allWords.get(i));
				b = catFeacherNum.get(j) - a;

				for (int k = 0; k < numOfCat; k++) {
					if (j == k)
						continue;
					c += list.get(k).get(allWords.get(i));
				}
				d = numOfAllDoc - catFeacherNum.get(j) - c;
				float tmp = (float) (numOfAllDoc * (a * d - c * b) * (a * d - c * b))
						/ ((a + c) * (b + d) * (a + b) * (c + d));
				chiSquare = Math.max(chiSquare, tmp);
			}
			ChiList.add(new Pair<String, Float>(allWords.get(i), chiSquare));
		}
	}

	public static void main(String[] args) throws IOException {
		mkFile(); // output & category 0~7
		initializeMap(); // initialize index map
		calChiSquare(); // calculate chi square
		// sort using second value (descending order)
		Collections.sort(ChiList, Comparator.comparing(p -> -p.getR()));

		for (int i = 0; i < numOfWord; i++) {
			ResultChi[i] = ChiList.get(i).getR();
			indexMap.put(ChiList.get(i).getL(), i);
		}
		//HashMap<String, Integer> indexMap, float ResultChi[], int size
		MakeTrainningSet mkTS = new MakeTrainningSet();
		mkTS.makingSet("output",indexMap, ResultChi, ChiList, numOfCat,"maro.txt");
		mkFile2();
		MakeTrainningSet mkTS2 = new MakeTrainningSet();
		mkTS2.makingSet("output2",indexMap, ResultChi, ChiList, numOfCat,"maro2.txt");
//		
//		System.out.println(ChiList.get(0).getL() + " " + ChiList.get(0).getR());
//		System.out.println(ChiList.get(1).getL() + " " + ChiList.get(1).getR());
//		System.out.println(ChiList.get(2).getL() + " " + ChiList.get(2).getR());
		
		for (int i=0; i<500; i++) {
			System.out.println(ChiList.get(i).getL() + " " + ChiList.get(i).getR());
		}
	}
}
