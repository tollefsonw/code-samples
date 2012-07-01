package matmath;

/**
 * An implementation of a multithreaded algorithm 
 * for addition and multiplication of matrices.
 * -Executor-
 * @version 1.0.1
 * @author Wade Tollefson
 */

import java.util.concurrent.CountDownLatch;

interface MatMath
{
    void multiply(int[][] A, int[][]B, int[][]C);  
    void add(int[][]A, int[][]B, int[][]C);        
    void print(int[][]A);                          
}

class MatMathImpl implements MatMath
{
	
    public void multiply(int[][] A, int[][]B, int[][]C)
    {
    	if (A[0].length == B.length){
    	CountDownLatch latch = 
			new CountDownLatch(A.length*B[0].length);	
    	for (int r=0; r<A.length; r++)
    	    for (int c=0; c<B[0].length; c++)
    	    {
    	        RowColProdExecutable rcpe = 
    	        	new RowColProdExecutable(A, B, C, 
    	        			r, c, A[r].length, latch);
    		new Thread(rcpe).start();
    	    }
    	try { latch.await(); }
    	catch (InterruptedException e) {e.printStackTrace();}
    	}
    	else
            System.out.println("Illegal Multiply operation: matrices"+
            	    " cannot be multiplied"); 
    }

    public void add(int[][] A, int[][] B, int[][] C)
    {
    	if ((A.length == B.length) && (A[0].length == B[0].length)){
    	CountDownLatch latch = 
			new CountDownLatch(A.length*A[0].length);	
    	for (int r=0; r<A.length; r++)
    	    for (int c=0; c<B[0].length; c++)
    	    {
    	        RowColSumExecutable rcse = 
    	        	new RowColSumExecutable(A, B, C, r, c, latch);
    	        new Thread(rcse).start(); 
    	    }
    	try { latch.await(); }
    	catch (InterruptedException e) {e.printStackTrace();}
    	}
    	else
    	    System.out.println("Illegal Add operation: matrices"+ 
    	    	    			" must have the same dimensions");
    }
    
    public void print(int[][] A)
    {
    	  for (int r=0; r<A.length; r++) 
    	  {
    	      for (int c=0; c<A[r].length; c++)
    	          System.out.print(A[r][c]+"  ");
    	      System.out.print("\n");
    	  }
    }
}

class RowColProdExecutable implements Runnable
{
     
    private int[][] first, second, result;
    private int row, col, size;
    private CountDownLatch latch;
    
    RowColProdExecutable(int[][] first, int[][] second, int[][] result,
    	    int row, int col, int size, CountDownLatch latch)
    {
        this.first = first;
        this.second = second;
        this.result = result;
        this.row = row;
        this.col = col;
        this.size = size;
        this.latch = latch;
    }

    public void run()
    {
        for (int k=0; k < size; k++)
        { result[row][col] += first[row][k]*second[k][col]; }
	latch.countDown();
    }
}
 
class RowColSumExecutable implements Runnable
{
     
    private int[][] first, second, result;
    private int row, col;
    private CountDownLatch latch;

    RowColSumExecutable(int[][] first, int[][] second, int[][] result,
    	    int row, int col, CountDownLatch latch)
    {
        this.first = first;
        this.second = second;
        this.result = result;
        this.row = row;
        this.col = col;
        this.latch = latch;
    }

    public void run()
    {
        result[row][col] = first[row][col] + second[row][col];
        latch.countDown();
    }
}
 
public class MatMathMain
{
    public static void main(String[] args)
    {
        int[][] A,B,C,D,r,s,t;
        MatMath u = new MatMathImpl();
   
        // code to initialize A,B,C,D
        
        u.add(A,B,r);
        u.multiply(r,C,s);
        u.multiply(s,D,t);
        
        // Sample Test Case:
        /*int[][] a = {{1,4,3},{6,2,8},{1,3,2},{7,6,4}};
        int[][] b = {{4,1},{3,6},{2,4}};
        int[][] c = new int[4][2];
        u.multiply(a,b,c); 
        int[][] d = {{1,2},{1,2},{1,2},{1,2}};
        int[][] e = new int[4][2];
        u.add(c,d,e);
        int[][] f = {{1,2},{2,1}};
        int[][] g = new int[4][2];
        u.multiply(e,f,g);
        u.print(g);*/
        // END TEST CASE 
    }
}


