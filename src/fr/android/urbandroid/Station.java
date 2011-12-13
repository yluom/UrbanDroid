package fr.android.urbandroid;

import java.io.*;

public class Station {
	private float longitude; // coordonnées X de la station. exemple: 43.02897
	private float latitude; // idem pour Y
	private boolean terminus; // terminus=1 -> this est le terminus de la ligne
	private String nom; // ex: "Fac de Pharma"
	
	
	
	
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
}
