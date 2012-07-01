/**
 * JDBC sample usage
 * @author Wade Tollefson
 * @version 1.0.1
 */

import java.sql.*;
import javax.swing.JOptionPane;

public class JDBC {

    public static void main(String[] args) throws ClassNotFoundException,
                                                  SQLException { 
    String studentID;
    String[] grades = {"A","B","C","D","F"};    
    Class.forName("oracle.jdbc.OracleDriver");
    Connection c = 
        DriverManager.getConnection(
        "jdbc:oracle:thin:@cdmoracle.cti.depaul.edu:1521:def", 
                                                "wtollefs", "sql123");    
    do {
        studentID =  JOptionPane.showInputDialog("Enter ID Number:");
        if (studentID != null){
            System.out.println("Student " + studentID);
            System.out.println("---------------");
            PreparedStatement p = c.prepareStatement("SELECT Count(GRADE) FROM GRADING " +
                    "WHERE StudentID = ? AND Grade = ?");
            int numberOfGrades = 0;
            for (int i = 0; i <= 4; i++){
                p.setString(1, studentID);
                p.setString(2,grades[i]);
                ResultSet r = p.executeQuery(); r.next();
                String gradeCount = r.getString(1);
                System.out.println(grades[i] + "'s: " + gradeCount);
                numberOfGrades += Integer.parseInt(gradeCount);
                r.close();
            }
            System.out.println("Total Grades: " + numberOfGrades + "\n");
        }

    } while (studentID != null);
    c.close();
    }
}
