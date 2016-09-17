package com.magiology.io;

import static com.magiology.core.MReference.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;

import com.magiology.core.MReference;
import com.magiology.core.Magiology;

public class IOManager{
	
	private List<File> files=new ArrayList<>(),folders=new ArrayList<>();
	
	public void addFolder(String path){
		folders.add(new File(path));
	}
	public void addFile(String path){
		files.add(new File(path));
	}
	
	public IOManager(){
		File root=new File(getRoot());
		if(!root.exists()||!root.isDirectory()){
			root.mkdirs();
		}
	}
	
	public String load(String fileName){
		File root=new File(getRoot()+fileName);
		if(root.exists()&&root.isFile()&&root.canRead()){
			try{
				return FileUtils.readFileToString(root);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public void checkAndExtract(){
		File versionFile=new File(getRoot()+"version.check");
		boolean versionOk=false;
		if(versionFile.exists()&&versionFile.isFile()){
			try{
				String content=FileUtils.readFileToString(versionFile);
				versionOk=content.equals(MReference.MD5_ID);
			}catch(IOException e){}
		}
		if(versionOk)return;
		File root=new File(getRoot());
		try{
			FileUtils.forceDelete(root);
		}catch(IOException e2){
			e2.printStackTrace();
		}
		root.mkdirs();
		folders.forEach((f)->new File(getRoot()+f.getPath()).mkdirs());
		try{
			versionFile.createNewFile();
			FileUtils.write(versionFile, MReference.MD5_ID);
		}catch(IOException e1){
			e1.printStackTrace();
		}
		
		ZipFile zipFile=null;
		try{
			zipFile=new ZipFile(new String(Magiology.isDev()?"mods/DummyJar.zip":MReference.SOURCE_FILE));
			ZipFile zipFile2=zipFile;
			files.forEach((fileBase)->{
				File finalFile=new File(getRoot()+fileBase.getPath());
				InputStream inputStream=null;
				try{
					new File(finalFile.getParent()).mkdirs();
					if(finalFile.exists())finalFile.delete();
					finalFile.createNewFile();
					String path="files/"+fileBase.getPath().replace('\\', '/');
					try{
						inputStream=zipFile2.getInputStream(zipFile2.getEntry(path));
					}catch(NullPointerException e){
						if(e.getMessage().equals("entry")){
							throw new NullPointerException("entry - "+path);
						}
					}
					FileUtils.copyInputStreamToFile(inputStream, finalFile);
				}catch(IOException e){
					e.printStackTrace();
				}
				if(inputStream!=null){
					try{
						inputStream.close();
					}catch(IOException e){
						e.printStackTrace();
					}
				}
			});
		}catch(IOException e){
			e.printStackTrace();
		}
		if(zipFile!=null){
			try{
				zipFile.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public String getRoot(){
		return "mods/"+MC_VERSION+"/"+MODID+"/";
	}

}
