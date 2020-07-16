package com.app.googledrive;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GGDriveUtils {
	  private static final String APPLICATION_NAME = "Google Drive API Java Quickstart";
	  
	    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	 
	    // Directory to store user credentials for this application.
	    private static final java.io.File CREDENTIALS_FOLDER //
	            = new java.io.File(System.getProperty("user.home"), "credentials");
	 
	    private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";
	 
	    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
	 
	    // Global instance of the {@link FileDataStoreFactory}.
	    private static FileDataStoreFactory DATA_STORE_FACTORY;
	 
	    // Global instance of the HTTP transport.
	    private static HttpTransport HTTP_TRANSPORT;
	 
	    private static Drive _driveService;
	 
	    static {
	        try {
	            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
	            DATA_STORE_FACTORY = new FileDataStoreFactory(CREDENTIALS_FOLDER);
	        } catch (Throwable t) {
	            t.printStackTrace();
	            System.exit(1);
	        }
	    }
	 
	    public static Credential getCredentials() throws IOException {
	 
	        java.io.File clientSecretFilePath = new java.io.File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);
	 
	        if (!clientSecretFilePath.exists()) {
	            throw new FileNotFoundException("Please copy " + CLIENT_SECRET_FILE_NAME //
	                    + " to folder: " + CREDENTIALS_FOLDER.getAbsolutePath());
	        }
	 
	        InputStream in = new FileInputStream(clientSecretFilePath);
	 
	        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
	 
	        // Build flow and trigger user authorization request.
	        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
	                clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
	        Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
	 
	        return credential;
	    }
	 
	    public static Drive getDriveService() throws IOException {
	        if (_driveService != null) {
	            return _driveService;
	        }
	        Credential credential = getCredentials();
	        //
	        _driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) //
	                .setApplicationName(APPLICATION_NAME).build();
	        return _driveService;
	    }
	    
	    
    public final File createFolder(String folderIdParent, String folderName) throws IOException {
    	 
        File fileMetadata = new File();
 
        fileMetadata.setName(folderName);
        fileMetadata.setMimeType("application/vnd.google-apps.folder");
 
        if (folderIdParent != null) {
            List<String> parents = Arrays.asList(folderIdParent);
 
            fileMetadata.setParents(parents);
        }
        Drive driveService = GGDriveUtils.getDriveService();
 
        // Create a Folder.
        // Returns File object with id & name fields will be assigned values
        File file = driveService.files().create(fileMetadata).setFields("id, name").execute();
 
        return file;
    }
    // com.google.api.services.drive.model.File
    public static final List<File> getGoogleSubFolders(String googleFolderIdParent) throws IOException {
 
        Drive driveService = GGDriveUtils.getDriveService();
 
        String pageToken = null;
        List<File> list = new ArrayList<File>();
 
        String query = null;
        if (googleFolderIdParent == null) {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + googleFolderIdParent + "' in parents";
        }
 
        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime
                    .setFields("nextPageToken, files(id, name, createdTime)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }
 
    // com.google.api.services.drive.model.File
    public final List<File> getGoogleRootFolders() throws IOException {
        return getGoogleSubFolders(null);
    }
	public Boolean uploadFile() { 
		try{
			File fileMetadata = new File();
			fileMetadata.setName("README.txt");
			
			java.io.File filePath = new java.io.File("D:\\Temp\\README.txt");
			FileContent mediaContent = new FileContent("*/*", filePath);
			Drive driveService = GGDriveUtils.getDriveService();
			File file = driveService.files().create(fileMetadata, mediaContent)
					.setFields("id")
					.execute();
			System.out.println("File ID: " + file.getId());
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public Boolean downloadFile() {
		try {
			String fileId = "0BwwA4oUTeiV1UVNwOHItT0xfa2M";
			OutputStream outputStream = new ByteArrayOutputStream();
			Drive driveService = GGDriveUtils.getDriveService();
			driveService.files().get(fileId)
			.executeMediaAndDownloadTo(outputStream);
			return true;
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

}
