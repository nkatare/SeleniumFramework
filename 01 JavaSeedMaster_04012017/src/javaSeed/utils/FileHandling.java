package javaSeed.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileHandling {
	
	public static void ZipFile(String ZipPath,String FilePath){
		
		final int BUFFER = 2048;
		
		try {
	         BufferedInputStream origin = null;
	         FileOutputStream dest = new FileOutputStream(ZipPath);
	         ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
	         //out.setMethod(ZipOutputStream.DEFLATED);
	         byte data[] = new byte[BUFFER];
	         // get a list of files from current directory
	         File f = new File(FilePath);
	         //String files[] = f.list();

	         //for (int i=0; i<files.length; i++) {
	            FileInputStream fi = new 
	              FileInputStream(f);
	            origin = new 
	              BufferedInputStream(fi, BUFFER);
	            ZipEntry entry = new ZipEntry(FilePath);
	            out.putNextEntry(entry);
	            int count;
	            while((count = origin.read(data, 0, 
	              BUFFER)) != -1) {
	               out.write(data, 0, count);
	            }
	            origin.close();
	         //}
	         out.close();
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	}

	public static void DeleteFile(String FilePath){
		
		try{
			
			File filePath = new File(FilePath);
			filePath.delete();
		}catch (Exception e){
			e.printStackTrace();
		}		
	}
}
