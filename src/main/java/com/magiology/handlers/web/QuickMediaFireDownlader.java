package com.magiology.handlers.web;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.magiology.util.utilclasses.PrintUtil;

public final class QuickMediaFireDownlader{
	
	private static String filePathC;
	
	/**
	 * get the download link by clicking on the file and selecting the share option
	 */
	public static void downlad(String filePath,String URL){
		filePathC=filePath;
		if(!URL.startsWith("http://www.mediafire.com/"))throw new IllegalStateException("MediaFireDownlader can download only from links from MediaFire!");
		try{
			String downloadLink=findingValidLink(getUrlSrc(URL));
			saveToDisc(filePath,downloadLink);
		}catch(Exception e){e.printStackTrace();}
	}
	/**
	 * get the download link by clicking on the file and selecting the share option
	 */
	public static void downladAndName(String filePath,String URL){
		filePath+=URL.substring(URL.lastIndexOf("/"));
		downlad(filePath, URL);
	}
	private static String findingValidLink(String string){
		PrintUtil.println("Fetching download link.");
		try{
			String code="(?=\\<)|(?<=\\>)";
			String data[]=string.split(code);
			String status="NOTFOUND";
			for(String data1:data){
				if(data1.contains("DLP_mOnDownload(this)")){
				status=data1;
				break;
			}}
			String cleanVersion=status.substring(status.indexOf("href=\"")+6);
			cleanVersion=cleanVersion.substring(0, cleanVersion.indexOf("\""));
			return cleanVersion;
		}catch(Exception e){
			e.printStackTrace();
			return "ERROR";
		}
	}
	
	
	private static String getUrlSrc(String URL)throws Exception{
		PrintUtil.println("Attempting to download "+name(filePathC));
		PrintUtil.println("Connecting!");
		URL link=new URL(URL);
		URLConnection linkCon=link.openConnection();
		BufferedReader reader=new BufferedReader(new InputStreamReader(linkCon.getInputStream(), "UTF-8"));
		String inputLine,total="";
		while((inputLine=reader.readLine())!=null)total+=inputLine;
		reader.close();
		return total;
	}
	private static String name(String string){
		return string.substring(string.lastIndexOf("/")+1);
	}
	private static void saveToDisc(String filePath,String URL)throws Exception{
		PrintUtil.println("Downloading "+name(filePath));
		BufferedInputStream in=null;
		FileOutputStream fout=null;
		try{
			
			new File(filePath.substring(0,filePath.lastIndexOf("/"))).mkdirs();
			
			in=new BufferedInputStream(new URL(URL).openStream());
			fout=new FileOutputStream(filePath);
			final byte data[]=new byte[1024];
			int count;
			while(-1!=(count=in.read(data, 0, 1024)))fout.write(data, 0, count);
		}finally{
			if(in!=null)in.close();
			if (fout!=null)fout.close();
		}
		PrintUtil.println(name(filePath)+" is successfuly downladed!");
	}
	private QuickMediaFireDownlader(){}
}