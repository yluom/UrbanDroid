package fr.android.urbandroid;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class Ligne 
{
	
	private String nom;		// Nom de la ligne. Exemple : "B23"		
	private String type; 	// Type de ligne. Exemple : "M" pour métro ou "B" pour bus
	private int nbStation;	// Nombre de stations que comporte la ligne. Utile pour obtenir "l'index" du deuxième terminus.
	
	// Liste des stations triées par leur positionnement.
	// Clé = positionnement. Ex : "1" pour un des deux terminus.
	// Valeur = Station. Ex : new Station(43.1337, 12.42, true, "Basso Cambo")
	private TreeMap<Integer, Station> listeStation;
	
	// Constructeur
	public Ligne(String paramNom, String paramType, TreeMap<Integer, Station> paramListe)
	{
		 this.nom = paramNom;
		 this.type = paramType;
		 this.listeStation = paramListe;
		 this.nbStation = this.listeStation.size();
	}
	
	// Retourne le nom de la ligne
	public String getNom()
	{
		return this.nom;
	}
	
	// Retourne le type de ligne
	public String getType()
	{
		return this.type;
	}
	
	// Retourne la somptueuse liste de station que comporte la ligne
	public TreeMap<Integer, Station> getListeStation()
	{
		return this.listeStation;
	}
	
	// Retourne "l'index" du second terminus
	public int getIndexSecondTerminus()
	{
		return this.nbStation;
	}
	
	// Retourne l'id d'une station passée en paramètre.
	// Si la station n'est pas sur la ligne, retourne -1.
	public int getIdStation(Station station)
	{
		Set<Integer> setStation = this.listeStation.keySet();
		Iterator<Integer> it = setStation.iterator();
		int buffer;
		while (it.hasNext())
		{
			buffer = it.next();
			if (station.getNom() == this.listeStation.get(buffer).getNom())
				return buffer;
		}
		return -1;	
	}
	
	public Station getStation(int position)
	{
		return this.listeStation.get(position);
	}
}
