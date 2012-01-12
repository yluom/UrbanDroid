package fr.android.urbandroid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Bdd 
{
	
	public static SQLiteDatabase db = null;
	private SQLiteDatabase nonStaticDb = null;
	private Cursor c = null;
	
	public static Cursor fetchAllTitles(String nomTable, String[] colonnes,  String select, String[] where, String groupby, String having, String orderby)
	{
		//db = null;
		try
		{
			String DB_PATH = "/data/data/fr.android.urbandroid/";
			String DB_NAME = "urbdroid.db";
			String myPath = DB_PATH + DB_NAME;
			db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			Cursor res = db.query(nomTable,colonnes, select,where,groupby,having,orderby);
			return res;
		}
		catch(Exception ex)
		{
		}
		return null;
	}
	
	public Cursor getCursor(String nomTable, String[] colonnes,  String select, String[] where, String groupby, String having, String orderby)
	{
		try
		{
			this.closeDb();
			String DB_PATH = "/data/data/fr.android.urbandroid/";
			String DB_NAME = "urbdroid.db";
			String myPath = DB_PATH + DB_NAME;
			this.nonStaticDb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
			this.c = this.nonStaticDb.query(nomTable,colonnes, select,where,groupby,having,orderby);
			return c;
		}
		catch(Exception ex)
		{
		}
		return null;
	}
	
	public void closeDb()
	{
		/*if (this.c != null)
			this.c.close();*/
		if (this.nonStaticDb != null)
			this.nonStaticDb.close();
		if (this.db != null)
			this.db.close();
	}
	
}
