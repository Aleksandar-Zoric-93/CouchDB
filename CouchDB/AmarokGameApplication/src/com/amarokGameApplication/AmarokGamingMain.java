
package com.amarokGameApplication;

import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 *
 * @author Aleksandar Zoric
 */
public class AmarokGamingMain {
    Session dbSession = new Session("localhost", 5984);
     public static void main(String[] args) throws IOException
    {   
        AmarokGamingMain m = new AmarokGamingMain();
        //m.simpleMapView("amarokgamingdb");
        //m.callSimpleMapView();
        //m.complexMapView("amarokgamingdb", 1000, 1000);
        //m.simpleMapReduce("amarokgamingdb");
    }
     
     
    //This method creates a database.  It accepts a string parameter to name the database. 
    public void createDatabase(String dbName)
    {
        dbSession.createDatabase(dbName);
        System.out.println("Database Created");
    }
    
    
    //This method lists all the current databases.
    public List listAllDB()
    {
        List <String> listofdb = dbSession.getDatabaseNames();
        
        List listOfDB = new ArrayList();
        for (String temp : listofdb) 
        {
	    listOfDB.add(temp);
	}
        
        return listOfDB;
    }
    
    
    //This method deletes a database.  It accepts a string parameter for the user
    //to enter the name of the database they wish to remove.
    public void deleteDatabase(String dbName)
    {
        dbSession.deleteDatabase(dbName);
        System.out.println("Database Deleted");
    }
    
    
    //This method creates a new document in a database.  It needs the name of the database,
    //and all the fields for the amarokgamingdb database.
    public void createDocument(String dbName, String docID, String gamerTag, String game, int points, int inGameHours, String language, String region, String email)
    {
        Database db = dbSession.getDatabase(dbName);
         
        Document doc = new Document();
         
        doc.setId(docID);
        doc.put("GamerTAG",gamerTag);
        doc.put("Game", game);
        doc.put("Points", points);
        doc.put("IngameHours", inGameHours);
        doc.put("Language", language);
        doc.put("Region", region);
        doc.put("Email",email);
         
        db.saveDocument(doc);
        
        System.out.println("Document Created");
    }
    
    
    //This method deletes a document.  It needs the name of the database the document is in,
    //and the document ID they wish to remove.
    public void deleteDocument(String dbName, String docID)
    {
        Database db = dbSession.getDatabase(dbName);
         
        Document doc = db.getDocument(docID);
        db.deleteDocument(doc);
        
        System.out.println("Document Deleted");
    }
    
    
    //This method gets the number of documents in a database.
    public int retrieveDocumentCount(String dbName)
    {
        Database db = dbSession.getDatabase(dbName);     
        int count = db.getDocumentCount();
        
        return count;
    }
    
    
    //This method retrieves all the current document in a given database.
    public ViewResults getAllDocuments(String dbName)
    {
        Database db = dbSession.getDatabase(dbName);    
        ViewResults results;
        results = db.getAllDocuments();
        
        
        return results;
    }
    
    
    //This method gets a documents based on the document ID,
    //and which database the document is in.
    public Document retrieveDocument(String dbName, String docID)
    {
        Database db = dbSession.getDatabase(dbName);    
        Document doc = db.getDocument(docID);
       
        
        return doc;
    }
    
    
    //This method updates a current document.  It needs the database name,
    //document ID and all the current fields from the amarokgamingdb database.
    public void updateDocument(String dbName, String docID, String gamerTag, String game, int points, int inGameHours, String language, String region, String email)
    {
        Database db = dbSession.getDatabase(dbName);  
        Document doc = db.getDocument(docID);
            
        doc.setId(docID);
        doc.put("GamerTAG",gamerTag);
        doc.put("Game", game);
        doc.put("Points", points);
        doc.put("IngameHours", inGameHours);
        doc.put("Language", language);
        doc.put("Region", region);
        doc.put("Email",email);
        
        doc.putAll(doc);
        db.saveDocument(doc);
    }
    
    
    //Only run if view does not exist in the database (Call located in main()).
    //This method returns all the english speaking gamers.
    public void simpleMapView(String dbName)
    {
        Database db = dbSession.getDatabase(dbName);
         
        Document doc = new Document();
        doc.setId("_design/mapView001");
                 
        String str = "{\"get_english_speaking_gamers\": {\"map\": \"function(doc) { if (doc.Language == 'English')" +
                "emit(null, doc) } \"},}";
         
        doc.put("views", str); 
        db.saveDocument(doc);         
    }  
    
    
    //Only run if view does not exist in the database (Call located in main()).
    //This method returns all loyal members.  Members who have more than 1000 points and game hours.
    public void complexMapView(String dbName, int points, int inGameHours)
    {
        Database db = dbSession.getDatabase(dbName);
         
        Document doc = new Document();
        doc.setId("_design/mapView002");

        String str = "{\"get_gamers_over_price_inGameHours\": {\"map\": \"function(doc) { if ('Points' in doc && 'IngameHours' in doc) {" +
                "if (doc.Points >= " + points + " && doc.IngameHours >= " + inGameHours + "){ emit(null, doc) }}} \"}}";
        
        doc.put("views", str); 
        db.saveDocument(doc);  
    }
    
    
    //Only run if view does not exist in the database (Call located in main()).
    //This method returns the total amount of hours the gamers have in game.
    public void simpleMapReduce(String dbName)
    {
        Database db = dbSession.getDatabase(dbName);
         
        Document doc = new Document();
        doc.setId("_design/mapReduceView003");
        
        String str = "{\"get_amount_hours_inGame\": {\"map\": \"function(doc) { if ('IngameHours' in doc && 'Game' in doc)" +
                "emit(doc.Game, doc.IngameHours) } \"}}";
             
         doc.put("views", str); 
         db.saveDocument(doc);  
    }
    
}
