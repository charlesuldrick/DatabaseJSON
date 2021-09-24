/*
Charles Uldrick
 */
package p2cs310;

import java.sql.*;
import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class p2cs310 {
    
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        try {
            JSONArray test = new JSONArray();
            test.add(getJSONData());
            String jsonString = JSONValue.toJSONString(test);
            System.out.println(jsonString);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(p2cs310.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @SuppressWarnings("unchecked")
    public static JSONArray getJSONData() throws SQLException {
        
        Connection conn;
        ResultSetMetaData metadata;
        ResultSet resultset = null;        
        PreparedStatement pstSelect = null, pstUpdate = null;
        boolean hasresults;
        String query, key, value;
        int resultCount, columnCount;
        
        JSONObject record = null;
        JSONArray jsonArray = new JSONArray(); 
        
        try {

            /* Identify the Server */

            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "4752";
            System.out.println("Connecting to " + server + "...");

            /* Open Connection (MySQL JDBC driver must be on the classpath!) */

            conn = DriverManager.getConnection(server, username, password);

            /* Test Connection */

            if (conn.isValid(0)) {

                /* Connection Open! */

                System.out.println("Connected Successfully!");

                query = "SELECT firstname, middleinitial, lastname, address, city, state, zip FROM people";
                pstSelect = conn.prepareStatement(query);

                 /* Execute Select Query */

                System.out.println("Submitting Query ...");

                hasresults = pstSelect.execute();                

                /* Get Results */

                System.out.println("Getting Results ...");

                if ( hasresults ) {

                    /* Get ResultSet Metadata */

                    resultset = pstSelect.getResultSet();
                    metadata = resultset.getMetaData();
                    columnCount = metadata.getColumnCount(); 

                    while(resultset.next()) {

                        /* Loop  */
                        record = new JSONObject();
                        for (int i = 1; i <= columnCount; i++) {

                            key = metadata.getColumnLabel(i);
                            //keys.add(key);
                            value = resultset.getString(i);
                            //values.add(value);
                            record.put(key,value);

                        }
                        jsonArray.add(record);
                    }
                }

            }

            conn.close(); 
      
        } catch (Exception e) { e.printStackTrace(); }
        
        
        /* Close Other Database Objects */
        
        finally {
            
            if (resultset != null) { try { resultset.close(); } catch (Exception e) { e.printStackTrace(); } }
            
            if (pstSelect != null) { try { pstSelect.close(); } catch (Exception e) { e.printStackTrace(); } }
        }
        
        return jsonArray;
    
    } 
}