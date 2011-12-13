/**
 * 
 */
package fr.android.urbandroid;

import java.util.*;


/**
 * @author leo
 *
 */
public class Ligne {
	// Attributs
	private String nom; // nom de la ligne... ex: A, B, T1, bus: 1, 2 , 3 .. etc
	private TypeLigne type; // type de la ligne, ex: METRO, BUS..
	//private ArrayList<Station> stations; // tableau des stations de la ligne
	private Map<Station, Horaires[]> stations;
	
	
	// Methodes
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @return the type
	 */
	public TypeLigne getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(TypeLigne type) {
		this.type = type;
	}
	
	
	
	
}
