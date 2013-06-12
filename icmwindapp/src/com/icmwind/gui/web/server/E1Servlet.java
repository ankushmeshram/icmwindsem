package com.icmwind.gui.web.server;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.icmwind.gui.web.client.helpers.GlobalInitializer;

public class E1Servlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PrintWriter logger = null;
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("E1Servlt Entered.");
		logger = GlobalInitializer.get().getLogWriter();
		logger.println();
		logger.println("File Upload Operation.");
		logger.flush();
		
		long startTime = System.currentTimeMillis();
		
		if(ServletFileUpload.isMultipartContent(req)) {
			System.out.println("E1Servlet - entering right zone.");
			
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			try {
				@SuppressWarnings("unchecked")
				List<FileItem> items = upload.parseRequest(req);
				
				for(FileItem item : items) {
					// Skip Form Fields which are not needed.
					if(item.isFormField()) continue;
					
					String filename = item.getName();

					//debug
					System.out.println("FileUpload Servlet - Filename: " + filename);
					
					// Only get filename, most browser doesn't allow full path.
					if(filename != null)
						filename = FilenameUtils.getName(filename);
					
					// Locate the upload directory
					File uploadDirectory = new File(GlobalInitializer.get().DATA_LOCATION);
					
					// if directory doesn't exist, create it 
					if(!uploadDirectory.exists())
						uploadDirectory.mkdir();
					else
						System.out.println("FileUploadServlet: " + GlobalInitializer.get().DATA_LOCATION + " Directory already exists.");
				
					// Create File Object at UPLOAD_DIRECTORY with filename
					File uploadedFile = new File(uploadDirectory, filename);
										
					//debug
					System.out.println("Uploaded File Path : " + uploadedFile.getAbsolutePath());
					
					// if it is created new, write/copy the passed file to created File object.
					// Report user the Success in the RESPONSE.				
					if(uploadedFile.createNewFile()) {
						item.write(uploadedFile);
						resp.setStatus(HttpServletResponse.SC_ACCEPTED);
						resp.getWriter().print("FileUploadServlet: The file was created successfully.");
//						DEBUG - removed
//						resp.flushBuffer();
					} else {
						resp.getWriter().print("FileUploadServlet: The file already exists.");
//						DEBUG - removed
//						throw new IOException("The file already exists in repository.");
					}
					
					// Set Global variable UPLOADED FILE PATH to be used for getting path
					GlobalInitializer.get().setUploadedFilePath(uploadedFile.getAbsolutePath());
					
					long elapsedTime = System.currentTimeMillis() - startTime; 
					logger.println("File Upload Time: " + elapsedTime + "ms");
					logger.flush();
					
					resp.flushBuffer();
				}
				
			} catch (Exception e) {
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"An error occurred while creating the file : " + e.getMessage());
			}
			
		} else {
			resp.sendError(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Request contents type is not supported by the servlet.");
		}
	}
	
}
