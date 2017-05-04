package dataMining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileRead {
	// category list
	private String[] category = {"건강과 의학", "경제", "과학", "교육", "문화와 종교", "사회", "산업", "여가생활"};
	// category tag
	private String catTag = "#CAT'03:";
	
	public void fRead(String fileName, boolean check, String readFile) throws IOException {
		File outputFile = new File(readFile + ".txt");
		
		try {
			BufferedReader bReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			FileWriter fWriter = new FileWriter(outputFile, check);

			String line = null;
			// read all file than parsing word
			while ((line = bReader.readLine()) != null) {
				if (line.length() == 0)
					continue;
				else if (line.contains(catTag)) {
					String[] cat = null;
					cat = line.split("/");
					
					for (int i=0; i<category.length; i++)
						if (cat[1].equals(category[i]))
							cat[1] = "" + i;
					line = "\r\n" + cat[1] + " ";
					
				} else if (line.contains("@DOCUMENT") || line.contains("#DocID :") || line.contains("#CAT'07:")
						|| line.contains("#TITLE :") || line.contains("#TEXT  : "))
					continue;
				else {
					line = deleteChar(line);
					line = removeSpace(line);
				}
				fWriter.write(line);
				fWriter.flush();
			}
			bReader.close();
			fWriter.close();

		} catch (

		FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// delete all special character
	public String deleteChar(String str) {
		String delemenator = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		return str.replaceAll(delemenator, " ");
	}

	// delete all space
	public String removeSpace(String str) {
		String delemenator = "\\s{2,}";
		return str.replaceAll(delemenator, " ");
	}
}