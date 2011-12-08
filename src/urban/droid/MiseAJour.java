package urban.droid;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;

public class MiseÀJour 
{

	// afin de récupérer les fichiers disponibles en ressources
	private final Resources r = Resources.getSystem();
	
	// notre lien de check de mise à jour
	// de la forme : http://www.site.com/urbandroid/check.urb
	private String urlCheck;
	
	public MiseÀJour(String urlCheck)
	{
		// vérifier si le lien est valide
		this.urlCheck = urlCheck;
	}
	
	// vérifie si une mise à jour doit être faites ou non
	// retourne TRUE si c'est le cas
	// retourne FALSE sinon
	public boolean testMàj()
	{
		// télécharger le fichier contenant le numéro de version de la bdd
		// vérifier son contenus avec le string que l'on a déjà
		// si il est égal retourne faux
		return false;
		// sinon retourne vrai sinon
		// return true;
	}
	// effectue une mise à jour de la base de donnée
	// précondition : this.testMàj() == false
	// postcondition : string checkMàj mis à jour ainsi que la base de donnée
	public boolean faireMàj() throws Exception
	{
		if (!this.testMàj())
			throw(new Exception("MiseÀJour.testMàj() - Vérification mise à jour failed."));
		
		// téléchargez le nouveau fichier check.urb et le mettre à la place de l'ancien
		String versionActuelle = r.getString(android.R.string.untitled);
		
		// téléchargez la nouvelle base de données et la mettre à la place de l'ancienne
		// si le téléchargement s'est bien passé alors retourne vrai
		// sinon retourne faux
		return false;
	}
	
	private String getVersion()
	{
		
	}
	
}
