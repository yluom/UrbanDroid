package fr.android.urbandroid;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;
import android.util.Log;

public class Itineraire  
{
	private static final String TAG = "Itineraire";
	// Notre station de dï¿½part.
	private Station stationDepart;
	// Notre station d'arrivï¿½e.
	private Station stationArrivee;
	// Le fameux parcours.
	private String parcours;
	// La liste de ligne dont fait partie la station de dï¿½part.
	private ArrayList<Ligne> ligneDepart = new ArrayList<Ligne>();
	// La liste de ligne dont fait partie la station d'arrivï¿½e.
	private ArrayList<Ligne> ligneArrivee = new ArrayList<Ligne>();
	// Heure de départ/arrivée
	private String heure;
	// Minute de départ/arrivée
	private String minute;
	// La blacklist de ligne
	private ArrayList<Ligne> bl;
	// Vraie si on cherche le départ; Faux si on cherche l'arrivée
	private boolean whatHeure;
	
	// Constructeur.
	public Itineraire(Station depart, Station arrivee, ArrayList<Ligne> listeLigneDepart, ArrayList<Ligne> listeLigneArrivee, String paramHeure, String paramMinute, boolean b)
	{
		this.stationArrivee = arrivee;
		this.stationDepart = depart;
		this.parcours = "";
		this.ligneArrivee = listeLigneArrivee;
		this.ligneDepart = listeLigneDepart;
		this.bl = new ArrayList<Ligne>();
		this.heure = paramHeure;
		this.minute = paramMinute;
		this.whatHeure = b;
	}
	
	// Obtenir l'heure:départ d'arrivée
	public String getHeure()
	{ 
		return (this.heure + ":" + this.minute);
	}

	// Retourne la station de depart.
	public Station getStationDepart()
	{
		return this.stationDepart;
	}
	
	// Retourne la station d'arrivee.
	public Station getStationArrivee()
	{
		return this.stationArrivee;
	}
	
	public String toString()
	{
		return this.parcours;
	}
	
	private void ordonnerParcours()
	{
		String leBonParcours = "";
		String[] leParcoursDecoupe = this.parcours.split("\n");
		Log.d(TAG, "Parcours avant boucle = \n" + this.parcours);
		for (int i = 0; i < leParcoursDecoupe.length - 1; i++)
		{
			Log.d(TAG, "valeur de i = " + i);
			if (leParcoursDecoupe[i].startsWith("Descendre") && leParcoursDecoupe[i + 1].startsWith("Prendre"))
			{	
				Log.d(TAG, "dans le if / leParcoursDecoupe2[i] = " + leParcoursDecoupe[i]);
				Log.d(TAG, "dans le if / leParcoursDecoupe2[i+1] = " + leParcoursDecoupe[i+1]);
				Log.d(TAG, "dans le if / valeur ajoutée = " + leParcoursDecoupe[i].substring(20, leParcoursDecoupe[i].length() - 1));
				leBonParcours = leBonParcours + "Changer à " + leParcoursDecoupe[i].substring(12, leParcoursDecoupe[i].length() - 1) + "\n";
				i++;
			}
			else
			{
				Log.d(TAG, "dans le else / leParcoursDecoupe2[i] = " + leParcoursDecoupe[i]);
				leBonParcours = leBonParcours + leParcoursDecoupe[i] + "\n";
			}					
		}
		
		leBonParcours = leBonParcours + leParcoursDecoupe[leParcoursDecoupe.length - 1] + "\n";
		this.parcours = leBonParcours;
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
				if (buffer.equals(itB.next()))
					return buffer;
			}
		}
		return null;
	}
	
	private String verifLigne()
	{
		String buffer = "";
		for (Ligne l1 : this.ligneArrivee)
		{
			for (Ligne l2 : this.ligneDepart)
			{
				if (l1.getNom().equals(l2.getNom()) && l1.getNom().charAt(0) == 'B')
					buffer = l1.getNom();
				else if (l1.getNom().equals(l2.getNom()) && l1.getNom().charAt(0) != 'B')
					return l1.getNom();
			}
		}
		if (buffer.equals(""))
			return null;
		else
			return buffer;
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
	
	
	private boolean ligneMetro()
	{
		for (Ligne l : this.ligneArrivee)
		{
			for (Ligne l2 : this.ligneDepart)
			{
				Log.e(TAG, "#ligneMetro# l = " + l.getNom());
				Log.e(TAG, "#ligneMetro# l2 = " + l2.getNom());
				if ((l.getNom().charAt(0) == 'M' || l.getNom().charAt(0) == 'T')
						&&
					(l2.getNom().charAt(0) == 'M' || l2.getNom().charAt(0) == 'T'))
				{
					Log.e(TAG, "#ligneMetro# TRUE");
					return true;
				}
			}
		}
		Log.e(TAG, "#ligneMetro# FALSE");
		return false;
	}
	
	private ArrayList<Ligne> getLignesBus()
	{
        ArrayList<Ligne> listeLigneBus = new ArrayList<Ligne>();
        listeLigneBus.add(Bdd.nouvelleLigne("B2"));
    	return listeLigneBus;
	}
	
	public void calculerItineraire()
	{
		
		if (this.verifLigne() != null)
		{
			this.parcours = this.addParcours(this.stationDepart, this.stationArrivee);
		}
		else if (this.ligneMetro())
		{
			this.bl.add(Bdd.nouvelleLigne("B2"));
			Log.e(TAG, "BLACKLIST AJOUTER TAMERE");
			String s = "";
			this.calculerItineraire(this.stationDepart, this.stationArrivee, this.ligneDepart, s);
			this.ordonnerParcours();
		}
		else
		{
			String s = "";
			this.calculerItineraire(this.stationDepart, this.stationArrivee, this.ligneDepart, s);
			this.ordonnerParcours();
		}
	}
	
	public boolean calculerItineraire(Station StationDepart, Station StationArrivee, ArrayList<Ligne> listeLigne, String res)
	{
		Log.d(TAG, "début calculerIti res = \n" + res);
		Set<Integer> setStation;
		Iterator<Integer> it;
		Station bufferStation;
		ArrayList<Ligne> bufferListeLigne = new ArrayList<Ligne>();
		Ligne ligneAutre;
		String bufferString = "";
		listeLigne = this.supprLigne(listeLigne, this.bl);
		for (Ligne l : listeLigne)
		{
			this.bl.add(l);
			Log.d(TAG, "ligne en cours = " + l.getNom());
			setStation = l.getListeStation().keySet();
			it = setStation.iterator();
			while (it.hasNext())
			{

				bufferStation = l.getListeStation().get(it.next());
				ligneAutre = this.autreLigne(bufferStation, l);
				Log.d(TAG, "station en cours = " + bufferStation.getNom());
				if (this.verifLigne(bufferStation, StationArrivee) != null
					&&
					this.autreLigneEnCommun(StationDepart, bufferStation, l)
					&&
					!this.estDansListe(ligneAutre))
				{
					Log.d(TAG, "if ; res #1 =\n" + res);
					this.parcours = this.parcours + this.addParcours(StationDepart, bufferStation);
					Log.d(TAG, "if ; res #2 =\n" + res);
					this.parcours = this.parcours + this.addParcours(bufferStation, StationArrivee);
					Log.d(TAG, "if ; res #3 =\n" + res);
					return true;
				}		
				else if (ligneAutre != null && !this.estDansListe(ligneAutre))
				{
					bufferString = "";
					Log.d(TAG, "2eme condition ; bufferStation = " + bufferStation.getNom());
					Log.d(TAG, "2eme condition ; l = " + l.getNom());
					bufferListeLigne = this.toArrayList(bufferStation.listeLigne());
					if (this.calculerItineraire(bufferStation, StationArrivee, this.supprLigne(bufferListeLigne, listeLigne), bufferString))
					{	
						Log.d(TAG, "else if ; res #1 =\n" + res);
						this.parcours = this.addParcours(StationDepart, bufferStation) + this.parcours;
						Log.d(TAG, "else if ; res #2 =\n" + res);
					}
					else
					{
						Log.d(TAG, "else ; res #1 =\n" + res);
						this.parcours = this.parcours + bufferString;
						Log.d(TAG, "else ; res #2 =\n" + res);
					}
				}
			}
		}
		return false;
	}
	
	private String addParcours(Station a, Station b)
	{
		String res = "";
		if (this.verifLigne(a, b) != null)
		{
			Ligne l = Bdd.nouvelleLigne((this.verifLigne(a, b)));
			String terminus1 = l.getListeStation().get(1).getNom();
			String terminus2 = l.getListeStation().get(l.getIndexSecondTerminus()).getNom();
			res = res + "Prendre la station " + a.getNom() + ".\n";
			res = res + "Ligne " + this.verifLigne(a, b) + ".\n";
			if (l.getIdStation(a)
					<
				l.getIdStation(b))
				res = res + "Direction " + terminus2 + ".\n";
			else if (l.getIdStation(a)
					>
					l.getIdStation(b))
				res = res + "Direction " + terminus1 + ".\n";
			int nbMin;
			if (this.verifLigne(a, b).charAt(0) == 'B')
				nbMin = 3;
			else
				nbMin = 2;
			if (this.whatHeure)
				this.addMinute(nbMin * nbStationsEntre(a, b));
			else
				this.delMinute(nbMin * nbStationsEntre(a, b));
			res = res + "Descendre à station " + b.getNom() + ".\n";
		}
		Log.d(TAG, "addParcours ; res =\n" + res);
		return res;
	}
	
	private int nbStationsEntre(Station a, Station b)
	{
		Ligne l = Bdd.nouvelleLigne((this.verifLigne(a, b)));
		Log.e(TAG, "#nbStationsEntre# res = " + java.lang.Math.abs(l.getIdStation(a) - l.getIdStation(b)));
		return(java.lang.Math.abs(l.getIdStation(a) - l.getIdStation(b)));
	}
	
	private void addMinute(int nbMinute)
	{
		int h = Integer.decode(this.heure);
		int m = Integer.decode(this.minute);
		if (m + nbMinute >= 60)
		{
			m += nbMinute;
			h += m / 60;
			m = m % 60;
		}
		else
			m += nbMinute;
		
		this.heure = String.valueOf(h);
		Log.e(TAG, "#addMinute# paramHeure = " + this.heure);
		this.minute = String.valueOf(m);
		Log.e(TAG, "#addMinute# paramMinute = " + this.minute);
	}
	
	private void delMinute(int nbMinute)
	{
		int h = Integer.decode(this.heure);
		int m = Integer.decode(this.minute);
		if (nbMinute <= m)
			this.minute = String.valueOf(m - nbMinute);
		else
		{
			this.minute = String.valueOf((60-(nbMinute%60)+m));
			this.heure = String.valueOf(h-(nbMinute/60)-1);
			/*if (nbMinute % 60 > 0)
				this.addMinute(60);*/
		}
		h = Integer.decode(this.heure);
		if (h < 0)
			h = 24 + h;
		this.heure = String.valueOf(h);
	}
	
	private Ligne getLigne(String nomLigne)
	{
		for (Ligne l : this.ligneArrivee)
		{
			if (l.getNom().equals(nomLigne))
				return l;
		}
		for (Ligne l2 : this.ligneDepart)
		{
			if (l2.getNom().equals(nomLigne))
				return l2;
		}
		return null;
	}
	
	private boolean autreLigneEnCommun(Station a, Station b, Ligne l)
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
				if (!buffer.equals(itB.next()))
					return true;
			}
		}
		return false;
	}
	
	private Ligne autreLigne(Station s, Ligne l)
	{
		Set<String> listeLigne = s.listeLigne().keySet();
		Iterator<String> it = listeLigne.iterator();
		String buffer;
		while(it.hasNext())
		{
			buffer = it.next();
			Log.d(TAG, "autreligne de " + s.getNom() + " = " + buffer);
			if (!buffer.equals(l.getNom()))
				return Bdd.nouvelleLigne(buffer);
		}
		return null;
	}
	
	private ArrayList<Ligne> toArrayList(HashMap<String, Boolean> listeLigne)
	{
		ArrayList<Ligne> resultat = new ArrayList<Ligne>();
		Set<String> setString = listeLigne.keySet();
		Iterator<String> it = setString.iterator();
		while (it.hasNext())
		{
			resultat.add(Bdd.nouvelleLigne(it.next()));
		}
		return resultat;
	}
	
	private ArrayList<Ligne> supprLigne(ArrayList<Ligne> listeLigne, ArrayList<Ligne> listeLigne2)
	{
		ArrayList<Ligne> resultat = new ArrayList<Ligne>();
		for (Ligne l : listeLigne)
		{
			if (!this.estDansListe(listeLigne2, l))
			{
				Log.d(TAG, "supprLigne ; added = " + l.getNom());
				resultat.add(l);
				
			}
		}
		return resultat;
	}
	
	private boolean estDansListe(Ligne l)
	{
		Log.d(TAG, "estDansListe ; l = " + l.getNom());
		for (Ligne li : this.bl)
		{
			Log.d(TAG, "estDansListe ; li = " + li.getNom());
			if (li.getNom().equals(l.getNom()))
				return true;
		}
		return false;
	}
	
	private boolean estDansListe(ArrayList<Ligne> listeLigne, Ligne l)
	{
		Log.d(TAG, "estDansListe ; l = " + l.getNom());
		for (Ligne li : listeLigne)
		{
			Log.d(TAG, "estDansListe ; li = " + li.getNom());
			if (li.getNom().equals(l.getNom()))
				return true;
		}
		return false;
	}
}