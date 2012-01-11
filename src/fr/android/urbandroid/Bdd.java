package fr.android.urbandroid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class Bdd {

		
	public static SQLiteDatabase db;
	
	public static Cursor fetchAllTitles(String nomTable, String[] colonnes,  String select, String[] where, String groupby, String having, String orderby){
	db = null;
	try
	{
		String DB_PATH = "/data/data/fr.android.urbandroid/";
	    String DB_NAME = "urbdroid.db";
		String myPath = DB_PATH + DB_NAME;
	    db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
	    
	    return db.query(nomTable,colonnes, select,where,groupby,having,orderby);
	}
	catch(Exception ex)
	{
	}
	return null;
}
}
