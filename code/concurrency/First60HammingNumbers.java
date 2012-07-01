/**
 * A multithreaded/multi-component solution 
 * to compute the first 60 hamming numbers.
 * @version 1.0.0
 * @author Wade Tollefson
 */

import java.util.concurrent.*;
import java.util.ArrayList;


class Mult implements Runnable 
{
	
    final BlockingQueue<Integer> in;
    final BlockingQueue<Integer> out;
    final int multType;
    
    Mult(BlockingQueue<Integer> in, BlockingQueue<Integer> out, int multType)
    {
        this.in = in;
        this.out = out;
        this.multType = multType;
    }

    public void run() 
    { 
    	try {
            while (!Thread.currentThread().isInterrupted())
            {
                int current = in.take();
                out.put(current*multType);
            
            }
        }
        catch (InterruptedException e) {}
    }
    
}
 
class ThreeInOrderMerge implements Runnable
{

    final BlockingQueue<Integer> twoIn;
    final BlockingQueue<Integer> threeIn;
    final BlockingQueue<Integer> fiveIn;
    final BlockingQueue<Integer> nextOut;

    ThreeInOrderMerge(BlockingQueue<Integer> twoIn, 
    	    		BlockingQueue<Integer> threeIn, 
    	    			BlockingQueue<Integer> fiveIn,
    	    				BlockingQueue<Integer> nextOut)
    {
        this.twoIn = twoIn;
        this.threeIn = threeIn;
        this.fiveIn = fiveIn;
        this.nextOut = nextOut;
    }

    public void run()
    {
        try {
            while (!Thread.currentThread().isInterrupted())
            {
               if (twoIn.peek() != null && threeIn.peek() 
                			!= null && fiveIn.peek() != null)
                {	    
                    int numTwo = twoIn.peek();
                    int numThree = threeIn.peek();
                    int numFive = fiveIn.peek();
                    boolean compareTwoAndThree = false;
                    boolean compareThreeAndFive = false;
                    boolean compareTwoAndFive = false;
                    
                    if (numTwo == numThree)
                        compareTwoAndThree = true;
                    if (numThree == numFive)
                        compareThreeAndFive = true;
                    if (numTwo == numFive)
            	        compareTwoAndFive = true;

                    String minLocation = "two";
                    int min = numTwo;
                    if (min > numThree)
                        { min = numThree; minLocation = "three"; }
                    if (min > numFive)
                        { min = numFive; minLocation = "five"; }

                   if (minLocation.equals("two"))
                   {
                       if (compareTwoAndThree == true)
                           threeIn.take();
                       if (compareTwoAndFive == true)
               	           fiveIn.take();
                       nextOut.put(twoIn.take());
                   }
                   else if (minLocation.equals("three"))
                   {
                       if (compareThreeAndFive == true)
                           fiveIn.take();
                       nextOut.put(threeIn.take());
                   }
                   else
                       nextOut.put(fiveIn.take());
                }
            }
        }
        catch (InterruptedException e) {}
    }
    
}
 
class FourOutCopy implements Runnable
{
    final BlockingQueue<Integer> nextNum;
    final BlockingQueue<Integer> outToTwo;
    final BlockingQueue<Integer> outToThree;
    final BlockingQueue<Integer> outToFive;
    final BlockingQueue<Integer> print;
    
    FourOutCopy(int firstNumber, BlockingQueue<Integer> nextNum,
    	    BlockingQueue<Integer> outToTwo, BlockingQueue<Integer> outToThree, 
    	    	BlockingQueue<Integer> outToFive, BlockingQueue<Integer> print)
    {
        this.nextNum = nextNum;
        this.outToTwo = outToTwo;
        this.outToThree = outToThree;
        this.outToFive = outToFive;
        this.print = print;
        try { nextNum.put(firstNumber);}
        catch (InterruptedException e){ }
    }

    public void run()
    {
    	try {    
            while (!Thread.currentThread().isInterrupted())
            {
                int next = nextNum.take();
                print.put(next);
                outToTwo.put(next);
                outToThree.put(next);
                outToFive.put(next);
            }
        }
        catch (InterruptedException e) {}
    }
}

class Print implements Runnable
{

    final BlockingQueue<Integer> print;
    final CountDownLatch countdown;

    Print(BlockingQueue<Integer> print, CountDownLatch countdown)
    {
        this.print = print; 
        this.countdown = countdown;
    }

    public void run()
    {
    	try {    
    	    while (!Thread.currentThread().isInterrupted())
    	    { 
                if (countdown.getCount() != 0) 
                {
            	    System.out.println(print.take());
            	    countdown.countDown(); 
            	}
            }
        }
        catch (InterruptedException e) { }
    }
}

public class First60HammingNumbers
{
    public static void main(String[] args)
    {
  
        final ExecutorService executor = Executors.newFixedThreadPool(6);
	final ArrayList<Runnable> components = new ArrayList<Runnable>(6);
	final CountDownLatch countdown = new CountDownLatch(60);

    	final BlockingQueue<Integer> multTwoIn = 
    				new LinkedBlockingQueue<Integer>();
    	final BlockingQueue<Integer> multTwoOut = 
    				new LinkedBlockingQueue<Integer>();
    	final BlockingQueue<Integer> multThreeIn = 
    				new LinkedBlockingQueue<Integer>();
    	final BlockingQueue<Integer> multThreeOut = 
    				new LinkedBlockingQueue<Integer>();
    	final BlockingQueue<Integer> multFiveIn = 
    				new LinkedBlockingQueue<Integer>();
    	final BlockingQueue<Integer> multFiveOut = 
    				new LinkedBlockingQueue<Integer>();
    	final BlockingQueue<Integer> nextInOrder = 
    				new LinkedBlockingQueue<Integer>();
    	final BlockingQueue<Integer> nextPrint = 
    				new LinkedBlockingQueue<Integer>();
    	
    	components.add(new Mult(multTwoIn, multTwoOut, 2));
        components.add(new Mult(multThreeIn, multThreeOut, 3));
        components.add(new Mult(multFiveIn, multFiveOut, 5));
        components.add(new ThreeInOrderMerge(multTwoOut, multThreeOut,
        					multFiveOut, nextInOrder));
        components.add(new FourOutCopy(1, nextInOrder, multTwoIn, 
        				multThreeIn, multFiveIn, nextPrint));
        components.add(new Print(nextPrint, countdown));
        
    	for (Runnable component : components)
            { executor.execute(component); }
    
	try {
	countdown.await();	
	executor.shutdownNow();
	executor.shutdown();
	}
	catch (InterruptedException e) {}
    }
    
}


