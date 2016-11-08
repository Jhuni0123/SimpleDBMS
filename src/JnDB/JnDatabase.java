package JnDB;

import java.io.File;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Cursor;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class JnDatabase {
	Environment myDbEnvironment;
    Database myDatabase;
    
    public JnDatabase(){
    	myDbEnvironment = null;
    	myDatabase = null;
    }
    
    public void open(String locate){
    	// Open or Create Database Environment
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        myDbEnvironment = new Environment(new File(locate), envConfig);
        
        // Open or Create Database
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        dbConfig.setSortedDuplicates(true);
        myDatabase = myDbEnvironment.openDatabase(null, "myDatabase", dbConfig);
    }
    
    public void close(){
    	if(myDatabase != null) myDatabase.close();
        if(myDbEnvironment != null) myDbEnvironment.close();
    }
    
    public void createTable(String tName, TableSchema schema){
    	System.out.println("Create table '" + tName + "' requested" );
    }
    
    public void dropTable(String tName){
    	System.out.println("Drop table '" + tName + "' requested" );
    }
    
    public void desc(String tName){
    	System.out.println("Desc '" + tName + "' requested" );    	
    }
    
    public void showTables(){
    	System.out.println("show tables requested" );
    }
}
