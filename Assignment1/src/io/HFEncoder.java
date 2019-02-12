package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class HFEncoder 
{
	public static void main(String[] args) throws IOException, InsufficientBitsLeftException 
	{
		System.out.println("Hello");
		
		String input_file_name = "data/uncompressed.txt";
		String output_file_name = "data/recompressed.txt";

		FileReader fr = new FileReader(input_file_name); 
		
		//Find the ocurrences of each symbol in file
		int[] symbol_counts = symbolCounter(fr);
		
		int numsym = 0;
		for(int i=0; i<symbol_counts.length; i++)
		{
			//find total number of symbols in file
			numsym+=symbol_counts[i];
		}

		fr.close();
		
		//calculate probabilities
		double[] prob = new double[256];
		  
		 for(int i=0; i<freq.length; i++)
		 {
			 prob[i] = (double)freq[i]/ (double) numsym;
		 }
		
		// Construct the optimal, minimum variance Huffman tree
		CNode head = buildTree(symbol_counts);
		
		//Build code table according to length of symbols
		String[] stcode = new String[256];
        buildCodeTable(stcode, head, "");
        
		
		FileOutputStream fos = new FileOutputStream(output_file_name);
		OutputStreamBitSink bit_sink = new OutputStreamBitSink(fos);
		
		//list with occurrences ordered by code
		ArrayList<CNode> E1 = codeList(stcode);
		
		System.out.println("Encoding...");
		for(int i=0; i<E1.size(); i++)
		{
			//write in file lenghts for each symbol
			bit_sink.write(E1.get(i)._len, 8);
		}
		//write total characters in file
		bit_sink.write(numsym, 32);
		
		//re sort list by lenghts 
		E1.sort((CNode c1, CNode c2) -> c1._len - c2._len);	
		
		//construct canonical huffman tree 
		HCtree T2 = new HCtree();
		
		for(int i = 0 ; i < E1.size(); i ++)
		{
				T2.insert(E1.get(i));
		}

		
		String[] stcode2 = new String[256];
		CNode canroot = T2._root;
		
		//new string array that holds the symbol's code according to canonical tree
        buildCodeTable(stcode2, canroot, "");
	
        
		fr = new FileReader(input_file_name);
		//write symbol codes in file right after number of characters
		symbolWriter(fr, stcode2, bit_sink);
		
		
		// Pad output to next word.
		bit_sink.padToWord();

		// Close files.
		fr.close();
		fos.close();
		
		System.out.println("Done with Encoding");
	}
	
	public static int[] symbolCounter(FileReader fin) throws IOException
	{
		
		int[] symbol_counts = new int[256];
		int[] symbols = new int[256];
		for (int i=0; i<256; i++) 
		{
			symbols[i] = i;
			symbol_counts[i] = 0;
		}

		int ks; 
		while ((ks=fin.read()) != -1) 
		{
			if(symbols[ks] == ks)
			{
				
				symbol_counts[ks]++;				
			}
		}
		
		return symbol_counts;
	}
	
	public static void symbolWriter(FileReader fin, String[] codes, OutputStreamBitSink bits) throws IOException
	{
  
		int ks; 
		while ((ks=fin.read()) != -1) 
		{
			if(codes[ks] != null)
			{
				bits.write(codes[ks]);
			}
			
		}		
	}
	
	
	private static CNode buildTree(int[] freq) 
	{
		
        // initialze priority queue with singleton trees
        PriorityQueue<CNode> pq = new PriorityQueue<>((x, y)-> x._len - y._len)
        		;
        for (int i = 0; i < 256; i++)
            if (freq[i] > 0)
            {
            	CNode inword = new CNode(freq[i], i, null, null);
            	//System.out.println(inword);
            	pq.add(inword);
            	
            }
        

        // special case in case there is only one character with a nonzero frequency
        if (pq.size() == 1) 
        {
            if (freq['\0'] == 0) 
            	pq.add(new CNode(0, '\0', null, null));
            else                 
            	pq.add(new CNode(0, '\1', null, null));
        }

        // merge two smallest trees
        while (pq.size() > 1) 
        {
            CNode left  = pq.remove();
            CNode right = pq.remove();
            CNode parent = new CNode(left._len + right._len, -1, left, right);
            pq.add(parent);
        }
        
        
        return pq.remove();
        

		/*
		 *  @author Robert Sedgewick
		 *  @author Kevin Wayne
		 */
		
		// code adapted from https://algs4.cs.princeton.edu/55compression/Huffman.java.html
    }
	
	
	  public static void buildCodeTable(String[] st, CNode x, String s) 
	  {
	        if (!x.isleaf) 
	        {
	            buildCodeTable(st, x.left,  s + '0');
	            buildCodeTable(st, x.right, s + '1');
	        }
	        else 
	        {
	            st[x._code] = s;
	        }
	        

			/*
			 *  @author Robert Sedgewick
			 *  @author Kevin Wayne
			 */
			
			// code adapted from https://algs4.cs.princeton.edu/55compression/Huffman.java.html
	   }
	  
	  public static ArrayList<CNode> codeList(String[] stlen)
	  {
		  ArrayList<CNode> L1 = new ArrayList<CNode>();

			for(int i = 0 ; i < 256; i ++)
			{
				if(stlen[i] != null)
				{
					int slen = stlen[i].length(); 
					int symbol = i;
					CNode word = new CNode(slen, symbol);
					
					L1.add(word);
				}	
				
				else
				{
					int slen = 0; 
					int symbol = i;
					CNode word = new CNode(slen, symbol);
					
					L1.add(word);
					
				}

			}
			
			L1.sort((CNode c1, CNode c2) -> c1._code - c2._code);	
			
			return L1;
	  }

}




