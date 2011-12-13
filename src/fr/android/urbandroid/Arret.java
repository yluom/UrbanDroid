package fr.android.urbandroid;

import java.io.*;

public class Arret extends Ligne {
	private float coordX; // coordonnées X de la station.
	private float coordY;
	private String nom; // nom de la station, ex: "Fac de pharmacie"
	
	
	
	/**
	 * @return the coordX
	 */
	public float getCoordX() {
		return coordX;
	}
	/**
	 * @param coordX the coordX to set
	 */
	public void setCoordX(float coordX) {
		this.coordX = coordX;
	}
	/**
	 * @return the coordY
	 */
	public float getCoordY() {
		return coordY;
	}
	/**
	 * @param coordY the coordY to set
	 */
	public void setCoordY(float coordY) {
		this.coordY = coordY;
	}
	
	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
}
