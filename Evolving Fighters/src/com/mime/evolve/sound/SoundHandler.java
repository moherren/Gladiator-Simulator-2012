package com.mime.evolve.sound;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundHandler {

	/**
	 * A guttural, quick grunt "OOAH"
	 */
	public static String GRUNT_ONE = "SoundSource/Grunt_1.wav";
	/**
	 * A guttural, quick grunt "AH"
	 */
	public static String GRUNT_TWO = "SoundSource/Grunt_2.wav";
	/**
	 * A booming voice saying "Colosseum Simulator 2012"
	 */
	public static String VOICE_ONE = "SoundSource/Voice_1.wav";
	/**
	 * A fast swinging noise from a rod
	 */
	public static String FAST_ONE = "SoundSource/Fast_1.wav";

	
	/**
	 * "Mighty And Meek" potential fight music
	 */
	public static String SONG_ONE = "SoundSource/Song_1.wav";
	
	public static Clip music=null;
	
	public static void play(String ref) {
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(
					ref));
			Clip clip = AudioSystem.getClip();

			clip.open(audio);

			clip.start();

		} catch (Exception e) {
			System.out.println("check " + ref + "\n");
			e.printStackTrace();
		}
	}

	public static void play(String ref, float deltaGain) {
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(
					ref));
			Clip clip = AudioSystem.getClip();

			clip.open(audio);

			FloatControl gainControl = (FloatControl) clip
					.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(deltaGain); // Reduce volume by deltaGain

			clip.start();

		} catch (Exception e) {
			System.out.println("check " + ref + "\n");
			e.printStackTrace();
		}
	}

	
	
	public static void play(String ref, float deltaGain, float deltaPitch) {
		try {
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(
					ref));
			Clip clip = AudioSystem.getClip();

			clip.open(audio);

			FloatControl gainControl = (FloatControl) clip
					.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(deltaGain); // Reduce volume by deltaGain

			FloatControl pitchControl = (FloatControl) clip
					.getControl(FloatControl.Type.SAMPLE_RATE);
			pitchControl.setValue(deltaPitch);

			clip.start();

		} catch (Exception e) {
			System.out.println("check " + ref + "\n");
			e.printStackTrace();
		}
	}
	
	public static void playMusic(String ref,float deltaGain){
		
		try {
			if(!(music==null))
			music.stop();
			
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(
					ref));
			
			music=AudioSystem.getClip();
			
			music.open(audio);
			
			FloatControl gainControl = (FloatControl) music
					.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(deltaGain);
			
			music.loop(9999);
			
			music.start();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
