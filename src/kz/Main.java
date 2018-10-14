/**
 * 
 */
package kz;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.List;

/**
 *
 *
 */
public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Running project...");
		ShanonFanoAlgorotihm process=new ShanonFanoAlgorotihm();
		
		System.out.println("Read Text\n");
		process.readTxt("text.txt");// File name text.txt
		
		System.out.println("\nTotal Count of Symbols: "+process.getTotalCountSymbols()+"\n");
		
		System.out.println("\nCounting probability:");
		process.countProbabilty();
		
		System.out.println("\nEncoding Letters: ");
		process.encodingLetters(0, process.getValues().size(),"");
		
		
		process.showEndodedSymbols();
		
		
		System.out.println("\nEncoding Text and Save to encodedText.txt");
		process.encodingText();
		
		System.out.println("\nEncoding binary sequence with Hamming Code: ");
		process.encodingToHammingCode("encodedText.txt");//File name  encodedText.txt
		
		System.out.println("\nAdd randomly errors To check : ");
		process.addError("encodedHammingCode.txt");//File name  encodedHammingCode
		
		System.out.println("\nDecoding Hamming Code and Fixing errors: ");
		process.fixErrorsAndDecodeHammingCode("hammingCodeWithErrors.txt");//File Name hammingCodeWithErrors
		
		System.out.println("\nDecoding Text and Save to decocededText.txt");
		process.decodingText("decodedHammingCode.txt");// File name encodedText.txt
		
		process.entropu();
	}
		
		



}
