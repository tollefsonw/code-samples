/**
 * An implementation of Graham's algorithm for computing
 * the convex hull of a set of points in a plane.
 * @author Wade Tollefson
 * @version 1.0.1
 */

package grahamscan;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.StringTokenizer;

public class GrahamScan {


    public static void main(String[] args) throws IOException {

        String[] tests = {"case1.txt", "case2.txt", "case3.txt", "case4.txt", "case5.txt", "case6.txt"};

        for (String input : tests)
        {
            int[][] testCase = findMinYCoordinateIndex(processInput(input));
            sortPoints(testCase, 1, testCase[0].length-1);
            testCase = findConvexHull(removeCollinear(testCase));
            printOutput(testCase);
        }
    }

    // takes a list of points sorted by polar order and returns a list containing the
    // points of the convex hull list in counter-clockwise order
    private static int[][] findConvexHull(int[][] pointSet)
    {
        Stack<Integer> convexHullStack = new Stack<Integer>();
        if (pointSet[0].length > 3)
        {
            convexHullStack.push(0); convexHullStack.push(1); convexHullStack.push(2);
            for (int i = 3; i<= pointSet[0].length-1; i++)
            {
                Integer top = convexHullStack.pop();
                Integer nextToTop = convexHullStack.pop();
                while (((pointSet[0][top]-pointSet[0][nextToTop])*(pointSet[1][i]-pointSet[1][nextToTop]))-
                        ((pointSet[0][i]-pointSet[0][nextToTop])*(pointSet[1][top]-pointSet[1][nextToTop])) <= 0)
                {
                   top = nextToTop;
                   nextToTop = convexHullStack.pop();
                }
                convexHullStack.push(nextToTop);
                convexHullStack.push(top);
                convexHullStack.push(i);
            }

            int[][] convexHull = new int[2][convexHullStack.size()];
            for (int i = convexHull[0].length-1; i>=0; i--)
            {
                int temp = convexHullStack.pop();
                convexHull[0][i] = pointSet[0][temp];
                convexHull[1][i] = pointSet[1][temp];
            }
            return convexHull;
        }
        else
        {
            return pointSet;
        }
    }

    // sorts points by polar
    private static void sortPoints(int[][] pointSet, int start, int end)
    {
       if (start < end)
       {
           int mid = (start+end)/2;
           sortPoints(pointSet, start, mid);
           sortPoints(pointSet, mid+1, end);
           crossProductMerge(pointSet, start, mid, end);
       }
    }

    // sorts points by polar angle
    private static void crossProductMerge(int[][] pointSet, int start, int mid, int end)
    {
       int[][] L = new int[2][mid-start+1];
       int[][] R = new int[2][end-mid];
       for (int i = 0; i<= L[0].length-1; i++)
       {
           L[0][i] = pointSet[0][start+i]; L[1][i] = pointSet[1][start+i];
       }
       for (int j = 0; j<= R[0].length-1; j++)
       {
           R[0][j] = pointSet[0][mid+j+1]; R[1][j] = pointSet[1][mid+j+1];
       }
       int i = 0;
       int j = 0;
       for (int k = start; k <= end; k++) 
       {
           if (i == L[0].length || j == R[0].length)
           {
               if (i == L[0].length)
               {
                   pointSet[0][k] = R[0][j]; pointSet[1][k] = R[1][j]; j++;
               }
               else
               {
                   pointSet[0][k] = L[0][i]; pointSet[1][k] = L[1][i]; i++;
               }
           }
           else
           {
                if (((L[0][i]-pointSet[0][0])*(R[1][j]-pointSet[1][0]))-
                        ((R[0][j]-pointSet[0][0])*(L[1][i]-pointSet[1][0])) > 0)
                {
                     pointSet[0][k] = L[0][i]; pointSet[1][k] = L[1][i]; i++; 
                }
                else
                {
                     pointSet[0][k] = R[0][j]; pointSet[1][k] = R[1][j]; j++; 
                }
           }
       }
    }

    // removes points collinear with respect to the minimum y-coordinate of the convex hull
    // maintaining the point of greater distance
    private static int[][] removeCollinear(int[][] pointSet)
    {
        int numCollinear = 0;
        for (int i = 1; i<= pointSet[0].length-2; i++)
        {
            if (((pointSet[0][i]-pointSet[0][0])*(pointSet[1][i+1]-pointSet[1][0]))-
                        ((pointSet[0][i+1]-pointSet[0][0])*(pointSet[1][i]-pointSet[1][0])) == 0)
                    numCollinear++;
        }
        if (numCollinear == 0)
            return pointSet;

        int[][] pointSetTemp = new int[2][pointSet[0].length-numCollinear];
        pointSetTemp[0][0] = pointSet[0][0]; pointSetTemp[1][0] = pointSet[1][0];
        int j=1;
        for (int i = 1; i<= pointSet[0].length-2; i++)
        {
            if (((pointSet[0][i]-pointSet[0][0])*(pointSet[1][i+1]-pointSet[1][0]))-
                        ((pointSet[0][i+1]-pointSet[0][0])*(pointSet[1][i]-pointSet[1][0])) == 0)
            {
                int d1 = Math.abs((pointSet[0][i]-pointSet[0][0])^2 + (pointSet[1][i]-pointSet[1][0])^2);
                int d2 = Math.abs((pointSet[0][i+1]-pointSet[0][0])^2 + (pointSet[1][i+1]-pointSet[1][0])^2);
                if (d1 > d2)
                {
                    pointSetTemp[0][j] = pointSet[0][i]; pointSetTemp[1][j] = pointSet[1][i];
                    i++; j++;
                }
                else
                {
                    pointSetTemp[0][j] = pointSet[0][i+1]; pointSetTemp[1][j] = pointSet[1][i+1];
                    i++; j++;
                }
            }
            else
            {
                pointSetTemp[0][j] = pointSet[0][i]; pointSetTemp[1][j] = pointSet[1][i];
                j++;
            }
        }
        if (j < pointSetTemp[0].length)
        {
            pointSetTemp[0][j] = pointSet[0][pointSet[0].length-1];
            pointSetTemp[1][j] = pointSet[1][pointSet[0].length-1];
        }
        return pointSetTemp;
    }

    //determines minimum y-coordinate of convex hull
    private static int[][] findMinYCoordinateIndex(int[][] pointSet)
    {
        int x = pointSet[0][0];
        int y = pointSet[1][0];
        int index = 0;
        for (int i = 1; i<=pointSet[0].length-1; i++)
        {
            if (pointSet[1][i] == y)
            {
               if (pointSet[0][i] < x)
               {
                   y = pointSet[1][i];
                   x = pointSet[0][i];
                   index = i;
               }
            }
            else
            {
                if (pointSet[1][i] < y)
                {
                    y = pointSet[1][i];
                    x = pointSet[0][i];
                    index = i;
                }
            }
        }
        int tempX = pointSet[0][0]; int tempY = pointSet[1][0];
        pointSet[0][0] = pointSet[0][index];
        pointSet[1][0] = pointSet[1][index];
        pointSet[0][index] = tempX; pointSet[1][index] = tempY;
        return pointSet;
    }

    //processes input from file
    private static int[][] processInput (String info) throws FileNotFoundException, IOException
    {
        BufferedReader input =  new BufferedReader(new FileReader(info));
        ArrayList<Integer> pointList = new ArrayList<Integer>();
        String point;
        while ((point = input.readLine()) != null)
        {
            StringTokenizer st = new StringTokenizer(point);
            pointList.add(Integer.parseInt(st.nextToken()));
            pointList.add(Integer.parseInt(st.nextToken()));
        }
        int[][] pointSet = new int[2][pointList.size()/2];
        int j = 0;
        for (int i = 0; i<=pointList.size()-1; i=i+2)
        {
            pointSet[0][j] = pointList.get(i);
            pointSet[1][j] = pointList.get(i+1);
            j++;
        }
        return pointSet;
    }

    //prints the convex hull in counter-clockwise order to display
    private static void printOutput (int[][] results)
    {
        for (int i=0; i<=results[0].length-1; i++)
        {
            System.out.println(results[0][i] +","+ results[1][i]);
        }
        System.out.println();
    }
}
