package io;


public class CNode
{
	public int _len;
	public int _code;
	
	public CNode left;
	public CNode right;
	public boolean isleaf;
	int complete;

	public CNode(int len, int code)
	{
		_len = len;
		_code = code;
		isleaf = false;
		complete = 0;
	}
	
	public CNode(int len, int code, CNode r, CNode l)
	{
		_len = len;
		_code = code;
		isleaf = false;
		complete = 0;
		left = l;
		right = r;
		
		if( l == null && r == null)
		{
			isleaf = true;
		}
	}
	
	public boolean insertNode(CNode c, int d)
	{
	
	  int depth = d;

	  if (depth == 1)
	  {
		  
		  if (this.left == null) 
		  {				  
			  this.left = c;
			  this.left.isleaf = true;
			  return true;
		  }
		  else 
		  {
			  this.right = c;
			  this.right.isleaf = true;
			  return true;
		  }
		 
	  }
	  
	  else
	  {
		  depth--;
		  
		  if (this.left == null)
		  {
			  this.left = new CNode(-1, -1);
			  return this.left.insertNode(c, depth); 
			  
		  }
		  
		  else
		  {
			  if(isFullTree(this.left) == false)
			  {
				  return this.left.insertNode(c, depth);
				  
			  }
			  else
			  {
				  if(this.right == null)
				  {
					  this.right = new CNode(-1, -1);
					  return this.right.insertNode(c, depth);
				  }
				  else
				  {
					  return this.right.insertNode(c, depth);
				  }
			  }
		  } 
	  }		
	}
	
    public boolean isFullTree(CNode node) 
    { 
        // if empty tree 
        if(node == null) 
        return true; 
           
        // if leaf node 
        if(node.left == null && node.right == null ) 
            return true; 
           
        // if both left and right subtrees are not null 
        // the are full 
        if((node.left!=null) && (node.right!=null)) 
            return (isFullTree(node.left) && isFullTree(node.right)); 
           
        // if none work 
        return false; 
        
        /*
		 *  @author Gaurav Gupta
		 */
		
		// code adapted from https://www.geeksforgeeks.org/check-whether-binary-tree-full-binary-tree-not/
    } 
	
	
	
	@Override
    public String toString() 
	{ 
        return String.format("atribute len/freq: " + _len + " -- code: " + _code );//+ "  [" + (char)_code + "]"); 
    } 
}
