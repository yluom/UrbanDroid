package fr.android.urbandroid;

import java.io.*;

public class Station {
	private float longitude; // coordonnées X de la station. exemple: 43.02897
	private float latitude; // idem pour Y
	private boolean terminus; // terminus=1 -> this est le terminus de la ligne
	private String nom; // ex: "Fac de Pharma"
	
	public Station(float longitude, float latitude, boolean terminus, String nom) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.terminus = terminus;
		this.nom = nom;
	}
	public Station(float longitude, float latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}
	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}
	/**
	 * @return the terminus
	 */
	public boolean isTerminus() {
		return terminus;
	}
	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}
	

}
