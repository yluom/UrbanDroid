package fr.android.urbandroid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;


public class Itineraire  
{
	// Notre station de d�part.
	private Station stationDepart;
	// Notre station d'arriv�e.
	private Station stationArrivee;
	// Le fameux parcours.
	private Station[] parcours;
	// La liste de ligne dont fait partie la station de d�part.
	private ArrayList<Ligne> ligneDepart;
	private ArrayList<Ligne> ligneArrivee;
	
	// Constructeur.
	public Itineraire(Station depart, Station arrivee, ArrayList<Ligne> listeLigneDepart, ArrayList<Ligne> listeLigneArrivee)
	{
		this.stationArrivee = arrivee;
		this.stationDepart = depart;
		this.parcours = null;
		this.ligneArrivee = listeLigneArrivee;
		this.ligneDepart = listeLigneDepart;
	}

	// Retourne la station de d�part.
	public Station getStationDepart()
	{
		return this.stationDepart;
	}
	
	// Retourne la station d'arriv�e.
	public Station getStationArrivee()
	{
		return this.stationArrivee;
	}
	
	// Retourne le parcours.
	public Station[] getParcours()
	{
		return this.parcours;
	}
	
	// Retourne le nom de la ligne en commun aux deux stations.
	// Sinon retourne null.
	private String verifLigne(Station a, Station b)
	{
		Set<String> listeLigneA = a.listeLigne().keySet();
		Set<String> listeLigneB = b.listeLigne().keySet();
		Iterator<String> itA = listeLigneA.iterator();
		Iterator<String> itB;
		String buffer;
		while (itA.hasNext())
		{
			buffer = itA.next();
			itB = listeLigneB.iterator();
			while(itB.hasNext())
			{
				if (buffer == itB.next())
					return buffer;
			}
		}
		return null;
	}
	
	// Retourne la liste de station d'une ligne.
	private TreeMap<Integer, Station> listeStation(Ligne ligne)
	{
		return ligne.getListeStation();	
	}
	
	// Retourne la liste de ligne d'une station.
	private HashMap<String, Boolean> listeLigne(Station station)
	{
		return station.listeLigne();
	}
	
}
