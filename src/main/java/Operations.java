import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.ini4j.Ini;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Operations {
    private static List<String> lines = new ArrayList<String>();
    private static List<String> textOne = new ArrayList<String>();
    private static List<String> textTwo = new ArrayList<String>();
    private static List<String> textThree = new ArrayList<String>();
    private static List<String> textFour = new ArrayList<String>();
    private static List<String> textFive = new ArrayList<String>();
    private static List<String> textSix = new ArrayList<String>();
    private static List<String> wordList = new ArrayList<String>();

    private Operations() throws IOException {
        wordList = Arrays.asList(new Ini(new File("./input.ini")).get("SectionOne", "limits").split(","));
    }

    private void readLine() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("./TextFile.txt"));

        String line = reader.readLine();

        while (line != null) {
            line = reader.readLine();

            if (line != null)
                if (!line.equals(""))
                    lines.add(line);
        }
    }

    private void split(List<String> lines, int limit) {
        String word = "";

        for (String line : lines) {
            String[] words = line.split("", limit + 1);

            int i = 0;

            for (; i < limit; i++) {
                word = word + words[i];
            }

            textTwo.add(words[i]);

            textOne.add(word);

            word = "";
        }

        if (wordList.size() > 1) {
            word = "";

            for (String line : textTwo) {
                String[] words = line.split("", Integer.parseInt(wordList.get(1) + 1));

                int i = 0;

                for (; i < Integer.parseInt(wordList.get(1)); i++) {
                    word = word + words[i];
                }

                textFour.add(line.replace(word, ""));

                textThree.add(word);

                word = "";
            }
        }

        if (wordList.size() > 2) {
            word = "";

            for (String line : textFour) {
                String[] words = line.split("", Integer.parseInt(wordList.get(2) + 1));

                int i = 0;

                for (; i < Integer.parseInt(wordList.get(2)); i++) {
                    word = word + words[i];
                }

                textSix.add(line.replace(word, ""));

                textFive.add(word);

                word = "";
            }
        }
    }

    private void generateExcel() throws IOException {
        split(lines, Integer.parseInt(wordList.get(0)));

        String filename = "./NewExcelFile.xls";
        FileOutputStream fileOut = new FileOutputStream(filename);
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("FirstSheet");
        HSSFRow row;
        HSSFRow rowSecond;

        for (int i = 0; i < textOne.size(); i++) {
            row = sheet.createRow(i);

            row.createCell(0).setCellValue(textOne.get(i));

            if (textTwo.size() > 0)
                if (textThree.size() == 0)
                    row.createCell(1).setCellValue(textTwo.get(i));

            if (textThree.size() > 0)
                row.createCell(1).setCellValue(textThree.get(i));

            if (textFour.size() > 0)
                if (textFive.size() == 0)
                    row.createCell(2).setCellValue(textFour.get(i));

            if (textFive.size() > 0)
                row.createCell(2).setCellValue(textFive.get(i));

            if (textSix.size() > 0)
                row.createCell(3).setCellValue(textSix.get(i));
        }

        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
        System.out.println("Your excel file has been generated!");
    }

    public static void main(String args[]) throws IOException {
        Operations ops = new Operations();

        ops.readLine();
        ops.generateExcel();
    }
}