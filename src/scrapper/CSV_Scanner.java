package scrapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import scrapper.Info;

public class CSV_Scanner {

	private LinkedList<Info> list = null;

	// use third party OpenCSV library
	// source :
	// https://www.mkyong.com/java/how-to-read-and-parse-csv-file-in-java/
	private static final char DEFAULT_SEPARATOR = ',';
	private static final char DEFAULT_QUOTE = '"';

	public LinkedList<Info> dataScan(String filePath) {
		list = new LinkedList<>();
		Info info = null;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filePath));
			if (scanner.hasNext()) {
				List<String> line = parseLine(scanner.nextLine());

				if ("Linkedin_Profile_URL" != line.get(0).toString() || "First_Name" != line.get(1).toString()
						|| "Last_Name" != line.get(2).toString() || "Email_ID" != line.get(3).toString()
						|| "Contact_Number" != line.get(4).toString() || "Location" != line.get(5).toString()
						|| "Industry" != line.get(6).toString() || "Designation" != line.get(7).toString()
						|| "Company_Name" != line.get(8).toString() || "Company_Size" != line.get(9).toString()) {
					System.out.println("WRONG FILE");
				}

			}
			if (!scanner.hasNext())
				System.out.println("EMPTY FILE");

			while (scanner.hasNext()) {
				List<String> line = parseLine(scanner.nextLine());
				info = new Info(line.get(0), line.get(1), line.get(2), line.get(3), line.get(4), line.get(5),
						line.get(6), line.get(7), line.get(8), line.get(9));
				list.add(info);
				System.out.println("[Linkedin_Profile_URL= " + line.get(0) + ", First_Name= " + line.get(1)
						+ " , Last_Name=" + line.get(2) + ", Email_ID= " + line.get(3) + ", Contact_Number= "
						+ line.get(4) + " , Location=" + line.get(5) + ", Industry= " + line.get(6) + ", Designation= "
						+ line.get(7) + " , Company_Name=" + line.get(8) + ", Company_Size= " + line.get(9) + "]");

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		scanner.close();
		return list;
	}

	public List<String> parseLine(String cvsLine) {
		return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
	}

	public List<String> parseLine(String cvsLine, char separators) {
		return parseLine(cvsLine, separators, DEFAULT_QUOTE);
	}

	public List<String> parseLine(String cvsLine, char separators, char customQuote) {

		List<String> result = new ArrayList<>();

		// if empty, return!
		if (cvsLine == null && cvsLine.isEmpty()) {
			return result;
		}

		if (customQuote == ' ') {
			customQuote = DEFAULT_QUOTE;
		}

		if (separators == ' ') {
			separators = DEFAULT_SEPARATOR;
		}

		StringBuffer curVal = new StringBuffer();
		boolean inQuotes = false;
		boolean startCollectChar = false;
		boolean doubleQuotesInColumn = false;

		char[] chars = cvsLine.toCharArray();

		for (char ch : chars) {

			if (inQuotes) {
				startCollectChar = true;
				if (ch == customQuote) {
					inQuotes = false;
					doubleQuotesInColumn = false;
				} else {

					// Fixed : allow "" in custom quote enclosed
					if (ch == '\"') {
						if (!doubleQuotesInColumn) {
							curVal.append(ch);
							doubleQuotesInColumn = true;
						}
					} else {
						curVal.append(ch);
					}

				}
			} else {
				if (ch == customQuote) {

					inQuotes = true;

					// Fixed : allow "" in empty quote enclosed
					if (chars[0] != '"' && customQuote == '\"') {
						curVal.append('"');
					}

					// double quotes in column will hit this!
					if (startCollectChar) {
						curVal.append('"');
					}

				} else if (ch == separators) {

					result.add(curVal.toString());

					curVal = new StringBuffer();
					startCollectChar = false;

				} else if (ch == '\r') {
					// ignore LF characters
					continue;
				} else if (ch == '\n') {
					// the end, break!
					break;
				} else {
					curVal.append(ch);
				}
			}

		}

		result.add(curVal.toString());

		return result;
	}

}
