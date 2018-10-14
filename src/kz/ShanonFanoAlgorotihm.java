/**
 * 
 */
package kz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author lenevo
 *
 */
public class ShanonFanoAlgorotihm {


	private  List<String>characters=null;
	private  List<Double>values=null;
	private List<String>encodedSymbols=null;
	private  int totalCountSymbols=0;
	private String filename="";
	private  List<Double>leng=null;
	
	public ShanonFanoAlgorotihm() {
	}
	
	
	/**
	 * @param filename
	 */
	public  void readTxt(String filename){
		this.filename=filename;
		characters=new LinkedList<String>();
		values=new LinkedList<Double>();
		
		File file=new File(this.filename);
		BufferedReader bfile=null;
		try{
			bfile=new BufferedReader(new FileReader(file));		
			while(bfile.ready()){
				String arr=bfile.readLine();
				for (char c : arr.toCharArray()) {			
						if(characters.contains(c+"")){
							int index=characters.indexOf(c+"");
							double num=values.get(index).doubleValue();
							num++;
							values.set(index, num);
							//System.out.println("characters["+index+"]="+c+" -> "+num);
						}else {
							characters.add(c+"");
							values.add(1.0);
							//System.out.println("new Char["+characters.indexOf(c)+"] -> 0");
						}
					//System.out.println(c+" : "+symbols.get(c));
				}
				if(characters.contains("/n")){
					int index=characters.indexOf("/n");
					double num=values.get(index).doubleValue();
					num++;
					values.set(index, num);
				}else{
					characters.add("/n");
					values.add(1.0);
				}
				totalCountSymbols+=arr.length()+1;
			}
			bfile.close();
		}catch(IOException e){
			System.err.println("File Not Found : "+filename);
			System.exit(0);
		}
		
		for(int i=0; i<characters.size(); i++){
			System.out.println(i+") "+characters.get(i)+" = "+values.get(i));
		}
	}


	public void countProbabilty() {
		for(int i=0; i<characters.size(); i++){
			double probability=values.get(i)/totalCountSymbols;
			//System.out.println(characters.get(i)+" -> "+probability);
			values.set(i, probability);
		}
		
		for(int i=0; i<values.size()-1; i++){
			for(int j=i+1; j<values.size(); j++){
				if(values.get(i)<values.get(j)){
					String str=characters.get(i);
					double tmp=values.get(i);
					characters.set(i,characters.get(j));
					values.set(i, values.get(j));
					characters.set(j,str);
					values.set(j, tmp);
				}
			}
		}
		
		encodedSymbols=new LinkedList<String>();
		
		for(int i=0; i<characters.size(); i++){
			System.out.println(i+") "+characters.get(i)+" = "+values.get(i));
			encodedSymbols.add("");
		}
		
		
	}
	
	public void entropu(){
		leng=new LinkedList<Double>();
		double sum=0.0;
		for(int i=0; i<encodedSymbols.size(); i++){
			leng.add(encodedSymbols.get(i).length()*1.0);
		}
		for(int i=0; i<encodedSymbols.size(); i++){
			sum+=values.get(i)*leng.get(i);
		}
		
		System.out.println("=======================================================");
		System.out.println("Sum: "+ sum);
		System.out.println("=======================================================");
	}
	//int r=1, l=2;
	public void encodingLetters(int start, int end, String str) {
		double right=0.0;
		for(int i=start; i<end; i++){
			encodedSymbols.set(i, encodedSymbols.get(i)+str);
			right+=values.get(i);
			//System.out.println(i+encodedSymbols.get(i));
		}
		
		int minIndex=0;
		double left=0.0, min=9999;
		
		for(int i=start; i<end; i++){
			left+=values.get(i);
			right-=values.get(i);
			//System.out.println("left : "+left);
			//System.out.println("right : "+right);
			if(right>left){
				if(min>(right-left)){
					minIndex=i;
					min=right-left;
				}
			}else{
				if(min>(left-right)){
					minIndex=i;
					min=left-right;
				}
				break;
			}
		}
		//System.out.println("Minimum : "+min);
		//System.out.println("Min Index: "+minIndex);
		
		
		if(start==minIndex){
			encodedSymbols.set(start, encodedSymbols.get(start)+"0");
		}else if(minIndex!= end-1){
			//System.out.println("Step: "+(r));
			encodingLetters(start,minIndex+1,"0");
		}
		if((minIndex+1)==(end-1)){
			encodedSymbols.set(end-1, encodedSymbols.get(end-1)+"1");
		} else if((minIndex+1)!=end){
			//System.out.println("Step: "+(l));
			encodingLetters(minIndex+1,end,"1");
		}
		
	}

	public void encodingText(){
		BufferedReader br=null;
		String str="";
		try{
			br=new BufferedReader(new FileReader(this.filename));	
			//System.out.println("Start Reading and Encoding file : can it read: "+br.ready());
			while(br.ready()){
				String arr=br.readLine();
				for (char c : arr.toCharArray()) {
					if(characters.contains(c+"")){
						str+=encodedSymbols.get(characters.indexOf(c+""));
						//System.out.println(str);
					}else {
						System.err.println("Error");
					}
				}
				str+=encodedSymbols.get(characters.indexOf("/n"));
				
			}
			br.close();
		}catch(IOException e){
			System.err.println("File Not Found : "+filename);
			System.exit(0);
		}
		
		br=null;
		BufferedWriter bw=null;
		File writeFile=new File("encodedText.txt");
		try{
			bw =new BufferedWriter(new FileWriter(writeFile));
			bw.write(str);
			bw.close();
			bw=null;
		}catch(IOException e){
			System.err.println("Can't write to file : "+writeFile);
			System.exit(0);
		}
		
	}
	
	public void decodingText(String filename){
		BufferedReader bf=null;
		BufferedWriter bw=null;
		try{
			bf =new BufferedReader(new FileReader(filename));
			bw = new BufferedWriter(new FileWriter("decodedText.txt"));
			while(bf.ready()){
				char [] str=bf.readLine().toCharArray();
				String letter="", line="";
				for (char c : str) {
					letter+=(c+"");
					if(encodedSymbols.contains(letter)){
						if(!characters.get(encodedSymbols.indexOf(letter)).equals("/n")){
							line+=characters.get(encodedSymbols.indexOf(letter));
						}else{
							line+="\n";
						}
						letter="";
					}
				}
				bw.write(line);
			}
			bw.close();
			bf.close();
		}catch(IOException e){
			System.err.println("Can't find File : "+filename);
		}
		System.out.println("Completed decoding\n");
	}
	
	/**
	 * @param file
	 */
	public void encodingToHammingCode(String file){
		BufferedReader bf=null;//для чтение файла
		BufferedWriter bw=null;//для написание в файл
		try{
			bf=new BufferedReader(new FileReader(file));
			bw=new BufferedWriter(new FileWriter("encodedHammingCode.txt"));
			while(bf.ready()){
				char line[]=bf.readLine().toCharArray(); 
				String encodedLine="";
				for(int i=0; i<line.length-3; i+=4){
					//System.out.println(line[i]+" "+line[i+1]+" "+line[i+2]+" "+line[i+3]);
					int r1=Character.getNumericValue(line[i])^Character.getNumericValue(line[i+1])^Character.getNumericValue(line[i+2]);
					int r2=Character.getNumericValue(line[i+1])^Character.getNumericValue(line[i+2])^Character.getNumericValue(line[i+3]);
					int r3=Character.getNumericValue(line[i])^Character.getNumericValue(line[i+1])^Character.getNumericValue(line[i+3]);
					//System.out.println(r1+" "+r2+" "+r3);
					encodedLine+=""+line[i]+line[i+1]+line[i+2]+line[i+3]+r1+r2+r3;
					//System.out.println(encodedLine);
				}
				if(line.length%4!=0){//Checking for last digits
					int x=line.length%4;
					for(int i=x; i>0; i--){
						encodedLine+=""+line[line.length-i];
					}
				}
				bw.write(encodedLine);
			}
			bf.close();bw.close();
		}catch(IOException e){
			System.err.println("Can't read File : "+file+"\n in method public void encodingToHammingCode(String file)\n "+e.getMessage());
		}
		System.out.println("Completed encoding to Hamming Code\n");
	}
	
	/**
	 * @param file
	 */
	public void addError(String file){
		BufferedReader bf=null;//для чтение файла
		BufferedWriter bw=null;//для написание в файл
		try{
			bf=new BufferedReader(new FileReader(file));
			bw=new BufferedWriter(new FileWriter("hammingCodeWithErrors.txt"));
			while(bf.ready()){
				char line[]=bf.readLine().toCharArray(); 
				String encodedLine="";
				for(int i=0; i<line.length-6; i+=7){
					int index=(int)(Math.random()*7)+i;
					if(line[index]=='0')line[index]='1';
					else line[index]='0';
					encodedLine+=""+line[i]+line[i+1]+line[i+2]+line[i+3]+line[i+4]+line[i+5]+line[i+6];
					//System.out.println(index);
				}
				if(line.length%7!=0){//Checking for last digits
					int x=line.length%7;
					for(int i=x; i>0; i--){
						encodedLine+=""+line[line.length-i];
					}
				}
				bw.write(encodedLine);
			}
			bf.close();bw.close();
		}catch(IOException e){
			System.err.println("Can't read File : "+file+"\n in method public void addError(String file)\n "+e.getMessage());
		}
		System.out.println("Adding errors completed");
		
	}
	
	
	/**
	 * @param file
	 */
	public void fixErrorsAndDecodeHammingCode(String file){
		BufferedReader bf=null;//для чтение файла
		BufferedWriter bw=null;//для написание в файл
		                        //     i1   i2     i3    i4   r1    r2     r3     i1   i2   i3   i4   r1   r2    r3
		final String SYNDROME[][]={{ "101","111","110","011","100","010","001"}, {"0", "1", "2", "3", "4", "5", "6"}};
		try{
			bf=new BufferedReader(new FileReader(file));
			bw=new BufferedWriter(new FileWriter("decodedHammingCode.txt"));
			while(bf.ready()){
				char line[]=bf.readLine().toCharArray(); 
				String str="", decodedLine="";
				for(int i=0; i<line.length-6; i+=7){
					str="";
					int s1=Character.getNumericValue(line[i+4])^Character.getNumericValue(line[i])^Character.getNumericValue(line[i+1])^Character.getNumericValue(line[i+2]);
					int s2=Character.getNumericValue(line[i+5])^Character.getNumericValue(line[i+1])^Character.getNumericValue(line[i+2])^Character.getNumericValue(line[i+3]);
					int s3=Character.getNumericValue(line[i+6])^Character.getNumericValue(line[i])^Character.getNumericValue(line[i+1])^Character.getNumericValue(line[i+3]);
					str+=s1+""+s2+""+s3;
					// System.out.println("\n\nsyndrome: "+str +" index: "+i);
					
					// System.out.println(str.equals("000"));
					if(str.equals("000"))continue;
					int index=99;
					for(int j=0; j<7; j++){
						if(SYNDROME[0][j].equals(str)){
							//System.out.println(SYNDROME[0][j]+" "+SYNDROME[1][j]);
							index=Integer.parseInt(SYNDROME[1][j]);
							break;
						}
					}
					// System.out.println("index: "+index+"\n line[index]: "+line[i+index]);
					// System.out.println(line[i+index]=='0');
					if(line[i+index]=='0'){ line[i+index]='1';}
					else line[i+index]='0';
					// System.out.println("line: "+line[i]+line[i+1]+line[i+2]+line[i+3]);
					decodedLine+=""+line[i]+line[i+1]+line[i+2]+line[i+3];
					
				}//end loop for
				
				if(line.length%7!=0){//Checking for last digits
					int x=line.length%7;
					for(int i=x; i>0; i--){
						decodedLine+=""+line[line.length-i];
					}
				}
				bw.write(decodedLine);
			}
			bf.close();bw.close();
		}catch(IOException e){
			System.err.println("Can't read File : "+file+"\n in method public void decodeHammingCodeAndFixErrors(String file)\n "+e.getMessage());
		}
		bf=null; bw=null;
		System.out.println("Decoding Hamming Code is completed");
	}

	
	/**
	 * @return the characters
	 */
	public List<String> getCharacters() {
		return characters;
	}


	/**
	 * @param characters the characters to set
	 */
	public void setCharacters(List<String> characters) {
		this.characters = characters;
	}


	/**
	 * @return the values
	 */
	public List<Double> getValues() {
		return values;
	}


	/**
	 * @param values the values to set
	 */
	public void setValues(List<Double> values) {
		this.values = values;
	}


	/**
	 * @return the totalCountSymbols
	 */
	public int getTotalCountSymbols() {
		return totalCountSymbols;
	}


	/**
	 * @param totalCountSymbols the totalCountSymbols to set
	 */
	public void setTotalCountSymbols(int totalCountSymbols) {
		this.totalCountSymbols = totalCountSymbols;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ShanonFanoAlgorotihm [characters=" + characters + ", values=" + values + ", totalCountSymbols="
				+ totalCountSymbols + "]";
	}


	/**
	 * @return the encodedSymbols
	 */
	public List<String> getEncodedSymbols() {
		return encodedSymbols;
	}


	/**
	 * @param encodedSymbols the encodedSymbols to set
	 */
	public void setEncodedSymbols(List<String> encodedSymbols) {
		this.encodedSymbols = encodedSymbols;
	}


	public void showEndodedSymbols() {
		// TODO Auto-generated method stub
		for(int i=0; i<values.size(); i++){
			System.out.println(characters.get(i) + "["+i+"] = " + encodedSymbols.get(i));
		}
	}




}

		

	


