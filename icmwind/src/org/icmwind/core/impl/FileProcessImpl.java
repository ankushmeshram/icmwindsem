package org.icmwind.core.impl;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.icmwind.core.FileProcess;
import org.icmwind.util.ICMWindConfig;
import org.icmwind.util.Normalization;


import com.csvreader.CsvReader;

/**
 * @author anme05
 * 
 *         Singleton Implementation of FileProcess Interface. Overrides methods
 *         which provides access to Header Names list, normalizes header names
 *         and also provides access to Normalized Header Names list.
 * 
 *         IMPORTANT: Check whether ICMWindSetup has been initialized or not.
 * 
 */
public class FileProcessImpl implements FileProcess {

	private static final FileProcessImpl INSTANCE = new FileProcessImpl();
	private char separator = ';';
	private CsvReader reader = null;
	boolean isFileOpened = false;
	private String filepath = null;
	private InputStream is = null;
	
	private Date beginPeriod = null;
	private Date endPeriod = null;
	private int rowNumbers = 0;

	private List<String> headNamesList = new ArrayList<String>();
	private List<String> normHeadNamesList = new ArrayList<String>();
	private Map<String, String> normHeadNamesToHeadNames = new HashMap<String, String>();
	


	private FileProcessImpl() {
	}

	/**
	 * @return FileProcessImpl instance
	 */
	public static FileProcessImpl getInstance() {
		if (!ICMWindConfig.isInitialised())
			ICMWindConfig.init();

		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.icmwind.core.FileProcess#setSeparator(char)
	 * 
	 * Set data separator
	 */
	@Override
	public void setSeparator(char separator) {
		this.separator = separator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.icmwind.core.FileProcess#openFile(java.lang.String)
	 * 
	 * Open file from path with the DEFAULT separator. Get Header Names list and
	 * then normalize them to get Normalized Header Names list Return true if
	 * everything works.
	 */
	@Override
	public boolean readFile(String path) {
		
		filepath = path;

	
		// instantiate InputStream is
		try {
			// if loaded from ClassPath with relative url
			if(this.getClass().getClassLoader().getResourceAsStream(filepath) != null)
				is = this.getClass().getClassLoader().getResourceAsStream(filepath);
			// loaded from file system with absolute path
			else
				is = new BufferedInputStream(new FileInputStream(filepath));
		} catch (FileNotFoundException e1) {
				e1.printStackTrace();
		}
						
		
		this.readFileForRowNumbers();
		
		
		try {
//			// if loaded from ClassPath with relative url
//			if(this.getClass().getClassLoader().getResourceAsStream(filepath) != null)
//				this.reader = new CsvReader(this.getClass().getClassLoader().getResourceAsStream(filepath),separator,Charset.defaultCharset());
//			// loaded from file system with absolute path
//			else
//				this.reader = new CsvReader(filepath, separator, Charset.defaultCharset());
			
			this.reader = new CsvReader( filepath, separator,Charset.defaultCharset());
			
			this.reader.readHeaders();
			
			this.readFileForPeriod();
			
			this.headNamesList = Arrays.asList(this.reader.getHeaders());
			
			Normalization normalize = new Normalization();
			this.normHeadNamesList = normalize.normalizeListWithTranslation(headNamesList);
			this.normHeadNamesToHeadNames = normalize.getNormalizationMap();

			
			isFileOpened = true;
			return isFileOpened;
		} catch (IOException e) {
			e.printStackTrace();
			isFileOpened = false;
			return isFileOpened;
		}
	}
	
	private void readFileForRowNumbers() {
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
     
			while ((readChars = is.read(c)) != -1) {
			    for (int i = 0; i < readChars; ++i) {
			        if (c[i] == '\n') {
			            ++count;
			        }
			    }
			}
			
			this.rowNumbers = count;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Number of rows : " + rowNumbers);
	}

	private void readFileForPeriod() {
		try {
			int start = 1;
			int end = rowNumbers - 1;
			int c = 1;
			
			while(reader.readRecord()) {
				if(c != start && c != end) {
					reader.skipLine();
				} else if(c == start) {
					beginPeriod = convertStringToDate(reader.get("Zeit"));
					System.out.println("Get the begin date : " + reader.get("Zeit"));
				} else if(c == end) {
					endPeriod = convertStringToDate(reader.get("Zeit"));
					System.out.println("Get the end date : " + reader.get("Zeit"));
				}
				
				c++;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.icmwind.core.FileProcess#closeFile()
	 * 
	 * Close the data file
	 */
	@Override
	public void closeFile() {
		try {
			this.reader.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getFileName()
	 * 
	 * Get file name
	 */
	@Override
	public String getFileName() {
		int start = filepath.replaceAll("\\\\", "/").lastIndexOf("/");
		int end = filepath.lastIndexOf(".");
		return start >= 0 ? filepath.substring(start + 1, end).trim() : filepath;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.icmwind.core.FileProcess#existsRecord()
	 * 
	 * Checks if records exist further or not
	 */
	@Override
	public boolean existsRecord() {
		try {
			return reader.readRecord();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.icmwind.core.FileProcess#readRecord(java.lang.String)
	 * 
	 * Returns record for the given Header in the current row.
	 */
	@Override
	public String readRecord(String headerName) {
		try {
			return reader.get(headerName);
		} catch (IOException e) {
			e.printStackTrace();
			return "NA";
		}
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getHeadNamesList()
	 * 
	 * Returns list of Header Names of Data file
	 */
	@Override
	public List<String> getHeadNamesList() {
		if (isFileOpened) {
			return headNamesList;
		} else {
			System.out.println("ERROR: File not found.");
			return Collections.emptyList();
		}
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getNormHeadNamesList()
	 * 
	 * Return list of Normalized Header Names
	 */
	@Override
	public List<String> getNormHeadNamesList() {
		if (isFileOpened) {
			return normHeadNamesList;
		} else {
			System.out.println("ERROR: File not found.");
			return Collections.emptyList();
		}
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getNormalizationMap()
	 */
	@Override
	public Map<String, String> getNormalizationMap() {
		if(normHeadNamesToHeadNames.isEmpty())
			return Collections.emptyMap();
		else
			return normHeadNamesToHeadNames;
	}
	
	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getHeadNameFor(java.lang.String)
	 * 
	 * Returns Header Name mapped for given Normalized Header Name
	 */
	@Override
	public String getHeadNameFor(String normHeadName) {
		if (isFileOpened) {
			if (normHeadNamesList.contains(normHeadName)) {
				return normHeadNamesToHeadNames.get(normHeadName);
			} else {
				return "normHeadName not found in system.";
			}
		} else {
			return "ERROR: File not found.";
		}
	}

	@Override
	public String getTimeBegin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTimeEnd() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getNumberOfRows() {
		return this.rowNumbers;
	}

	@Override
	public Date getBeginPeriod() {
		return beginPeriod;
	}

	@Override
	public Date getEndPeriod() {
		return endPeriod;
	}

	private Date convertStringToDate(String dateString) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	

//	 DEBUG
//	 public static void main(String[] args) throws IOException 
//	 {
//		 FileProcess f = FileProcessImpl.getInstance();
//		 Utility.initDictionary(ICMWindSetup.getDictionaryPath());
//		 f.openFile(ICMWindSetup.getDataFilePath());
//		 System.out.println(f.getFileName());
////		 System.out.println(f.getHeadNameFor("dmcs a"));
////		 for(Map.Entry<String, String> entry : f.getNormalizationMap().entrySet())
////			 System.out.println(entry.getKey() + " -- " + entry.getValue());
////		 System.out.println(f.getHeadNamesList().toString() + "\n" + f.getNormHeadNamesList().toString());
//	 }

}
