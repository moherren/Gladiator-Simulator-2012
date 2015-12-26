package com.mime.evolve.input;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class RandomNameGenerator{
	private static ArrayList<String>[] starts=loadListDouble("wordList/engStarts.txt");
	private static ArrayList<String>[] ends=loadListDouble("wordList/engEndings.txt");
	private static ArrayList<String>[] vowels=loadListDouble("wordList/engVowels.txt");
	private static ArrayList<String> ers=loadList("wordList/ers.txt");
	private static ArrayList<String> plurals=loadList("wordList/plurals.txt");
	private static ArrayList<String> nouns=loadList("wordList/nouns.txt");
	private static ArrayList<String> verbs=loadList("wordList/verbs.txt");
	private static ArrayList<String> adjectives=loadList("wordList/adjectives.txt");
	private static ArrayList<String> names=loadList("names/names.txt");
	private static Random rand=new Random();
	private static String[] titleForms={"the /r","the /r of /p","the /n","the /a /n","the /r of the /a /n","the /r of /w"};
	private static String[] nameForms={"/m /m","/m \"/t\" /m",
		"/m","/m \"/t\"","/m /m of /w","/m of /w","/m of the /a /p","/t"
	};
	
	public static String generateName(){
		String form=nameForms[rand.nextInt(nameForms.length)],name="";
		for(int i=0;i<form.length();i++){
			if(form.charAt(i)=='/'){
				switch(form.charAt(i+1)){
				case 't':
					name+=generateTitle();
					i++;
					break;
				case 'w':
					name+=generateWord((int)(randTriangle()*2)+1);
					i++;
					break;
				case 'p':
					name+=plurals.get(rand.nextInt(plurals.size()));
					i++;
					break;
				case 'n':
					name+=nouns.get(rand.nextInt(nouns.size()));
					i++;
					break;
				case 'm':
					name+=names.get(rand.nextInt(names.size()));
					i++;
					break;
				case 'v':
					name+=verbs.get(rand.nextInt(verbs.size()));
					i++;
					break;
				case 'a':
					name+=adjectives.get(rand.nextInt(adjectives.size()));
					i++;
					break;
				default:
					name+='/';
				}
			}
			else
				name+=form.charAt(i);
		}
		
		return name;
	}
	
	public static String generateSyllable(){
		ArrayList<String> list=starts[(int)(randTriangle()*3)];
		String start=list.get(rand.nextInt(list.size()));
		list=vowels[rand.nextInt(2)];
		String vowel=list.get(rand.nextInt(list.size()));
		list=ends[rand.nextInt(2)];
		String end=list.get(rand.nextInt(list.size()));
		return start+vowel+end;
	}
	
	public static String generateSyllable(double startChance,double endChance){
		String start="",end="";
		ArrayList<String> list;
		if(rand.nextDouble()<startChance){
			list=starts[(int)(randTriangle()*starts.length)];
			start=list.get(rand.nextInt(list.size()));
		}
		list=vowels[rand.nextInt(vowels.length)];
		String vowel=list.get(rand.nextInt(list.size()));
		if(rand.nextDouble()<endChance){
			list=ends[rand.nextInt(ends.length)];
			end=list.get(rand.nextInt(list.size()));
		}
		return start+vowel+end;
	}
	
	public static String generateTitle(){
		String form=titleForms[rand.nextInt(titleForms.length)],title="";
		for(int i=0;i<form.length();i++){
			if(form.charAt(i)=='/'){
				switch(form.charAt(i+1)){
				case 'r':
					title+=ers.get(rand.nextInt(ers.size()));
					i++;
					break;
				case 'p':
					title+=plurals.get(rand.nextInt(plurals.size()));
					i++;
					break;
				case 'n':
					title+=nouns.get(rand.nextInt(nouns.size()));
					i++;
					break;
				case 'v':
					title+=verbs.get(rand.nextInt(verbs.size()));
					i++;
					break;
				case 'a':
					title+=adjectives.get(rand.nextInt(adjectives.size()));
					i++;
					break;
				case 'm':
					title+=names.get(rand.nextInt(names.size()));
					i++;
					break;
				case 'w':
					title+=generateWord((int)(randTriangle()*2)+1);
					i++;
					break;
				default:
					title+='/';
				}
			}
			else
				title+=form.charAt(i);
		}
		return title;
	}
	
	public static String generateWord(int syllables){
		String name="";
		for(int i=0;i<syllables;i++)
			name+=generateSyllable(0.8,1);
		name=name.substring(0,1).toUpperCase()+name.substring(1,name.length());
		return name;
	}

	public static ArrayList<String> loadList(String fileName){
		try{
			ArrayList<String> list=new ArrayList<String>();
			Scanner scan;
			scan = new Scanner(new File(fileName));
			while(scan.hasNext()){
				list.add(scan.next());
			}
			scan.close();
			return list;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static ArrayList<String>[] loadListDouble(String fileName){
		try{
			Scanner scan=new Scanner(new File(fileName));
			ArrayList<String>[] words=new ArrayList[scan.nextInt()];
			for(int i=0;i<words.length;i++)
				words[i]=new ArrayList<String>();
			int i=0;
			while(scan.hasNext()){
				String load=scan.next();
				if(!load.equals("~~~"))
					words[i].add(load);
				else
					i++;
			}
			scan.close();
			return words;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException{
		while(true){
			Scanner scan=new Scanner(System.in);
			scan.nextLine();
			System.out.println(generateName());
		}
	}
	
	public static double randTriangle(){
		return 1-Math.sqrt(1-rand.nextDouble());
	}
}
