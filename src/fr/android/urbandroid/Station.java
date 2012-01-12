package fr.android.urbandroid;

import java.util.HashMap;

public class Station {
	
	// Longitude de l'emplacement de la station. Exemple : 42.1337
	private float longitude;
	// Latitude de l'emplacement de la station. Exemple : 13.37
	private float latitude;
	// Nom de la station. Exemple : Basso Cambite
	private String nom;
	// Liste des lignes accessibles via la station.
	// Index = "B23"
	// Valeur = vrai si la station est un terminus de la ligne
	private HashMap<String, Boolean> listeLigne;
	
	// Constructeur
	public Station(float paramLong, float paramLat, String paramNom, HashMap<String, Boolean> paramListe) 
	{
		this.longitude = paramLong;
		this.latitude = paramLat;
		this.nom = paramNom;
		this.listeLigne = paramListe;
	}
	
	// Retourne la longitude de la station.
	public float getLongitude() 
	{
		return longitude;
	}

	// Retourne la latitude de la station.
	public float getLatitude() 
	{
		return latitude;
	}

	// Retourne vrai si la station est un terminus de la ligne l.
	// Retourne faux si elle n'est pas un terminus ou si elle n'appartient pas à cette ligne.
	public boolean isTerminus(Ligne l) 
	{
		if (this.listeLigne.containsKey(l.getNom()))
			return this.listeLigne.get(l.getNom());
		else
			return false;
	}

	// Retourne le nom de la station.
	public String getNom() 
	{
		return nom;
	}
	
	// Retourne vrai si la station est sur la ligne l.
	// Sinon retourne faux.
	public boolean estSurLigne(Ligne l)
	{
		return this.listeLigne.containsKey(l.getNom());
	}
	
	// Retourne la liste des lignes de la station
	public HashMap<String, Boolean> listeLigne()
	{
		return this.listeLigne;
	}
}
