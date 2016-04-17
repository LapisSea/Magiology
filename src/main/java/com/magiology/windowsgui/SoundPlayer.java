package com.magiology.windowsgui;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import net.minecraftforge.fml.common.FMLCommonHandler;

public class SoundPlayer{

	/**
	 * @param filename the name of the file that is going to be played
	 */
	public static void playSound(String filename){
		final int BUFFER_SIZE = 128000;
		File soundFile = null;
		AudioInputStream audioStream = null;
		AudioFormat audioFormat;
		SourceDataLine sourceLine = null;
		String strFilename = filename;

		try {
			soundFile = new File(strFilename);
		} catch (Exception e) {
			e.printStackTrace();
			FMLCommonHandler.instance().exitJava(404, false);
		}

		try {
			audioStream = AudioSystem.getAudioInputStream(soundFile);
		} catch (Exception e){
			e.printStackTrace();
			FMLCommonHandler.instance().exitJava(404, false);
		}

		audioFormat = audioStream.getFormat();

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
		try {
			sourceLine = (SourceDataLine) AudioSystem.getLine(info);
			sourceLine.open(audioFormat);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			FMLCommonHandler.instance().exitJava(404, false);
		} catch (Exception e) {
			e.printStackTrace();
			FMLCommonHandler.instance().exitJava(404, false);
		}

		sourceLine.start();

		int nBytesRead = 0;
		byte[] abData = new byte[BUFFER_SIZE];
		while (nBytesRead != -1) {
			try {
				nBytesRead = audioStream.read(abData, 0, abData.length);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (nBytesRead >= 0) {
			}
		}

		sourceLine.drain();
		sourceLine.close();
		try{
			audioStream.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}