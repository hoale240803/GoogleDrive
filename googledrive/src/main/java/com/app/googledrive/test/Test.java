package com.app.googledrive.test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.app.googledrive.GGDriveUtils;
import com.google.api.services.drive.model.File;

public class Test {
	public static void main(String[] args) throws IOException, GeneralSecurityException {
		GGDriveUtils ggDriveUtils=new GGDriveUtils();
//        List<File> googleRootFolders = ggDriveUtils.getGoogleRootFolders();
//        for (File folder : googleRootFolders) {
// 
//            System.out.println("Folder ID: " + folder.getId() + " --- Name: " + folder.getName());
//        }
		ggDriveUtils.uploadFile();
	}

}
