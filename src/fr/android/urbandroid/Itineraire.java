package fr.android.urbandroid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;


public class Itineraire  
{
	// Notre station de départ.
	private Station stationDépart;
	// Notre station d'arrivée.
	private Station stationArrivée;
	// Le fameux parcours.
	private StringBuffer parcours;
	// La liste de ligne dont fait partie la station de départ.
	private ArrayList<Ligne> ligneDépart = new ArrayList<Ligne>();
	// La liste de ligne dont fait partie la station d'arrivée.
	private ArrayList<Ligne> ligneArrivée = new ArrayList<Ligne>();
	
	// Constructeur.
	public Itineraire(Station départ, Station arrivée, ArrayList<Ligne> listeLigneDépart, ArrayList<Ligne> listeLigneArrivée)
	{
		this.stationArrivée = arrivée;
		this.stationDépart = départ;
		this.parcours = new StringBuffer("");
		this.ligneArrivée = listeLigneArrivée;
		this.ligneDépart = listeLigneDépart;
	}

	// Retourne la station de départ.
	public Station getStationDépart()
	{
		return this.stationDépart;
	}
	
	// Retourne la station d'arrivée.
	public Station getStationArrivée()
	{
		return this.stationArrivée;
	}
	
	public String toString()
	{
		return this.parcours.toString();
	}
	
	// Retourne le nom de la ligne en commun aux deux stations.
	// Sinon retourne null.
	private String vérifLigne(Station a, Station b)
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
	
	private String vérifLigne()
	{
		for (Ligne l1 : this.ligneArrivée)
		{
			for (Ligne l2 : this.ligneDépart)
			{
				if (l1.getNom() == l2.getNom())
					return l1.getNom();
			}
		}
		return "MA";
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
	
	public void calculerItineraire()
	{
		/*
		si (this.stationDepart.getLigne() == this.stationArrivee.getLigne())
			parcours.ajouter("Prendre la station " + this.stationDepart);
		si (this.stationDepart.getPosition() < this.stationArrivee.getPosition())
			parcours.ajouter(" direction " + this.station.Depart.getLigne().getDernierTerminus() + ".\n");
		sinon
			parcours.ajouter(" direction " + this.station.Depart.getLigne().getPremierTerminus() + ".\n");
		finsi;
			parcours.ajouter("S'arretter à la station " + this.station.Arrivee.getNom() + ".\n");
		finsi;
		 */
		if (this.vérifLigne() != null)
		{
			Ligne l = this.getLigne(/*this.vérifLigne()*/"MA");
			String terminus1 = l.getListeStation().get(1).getNom();
			String terminus2 = l.getListeStation().get(l.getIndexSecondTerminus()).getNom();
					
			this.parcours.append("Prendre la station " + this.stationDépart.getNom());
			if (l.getIdStation(this.stationDépart)
					<
				l.getIdStation(this.stationArrivée))
				this.parcours.append(" direction " + terminus2 + ".\n");
			else if (l.getIdStation(this.stationDépart)
					>
					l.getIdStation(this.stationArrivée))
				this.parcours.append(" direction " + terminus1 + ".\n");
			this.parcours.append("Descendre à " + this.stationArrivée.getNom() + ".\n");
		}
	}
	
	private Ligne getLigne(String nomLigne)
	{
		for (Ligne l : this.ligneArrivée)
		{
			if (l.getNom() == nomLigne)
				return l;
		}
		for (Ligne l2 : this.ligneDépart)
		{
			if (l2.getNom() == nomLigne)
				return l2;
		}
		return null;
	}
}
