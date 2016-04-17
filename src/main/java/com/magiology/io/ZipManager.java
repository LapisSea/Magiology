package com.magiology.io;

import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.swing.ImageIcon;

import org.apache.commons.lang3.ArrayUtils;

public class ZipManager{
	public static ZipManager instance=new ZipManager();
	public static void extractFileFromZip(String zipPath,String filePath,String... inZipFileNames){
		for(String name:inZipFileNames)extractFileFromZip(zipPath, filePath, name);
	}
	public static void extractFileFromZip(String zipPath,String filePath,String inZipFileName){
		try{
			filePath+="\\"+inZipFileName;
			if(!new File(filePath).createNewFile())return;
			OutputStream out = new FileOutputStream(filePath);
			FileInputStream fin = new FileInputStream(zipPath);
			BufferedInputStream bin = new BufferedInputStream(fin);
			ZipInputStream zin = new ZipInputStream(bin);
			ZipEntry ze=null;
			while((ze=zin.getNextEntry())!=null){
				if(ze.getName().equals(inZipFileName)){
					byte[]buffer=new byte[8192];
					int len;
					while((len=zin.read(buffer))!=-1)out.write(buffer, 0, len);
					out.close();
					break;
				}
			}
			zin.close();
			out.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static ImageIcon[] getAllImagesFromZip(String zipName){
		String[] names={};
		ZipFile zipFile=null;
		if(!zipName.endsWith(".zip"))zipName+=".zip";
		try{
			zipFile = new ZipFile(zipName);
			Enumeration<?extends ZipEntry>e=zipFile.entries();
			while(e.hasMoreElements()){
				String name=e.nextElement().getName();
				if(name.endsWith(".png"))names=ArrayUtils.add(names, name);
			}
		}catch(Exception e){e.printStackTrace();}
		if(zipFile!=null)try{zipFile.close();}catch(IOException e){e.printStackTrace();}
		return getImagesFromZip(zipName, names);
	}
	
	public static ImageIcon getImageFromZip(String zipName,String imageName){
		return getImagesFromZip(zipName, new String[]{imageName})[0];
	}
	public static ImageIcon[] getImagesFromZip(String zipName,String... imageNames){
		if(imageNames.length==0)return null;
		for(int a=0;a<imageNames.length;a++)if(!imageNames[a].endsWith(".png"))imageNames[a]+=".png";
		try{
			if(!zipName.endsWith(".zip"))zipName+=".zip";
			ImageIcon[] result=new ImageIcon[imageNames.length];
			for(int id=0;id<imageNames.length;id++){
				String fileName=imageNames[id];
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ZipFile zf = new ZipFile(zipName);
				ZipEntry entry = zf.getEntry(fileName);
				InputStream in = zf.getInputStream(entry);
				byte[] buffer = new byte[4096];
				for(int n;(n=in.read(buffer))!=-1;)out.write(buffer,0,n);
				in.close();
				zf.close();
				out.close();
				ImageIcon img=new ImageIcon();
				img.setImage(Toolkit.getDefaultToolkit().createImage(out.toByteArray()));
				result[id]=img;
			}
			return result;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
