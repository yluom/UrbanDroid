package fr.android.urbandroid;

import java.net.*;
import java.io.*;

/*
* Classe MiseAJour
* 
* Derniere modification : Flo
* 
* Utilisation :
* 
* MiseAJour maj("http://www.site.com/version.txt", "1");
* if (maj.nouvelle())
* 	maj.faire();
* 
*/

public class MiseAJour
{
	
	// l'url que l'on recupere en parametre
	private String url;
	private String versionActuelle;
	
	// Constructeur
	public MiseAJour(String urlParam, String version) throws Exception
	{
		this.url = urlParam;
		if (!this.testURL())
			throw new Exception("URL de mise a jour invalide");
	}
	
	private String getVersion()
	{
		try
		{
			URL checkVersion = new URL(this.url);
			URLConnection co = checkVersion.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(co.getInputStream()));
			String version = in.readLine();
			in.close();
			return version;
		}
		catch (Exception ex)
		{
			// aficher un message d'erreur
			System.out.println(ex.toString());
		}
		return null;
	}
	
	// Verifier que l'url est valide
	private boolean testURL()
	{
		if (this.getVersion() == null || this.getVersion() == "")
			return false;
		else
			return true;
	}

	public boolean nouvelle() throws Exception
	{
		if (!this.testURL())
			throw new Exception("URL de mise a jour invalide");
		
		if (this.getVersion()
	}
	
	
	
	

}
