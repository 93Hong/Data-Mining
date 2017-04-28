package dm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileRead {
   public void Fread(String fileName, boolean starter, String fileN) throws IOException {
      File outputFile = new File(fileN+".txt");
      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
         FileWriter fw = new FileWriter(outputFile, starter);

         String line = null;
         while ((line = reader.readLine()) != null) {
            if (line.length() == 0)
               continue;
            else if (line.contains("#CAT'03:")) {
               String[] tmp = null;
               tmp = line.split("/");
               if (tmp[1].equals("건강과 의학"))
                  tmp[1] = "0";
               else if (tmp[1].equals("경제"))
                  tmp[1] = "1";
               else if (tmp[1].equals("과학"))
                  tmp[1] = "2";
               else if (tmp[1].equals("교육"))
                  tmp[1] = "3";
               else if (tmp[1].equals("문화와 종교"))
                  tmp[1] = "4";
               else if (tmp[1].equals("사회"))
                  tmp[1] = "5";
               else if (tmp[1].equals("산업"))
                  tmp[1] = "6";
               else if (tmp[1].equals("여가생활"))
                  tmp[1] = "7";

               line = "\r\n" + tmp[1] + " ";
            } else if (line.contains("@DOCUMENT") || line.contains("#DocID :") || line.contains("#CAT'07:")
                  || line.contains("#TITLE :") || line.contains("#TEXT  : "))
               continue;

            else if (line.contains("<!-search")) {
               int start = line.indexOf("<!");
               int end = line.indexOf(">");
               String tmp = line.substring(0, start);
               line = line.substring(end + 1);
               line = tmp + " " + line;
               line = StringReplace(line);
               line = continueSpaceRemove(line);

            } else {
               line = StringReplace(line);
               line = continueSpaceRemove(line);
            }
            fw.write(line);
            fw.flush();
         }
         reader.close();
         fw.close();

      } catch (

      FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // 특수문자 제거 하기
   public static String StringReplace(String str) {
      String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
      str = str.replaceAll(match, " ");
      return str;
   }

   // 연속 스페이스 제거
   public static String continueSpaceRemove(String str) {
      String match2 = "\\s{2,}";
      str = str.replaceAll(match2, " ");
      return str;
   }
}