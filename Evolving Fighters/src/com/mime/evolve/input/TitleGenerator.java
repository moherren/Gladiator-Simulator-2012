package com.mime.evolve.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.mime.evolve.Game;

public class TitleGenerator {

	public static final int ERS=2272;
	public static final int PLURALS=12903;
	private static String[] ers=createErs();
	private static String[] plurals=createPlurals();

	private static String[] createErs(){
		try{
			String[] ers=new String[ERS];
			Scanner scan;
			scan = new Scanner(new File("ers.txt"));
			for(int i=0;i<ERS&&scan.hasNext();i++){
				ers[i]=scan.nextLine();
			}
			scan.close();
			return ers;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	private static String[] createPlurals(){
		try{
			String[] plurals=new String[PLURALS];
			Scanner scan;
			scan = new Scanner(new File("plurals.txt"));
			for(int i=0;i<PLURALS&&scan.hasNext();i++){
				plurals[i]=scan.nextLine();
			}
			scan.close();
			return plurals;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String generateTitle(){
		String er="",plural="";
		while(er.equals(""))
			er=ers[(int)(Game.rand.nextDouble()*ERS)];
		while(plural.equals(""))
			plural=plurals[(int)(Game.rand.nextDouble()*PLURALS)];
		return er+" of "+plural;
	}

}
