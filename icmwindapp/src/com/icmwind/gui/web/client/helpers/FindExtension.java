package com.icmwind.gui.web.client.helpers;

import java.io.File;
import java.io.FilenameFilter;

public class FindExtension {
	
	private String folder ;
	private String extension;
	
	private File directory = null;
	private String[] fileList = null;
	
	private FilenameFilter filter = null;

	@SuppressWarnings("unused")
	private FindExtension() {}
	
	public FindExtension(String folder, String extension) {
		this.folder = folder;
		this.extension = extension;
		this.filter = new FilenameFilterImpl(this.extension);
	}
	
	// Check whether file with Extension exits
	public boolean existsExtension() {
		boolean exists = false;
		
		directory = new File(folder);
		
		// check if its folder and not the file
		if(directory.isDirectory() == false) {
			System.out.println( folder +  " Directory doesn't exists.");
			return exists;
		}
		
		// list all file names and filter by extension
		fileList = directory.list(filter);
				
		if(fileList.length != 0)
			exists = true;
		
		return exists;
	}
	
	// List files with extension of the folder
	public void listFilesWithExtension() {
		if(this.existsExtension()) {
			System.out.println( folder + " contains files -");
			// to list file paths of the contained files
			for(String file : fileList) {
				String tempFile = new StringBuffer(folder).append(File.separator).append(file).toString();
				System.out.println(tempFile);
			}
		}
	}
	
	
	// Implementation of interface java.io.FilternameFilter
	private class FilenameFilterImpl implements FilenameFilter {
		private String extension;
		
		public FilenameFilterImpl(String extension) {
			this.extension = extension;
		}
		
		@Override
		public boolean accept(File dir, String name) {
				return name.endsWith(extension);
		}
		
	}
}
