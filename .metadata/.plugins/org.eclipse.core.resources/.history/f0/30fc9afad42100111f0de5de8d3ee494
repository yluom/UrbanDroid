package urban.droid;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

public class MiseAJour 
{

	// afin de r�cup�rer les fichiers disponibles en ressources
	private final Resources r = Resources.getSystem();
	
	// notre lien de check de mise a jour
	// de la forme : http://www.site.com/urbandroid/check.urb
	private String urlCheck;
	
	public MiseAJour(String urlCheck)
	{
		// v�rifier si le lien est valide
		this.urlCheck = urlCheck;
	}
	
	// v�rifie si une mise � jour doit �tre faites ou non
	// retourne TRUE si c'est le cas
	// retourne FALSE sinon
	public boolean testMaj()
	{
		// t�l�charger le fichier contenant le num�ro de version de la bdd
		// v�rifier son contenus avec le string que l'on a d�j�
		// si il est �gal retourne faux
		return false;
		// sinon retourne vrai sinon
		// return true;
	}
	// effectue une mise � jour de la base de donn�e
	// précondition : this.testMaj() == false
	// postcondition : string checkM�j mis � jour ainsi que la base de donn�e
	public boolean faireMaj() throws Exception
	{
		if (!this.testMaj())
			throw(new Exception("MiseAJour.testMaj() - Vérification mise a jour failed."));
		
		// t�l�chargez le nouveau fichier check.urb et le mettre � la place de l'ancien
		String versionActuelle = r.getString(android.R.string.untitled);
		
		// t�l�chargez la nouvelle base de donn�es et la mettre � la place de l'ancienne
		// si le t�l�chargement s'est bien pass� alors retourne vrai
		// sinon retourne faux
		return false;
	}
	
	private String getVersion()
	{
		
	}
	
}
