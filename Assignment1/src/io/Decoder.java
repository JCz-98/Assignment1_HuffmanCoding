package io;

import java.io.*;
import java.util.ArrayList;

public class Decoder 
{
	
	public static void main(String [ ] args) throws Exception
	{
		System.out.print("Hello\n\n");
		
		
		
		InputStream compressed = new FileInputStream(new File("data/compressed.dat"));
		InputStreamBitSource br = new InputStreamBitSource(compressed);
		
		ArrayList<CNode> L1 = new ArrayList<CNode>();

		for(int i = 0 ; i < 256; i ++)
		{
				int slen = br.next(8); 
				int symbol = i;
				CNode word = new CNode(slen, symbol);
				//read and construct codeword object with length associated with code
				L1.add(word);

		}
		//sort list by length
		L1.sort((CNode c1, CNode c2) -> c1._len - c2._len);		
		
		//read total number of symbols
		int symbols = br.next(32); 
		
		HCtree T1 = new HCtree();
		
		for(int i = 0 ; i < 256; i ++)
		{
				T1.insert(L1.get(i));
		}		

		System.out.println("Decoding...");
		try 
		{
			String output_file_name = "data/uncompressed.txt";
			
			FileOutputStream fos = new FileOutputStream(output_file_name);
			
			OutputStreamBitSink tr = new OutputStreamBitSink(fos);

			CNode currnode = T1._root;
			int i = 0;
			while(i< symbols)
			{
				int cbit = br.next(1);		
				if (currnode._len == -1)
				{
					if(cbit == 0)
					{
						currnode = currnode.left;
						
						if(currnode.isleaf == true)
						{
							tr.write(currnode._code,8);
							currnode = T1._root;
							i++;	
						}
					}
					else
					{
						currnode = currnode.right;
						
						if(currnode.isleaf == true)
						{
							tr.write(currnode._code,8);
							currnode = T1._root;
							i++;	
						}
					}	
				}			
			}
			
			// Flush output and close files.
			tr.padToWord();
			fos.flush();
			fos.close();
			compressed.close();
			System.out.println("\nDecoding Done");
		} 
		
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
