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
	
	private CsvReader reader = null;
	
	private char separator = ';';
	
	private InputStream is = null;
	private String filepath = null;
	
	boolean isFileOpened = false;
	
	private String dataFilePK = null; 
	
	private int numOfObservations = 0;
	
	private Date beginDate = null;
	private Date endDate = null;
	
	private String fileName = null;

	private List<String> columnNamesList = new ArrayList<String>();
	private List<String> normColumnNamesList = new ArrayList<String>();
	
	private Map<String, String> normColNamesToColNames = new HashMap<String, String>();
	
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
						
		
		this.readFileForNumOfObservations();
		this.readFileForDates();
		
		try {
//			// if loaded from ClassPath with relative url
//			if(this.getClass().getClassLoader().getResourceAsStream(filepath) != null)
//				this.reader = new CsvReader(this.getClass().getClassLoader().getResourceAsStream(filepath),separator,Charset.defaultCharset());
//			// loaded from file system with absolute path
//			else
//				this.reader = new CsvReader(filepath, separator, Charset.defaultCharset());
			
			this.reader = new CsvReader( filepath, separator,Charset.defaultCharset());
			
			this.reader.readHeaders();
			
//			REMOVE THIS...before running actual
//			this.test();

//			/*
			this.columnNamesList = Arrays.asList(this.reader.getHeaders());
			
			Normalization normalize = new Normalization();
			this.normColumnNamesList = normalize.normalizeListWithTranslation(columnNamesList);
			this.normColNamesToColNames = normalize.getNormalizationMap();
//			*/
			
			isFileOpened = true;
			return isFileOpened;
		} catch (IOException e) {
			e.printStackTrace();
			isFileOpened = false;
			return isFileOpened;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getDataFilePK()
	 */
	@Override
	public String getDataFilePK() {
		return dataFilePK;
	}
	
	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getNumberOfObservations()
	 */
	@Override
	public int getNumberOfObservations() {
		return this.numOfObservations;
	}
	
	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getBeginDate()
	 */
	@Override
	public Date getBeginDate() {
		return beginDate;
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getEndDate()
	 */
	@Override
	public Date getEndDate() {
		return endDate;
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
		fileName = start >= 0 ? filepath.substring(start + 1, end).trim() : filepath;
		
		return fileName;
	}

	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getHeadNamesList()
	 * 
	 * Returns list of Header Names of Data file
	 */
	@Override
	public List<String> getColumnNamesList() {
		if (isFileOpened) {
			return columnNamesList;
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
	public List<String> getNormColumnNamesList() {
		if (isFileOpened) {
			return normColumnNamesList;
		} else {
			System.out.println("ERROR: File not found.");
			return Collections.emptyList();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getHeadNameFor(java.lang.String)
	 * 
	 * Returns Header Name mapped for given Normalized Header Name
	 */
	@Override
	public String getColumnNameFor(String normHeadName) {
		if (isFileOpened) {
			if (normColumnNamesList.contains(normHeadName)) {
				return normColNamesToColNames.get(normHeadName);
			} else {
				return "normHeadName not found in system.";
			}
		} else {
			return "ERROR: File not found.";
		}
	}
	
	/* (non-Javadoc)
	 * @see org.icmwind.core.FileProcess#getNormalizationMap()
	 */
	@Override
	public Map<String, String> getNormalizationMap() {
		if(normColNamesToColNames.isEmpty())
			return Collections.emptyMap();
		else
			return normColNamesToColNames;
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
	
	private Date convertStringToDate(String dateString) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			date =  sdf.parse(dateString);
						
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	private void readFileForNumOfObservations() {
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
			
			this.numOfObservations = count;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Number of rows : " + numOfObservations);
	}

	private void readFileForDates() {
		try {
			
			CsvReader tempReader = new CsvReader( filepath, separator,Charset.defaultCharset());
			
			tempReader.readHeaders();
			
			int start = 1;
			int end = numOfObservations - 1;
			int c = 1;
			
			while(tempReader.readRecord()) {
//			while(existsRecord()) {
				if(c != start && c != end) {
					tempReader.skipLine();
				} else if(c == start) {
					beginDate = convertStringToDate(tempReader.get("Zeit"));
					System.out.println("Get the begin date : " + tempReader.get("Zeit"));
				} else if(c == end) {
					endDate = convertStringToDate(tempReader.get("Zeit"));
					System.out.println("Get the end date : " + tempReader.get("Zeit"));
				}
				
				c++;
			}
			
			tempReader.close();
			System.gc();System.gc();System.gc();System.gc();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void test() {

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			Date test = sdf.parse("23.09.2008 13:10:00");
			
			
			while(reader.readRecord()) {
				Date temp = sdf.parse(this.reader.get(0));
				if(temp.before(test)) {
					System.out.println(temp.toString());
				}
			}
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
