package jnDB;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Cursor;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import jnDB.exception.TableExistenceError;

public class JnDatabase {
	public static final String CreateTableSuccess(String tableName) { return "'" + tableName + "' table is created"; }
	public static final String DropSuccess(String tableName) { return "'" + tableName + "' table is droped"; }
	public static final String NO_SUCH_TABLE = "No such table";
	public static final String PROMPT = "DB_2015-18380> ";
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
        //dbConfig.setSortedDuplicates(true);
        myDatabase = myDbEnvironment.openDatabase(null, "myDatabase", dbConfig);
    }
    
    public void close(){
    	if(myDatabase != null) myDatabase.close();
        if(myDbEnvironment != null) myDbEnvironment.close();
    }
    
    private static byte[] serializeTable(Table table) throws IOException {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(table);
        return out.toByteArray();
    }
    
    public static Table deserializeTable(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return (Table) is.readObject();
    }
    
    public void putTable(Table table){
    	Cursor cursor = null;
    	DatabaseEntry key,data;
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		key = new DatabaseEntry(table.getName().getBytes("UTF-8"));
    		data = new DatabaseEntry(serializeTable(table));
    		cursor.put(key, data);
    		cursor.close();
    	}
    	catch (Exception e){
    		
    	}
    }
    
    public Table getTable(String tableName){
    	Cursor cursor = null;
    	DatabaseEntry foundKey = new DatabaseEntry();
    	DatabaseEntry foundData = new DatabaseEntry();
    	
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
    		do{
        		String keyString = new String(foundKey.getData(), "UTF-8");
        		if(keyString.equals(tableName)){
        			cursor.close();
        			return deserializeTable(foundData.getData());
        		}
        	}while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    	}catch(Exception e){
    		
    	}
    	cursor.close();
    	return null;
    }
    
    public boolean isExists(String tableName){
    	Cursor cursor = null;
    	DatabaseEntry foundKey = new DatabaseEntry();
    	DatabaseEntry foundData = new DatabaseEntry();
    	
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		cursor.getFirst(foundKey, foundData, LockMode.DEFAULT);
    		do{
        		String keyString = new String(foundKey.getData(), "UTF-8");
        		if(keyString.equals(tableName)){
        			cursor.close();
        			return true;
        		}
        	}while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    	}catch(Exception e){
    		
    	}
    	cursor.close();
    	return false;
    }
    
    public void createTable(TableSchema schema){
    	if(isExists(schema.getName())) throw new TableExistenceError();
    	schema.checkValidity();
    	Table table = new Table(schema);
    	putTable(table);
    	printMessage(CreateTableSuccess(schema.getName()));
    }
    
    public void dropTable(String tableName){
    	Cursor cursor = null;
    	DatabaseEntry foundKey = new DatabaseEntry();
    	DatabaseEntry foundData = new DatabaseEntry();
    	
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		if(cursor.getFirst(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.NOTFOUND){
    			printMessage(NO_SUCH_TABLE);
    			cursor.close();
    			return;
    		}
    		do{
        		String keyString = new String(foundKey.getData(), "UTF-8");
        		if(keyString.equals(tableName)){
        			cursor.delete();
        			cursor.close();
        			printMessage(DropSuccess(tableName));
        			return;
        		}
        	}while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    	}catch(Exception e){
    		
    	}
    	cursor.close();
    	printMessage(NO_SUCH_TABLE);
    }
    
    public void desc(String tName){
    	
    }
    
    public void showTables(){
    	Cursor cursor = null;
    	DatabaseEntry foundKey = new DatabaseEntry();
    	DatabaseEntry foundData = new DatabaseEntry();
    	
    	try{
    		cursor = myDatabase.openCursor(null, null);
    		if(cursor.getFirst(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.NOTFOUND) {
    			printMessage(NO_SUCH_TABLE);
    			cursor.close();
    			return;
    		}
    		System.out.println("----------------");
    		do{
        		String keyString = new String(foundKey.getData(), "UTF-8");
        		System.out.println(keyString);
        	}while(cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS);
    	}catch(Exception e){
    		
    	}
    	cursor.close();
    	System.out.println("----------------");
    	System.out.print(PROMPT);
    	return;
    }
    
    public void printMessage(String s){
    	System.out.println(s);
    	System.out.print(PROMPT);
    }
}
