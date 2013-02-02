package org.icmwind.core.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.icmwind.core.FileProcess;
import org.icmwind.core.ICMWindSetup;
import org.icmwind.util.Utility;

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

	private List<String> headNamesList = new ArrayList<String>();
	private List<String> normHeadNamesList = new ArrayList<String>();
	private Map<String, String> normHeadNamesToHeadNames = new HashMap<String, String>();

	private FileProcessImpl() {
	}

	/**
	 * @return FileProcessImpl instance
	 */
	public static FileProcessImpl getInstance() {
		if (!ICMWindSetup.isInitialised())
			ICMWindSetup.init();

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
	public boolean openFile(String path) {
		try {
			this.reader = new CsvReader(this.getClass().getClassLoader()
					.getResourceAsStream(path), separator,
					Charset.defaultCharset());
			this.reader.readHeaders();
			this.headNamesList = Arrays.asList(this.reader.getHeaders());
			this.normHeadNamesList = Utility
					.normalizeListWithTranslation(headNamesList);
			isFileOpened = true;
			return isFileOpened;
		} catch (IOException e) {
			e.printStackTrace();
			isFileOpened = false;
			return isFileOpened;
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
		this.reader.close();
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

	// DEBUG
	// public static void main(String[] args) throws FileNotFoundException,
	// IOException {
	// FileProcessImpl f = FileProcessImpl.getInstance();
	// Utility.initDictionary(ICMWindSetup.getDictionaryPath());
	// f.openFile(ICMWindSetup.getDataFilePath());
	// System.out.println(f.getHeadNamesList().toString() + "\n" +
	// f.getNormHeadNamesList().toString());
	// }

}
