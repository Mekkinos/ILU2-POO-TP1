package villagegaulois;

import personnages.*;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtals);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() throws VillageSansChefException {
		StringBuilder chaine = new StringBuilder();
		if (chef == null) {
			throw new VillageSansChefException("Il n'y a pas de chef");
			}
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef " + chef.getNom() + ".\n");
			} else {
				chaine.append("Au village du chef " + chef.getNom() + " vivent les légendaires gaulois :\n");
				for (int i = 0; i < nbVillageois; i++) {
					chaine.append("- " + villageois[i].getNom() + "\n");
					}
				}
		return chaine.toString();
		}
	
	public String installerVendeur(Gaulois vendeur, String produit,int nbProduit) {
		StringBuilder chaine = new StringBuilder();
		int trouve = marche.trouverEtalLibre();
		chaine.append(vendeur.getNom() + " cherche un endroit pour vendre " + nbProduit + " " + produit + ".\n");
		if (trouve == -1) {
			chaine.append("Tous les étals sont occupés !");
		 } else {
			 marche.utiliserEtal(trouve, vendeur, produit, nbProduit);
			 chaine.append("Le vendeur " + vendeur.getNom() + " vend des " + produit + " à l'étal n° " + (trouve + 1) + ".");
		  }
		  return chaine.toString() + "\n";
		 }
	
	public String rechercherVendeursProduit(String produit) {
		StringBuilder chaine = new StringBuilder();
		chaine.append("Les vendeurs qui proposent des " + produit + " sont: \n");
		Etal[] etalsProduit = marche.trouverEtals(produit);
		if (etalsProduit != null) {
			for (int i = 0; i < etalsProduit.length; i++) {
				chaine.append("-" + etalsProduit[i].getVendeur().getNom() + "\n");
			}
		 }
		
		return chaine.toString();
		}

	public Etal rechercherEtal(Gaulois vendeur) {
		  return marche.trouverVendeur(vendeur);
		  }
	
	public String partirVendeur(Gaulois vendeur) {
		Etal etal = marche.trouverVendeur(vendeur);
		StringBuilder chaine = new StringBuilder();
		if (etal != null) {
			chaine.append(etal.libererEtal());
		}
		return chaine.toString();
		}
	
	public String afficherMarche() { 
		String intro = "Le marché du village <<" + nom + ">> possède plusieurs étals :\n";
		return intro + marche.afficherMarche();
		}
	
	
	private static class Marche {
		
		private Etal[] etals;
		
		private Marche (int nbEtals) {
			etals = new Etal[nbEtals];
			for (int i=0; i<etals.length; i++ ) {
				etals[i]=new Etal();
				}
			}
		
		private void utiliserEtal(int indiceEtal, Gaulois vendeur,
				String produit, int nbProduit) {
			etals[indiceEtal].occuperEtal(vendeur,  produit,  nbProduit);
		}
		
		private int trouverEtalLibre() {
			int numeroEtal=-1;
			for (int i=0; i<etals.length; i++ ) {
				if (!etals[i].isEtalOccupe()) {
					numeroEtal=i;
					break;
				}
			}
			return numeroEtal;
		}
		
		private Etal[] trouverEtals(String produit) {
			int nbEtal=0;
			for (Etal etal: etals) {
				if(etal.isEtalOccupe() && etal.contientProduit(produit)) {
					nbEtal++;
				}
			}
			Etal[] etalsProd = null;
			if (nbEtal>0) {
				etalsProd = new Etal[nbEtal];
				int nbEtalTrouve = 0;
				for (int i=0; i<etals.length && nbEtalTrouve < nbEtal; i++ ) {
					if(etals[i].isEtalOccupe() && etals[i].contientProduit(produit)) {
						etalsProd[nbEtalTrouve]=etals[i];
						nbEtalTrouve++;
					}
				}
			}
			return etalsProd;
		}
		
		private Etal trouverVendeur(Gaulois gaulois) {
			Etal etal = new Etal();
			boolean trouve = false;
			for (int i=0;  i<etals.length && !trouve; i++) {
				if (etals[i].getVendeur() == gaulois) {
					etal = etals[i];
					trouve = true;
				}
			}
			
			return etal;
		}
		
		private String afficherMarche() {
			StringBuilder trouve = new StringBuilder();
			int nbEtalVide=0;
			for (int i=0;  i<etals.length; i++) {
				if (etals[i].isEtalOccupe()) {
					trouve.append(etals[i].afficherEtal());
				}
				else {
					nbEtalVide++;
				}
			}
			if (nbEtalVide!=0) {
				trouve.append("Il reste " + nbEtalVide + " étals non utilisés dans le marché.\n");
			}
			return trouve.toString();
		}
		
	}
}