
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.* ;

/*  Author: Yi Song
 *  E-mail: yis1@andrew.cmu.edu
 *  Phone: 412-608-4515
 *  Using brute forcing, with n=1000 O(n^2 log n) might as well work.
 *  Improvement Pending: narrow down search area.
 * 	
 */
public class Solution{
	 
	public static int []Topic;
	//public static int []TopicId;
	
	public static double [][] TopicCo;
	public static Map<Integer, ArrayList<Integer>> Questions;
	public static Map<Integer, ArrayList<Integer>> TopicWithQuestion;
	public static ArrayList<ArrayList<Double>> Tdistance;
	 public static BufferedWriter out; 
	    static { 
	    	try {    
	    	  out = new BufferedWriter(new OutputStreamWriter(System.out));
	    	}
	    	catch (Exception e) {
	    	  e.printStackTrace();
	    	}
	    }
	    
	
	public static void main(String args[]) throws Exception{
        
		//File input = new File("/Users/songyi/Desktop/Nearby/4.txt");
		//Scanner stdin = new Scanner(input);  
		  FileReader filereader = new FileReader("/Users/songyi/Desktop/Nearby/4.txt");
		  BufferedReader stdin = new BufferedReader(filereader);
		 // BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
	    	
		//Scanner stdin = new Scanner(System.in);  
		 
		 
		 String[] Line = stdin.readLine().split(" ");
		 int T = Integer.parseInt(Line[0]);
		 int Q = Integer.parseInt(Line[1]);
		 int N = Integer.parseInt(Line[2]);		 
		// System.out.println(T +" "+ Q +" "+ N);
		 Topic = new int[T];
		 TopicCo = new double [T][2];
		 
		 // read Topic
		 for(int i = 0; i < T; i++){
			 Line = stdin.readLine().split(" ");
			 Topic[i] = Integer.parseInt(Line[0]);
			 TopicCo[i][0] = Double.parseDouble(Line[1]);
			 TopicCo[i][1] = Double.parseDouble(Line[2]);	
		 // System.out.println(Topic[i] +" "+ TopicCo[i][0] +" "+ TopicCo[i][1]);
				
		 }
		 
		 // read Question
		 Questions =new  HashMap<Integer, ArrayList<Integer>>();
		 TopicWithQuestion =new  HashMap<Integer, ArrayList<Integer>>();
			
		 for(int i = 0; i < Q; i++){
			 Line = stdin.readLine().split(" ");
			 int qid = Integer.parseInt(Line[0]);
			 
			 
			 ArrayList <Integer> qlist = new ArrayList<Integer>();
			 for (int j = 2; j < Line.length; j++){
				 int tid = Integer.parseInt(Line[j]);
				 qlist.add(tid);
				 
				 // add question id to Topic 
				 if (TopicWithQuestion.get(tid)==null){
					 ArrayList <Integer> tqlist = new ArrayList<Integer>();
					 tqlist.add(qid);

					 TopicWithQuestion.put(tid, tqlist);	
				 }
				 else { 
					 TopicWithQuestion.get(tid).add(qid);
				 }
				 

			 }
			 Questions.put(qid, qlist);
		  }
		 
		 
		// Read Queries  Brute Forse
		for(int i = 0; i < N; i++){
			Line = stdin.readLine().split(" ");
			char Qtype = Line[0].charAt(0);
			int Qnum = Integer.parseInt(Line[1]);
			double Qx = Double.parseDouble(Line[2]);
			double Qy = Double.parseDouble(Line[3]);
			Distance(T, Qx, Qy);
			// t type Query
			if (Qtype=='t') {
				
				for(int j = 0; j < Qnum; j++){
					//if (j > 0) System.out.print(" ");					
					double r = Tdistance.get(j).get(0);
					//System.out.print((int)r);
					try{ 
						out.write((int)r + " ");
					}catch (IOException e) {
						e.printStackTrace();
					}
				
				}
			}	
			
			
			else{
			// q type Query	
				
				int z = Qnum;
				ArrayList<Integer> nearestQ = new ArrayList<Integer>();
				ArrayList<Integer> nearestQsub = new ArrayList<Integer>();
				
				for(int j = 0; j < T; j++){
					
					double r = Tdistance.get(j).get(0);
					
					/*if ( (j < T-1)&&(Tdistance.get(j+1).get(1)-Tdistance.get(j).get(1)<0.001) ){

						 if (TopicWithQuestion.get((int)r) != null)
							 nearestQsub.addAll( TopicWithQuestion.get((int)r) );
					
					
					}
					else*/ {
						
					
					 if (TopicWithQuestion.get((int)r) != null)
						 nearestQsub.addAll( TopicWithQuestion.get((int)r) );
						
					 
					 
					 Collections.sort(nearestQsub,new Comparator<Integer>(){
						 public int compare(Integer arg0, Integer arg1){
							 return arg1 - arg0;
						 }
					 });
					
					 for(int k = 0; k < nearestQsub.size(); k++){
						 if (!nearestQ.contains(nearestQsub.get(k)) ) 
							 { 
								
							 nearestQ.add(nearestQsub.get(k));
							 z--;
							 }
					 }
					 nearestQsub.clear();
					 if (z <= 0) break;
					}
				}
				
				
				// in case many same distance topics				
				if (nearestQsub.size()>0) {
					
					 Collections.sort(nearestQsub,new Comparator<Integer>(){
						 public int compare(Integer arg0, Integer arg1){
							 return arg1 - arg0;
						 }
					 });
					
					 for(int k = 0; k < nearestQsub.size(); k++){
						 if (!nearestQ.contains(nearestQsub.get(k)) ) 
							 { 
							 nearestQ.add(nearestQsub.get(k));
							 z--;
							 }
					 }
					 nearestQsub.clear();
				}
				
	
				//output result
				for(int j = 0; j < Math.min(Qnum, nearestQ.size()) ; j++){
					//System.out.print(nearestQ.get(j)+" ");
					
					try{ 
						out.write(nearestQ.get(j) + " ");
					}catch (IOException e) {
						e.printStackTrace();
					}
				
				}
				
				
			}
			
			 try {
				   out.newLine();
			   } catch (IOException e){ 
				   e.printStackTrace();
			   }   
				
				
		}
		
		out.flush();
		out.close();
		
		 	  
	 }
	
	// calculate distance
	public static void Distance(int T, double x, double y){
		Tdistance = new ArrayList<ArrayList<Double>>(); 
		for(int i = 0; i < T; i++){
			ArrayList<Double> elements=new ArrayList<Double>();
			elements.add((double)Topic[i]);
			double distance = (TopicCo[i][0]-x)*(TopicCo[i][0]-x)+(TopicCo[i][1]-x)*(TopicCo[i][1]-x);
			elements.add(distance);			
			Tdistance.add(elements);
		}
		
		Collections.sort(Tdistance,new Comparator<ArrayList<Double>>(){
			public int compare(ArrayList<Double> arg0, ArrayList<Double> arg1){
					
			    if  (Math.abs(arg0.get(1) - arg1.get(1)) < 0.000001) 
			    	return (int) (arg1.get(0) - arg0.get(0));
			    if  (arg0.get(1) - arg1.get(1)>0) return 1;
			    else return -1;
			    
			   	}
			
		});		
	    
		for(int i = 0; i < Tdistance.size(); i++){
		    System.out.println(Tdistance.get(i).get(0)+" "+Tdistance.get(i).get(1));
		}
		
	}
	
}

	
	
