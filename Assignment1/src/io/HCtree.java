package io;



public class HCtree 
{
	
	public CNode _root;
	int size;
	
	public HCtree()
	{
		CNode rootHT = new CNode(-1, -1);
		_root = rootHT;
		rootHT.left = null;
		rootHT.right = null;
		rootHT.isleaf = false;
		size = 1;
	}
	
	public boolean insert(CNode c) 
	{
		if(c._len == 0)
		{
			return false;
		}
		
		if (size >= 1)
		{
			if(_root.insertNode(c, c._len) == true)
			{
				size++;
				//System.out.println("Node inserted!");
				return true;
			}
		}
		return false;
	}
	

	



}
