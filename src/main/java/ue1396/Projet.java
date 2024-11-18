package ue1396;

public class Projet {
    /*
     * Cette classe couvre l'ensemble des méthodes nécessaires au calcul d'un pret hypothecaire.
     */
    private int revenuCadastral;
    private double prixHabitation;
    private double fraisNotaireAchat;
    private double fraisTransformation; // Aka travaux de rénovation   
   


    /**
     * Calcule le total à emprunter à la banque pour le projet.
     * @return double emprunt
     */
    public double calculResteAEmprunter(){
        return this.calculTotalProjetAchat() -
            this.calculApportMinimal();
    }

    /**
     * Calcule l'apport minimal nécessaire pour le projet d'achat.
     * @return doube apport minimal
     */
    public double calculApportMinimal(){
        return 0.1 * 
            (this.prixHabitation +
             this. fraisTransformation +
             this.calculTVAFraisTransformation()
            ) +
             this.calculDroitEnregistrement() +
             this.fraisNotaireAchat;
    }

    /**
     * Calcule le coût total du projet d'Achat
     * @return double total
     */
    public double calculTotalProjetAchat(){
        return this.prixHabitation +
            this.fraisNotaireAchat +
            this.calculDroitEnregistrement() +
            this.fraisTransformation +
            this.calculTVAFraisTransformation();
    }

    /**
     * Calcule le montant du droit d'enregistrement.
     * @return double droit d'enregistrement
     */
    public double calculDroitEnregistrement(){
        float taux = 0.125f;
        if (this.revenuCadastral <= 745){
             taux = 0.06f;  
        }
        return (double) taux * (this.prixHabitation - this.calculAbattement());
    }

    /**
     * Calcule l'abattement fiscal possible. 
     * @return double abattement
     */
    public double calculAbattement(){
        if (this.prixHabitation < 350_000) return 40_000;
        if (this.prixHabitation >= 500_000) return 20_000;
        return 40_000 - (20_000 * (this.prixHabitation - 350_000) / (500_000 - 350_000));
    }

    /**
     * Calcule la tva sur les frais de transformation
     * @return doube TVA
     */
    public double calculTVAFraisTransformation(){
        return 0.06 * this.fraisTransformation;
    }

    public int getRevenuCadastral() {
        return revenuCadastral;
    }

    public void setRevenuCadastral(int revenuCadastral) {
        this.revenuCadastral = revenuCadastral;
    }

    public double getPrixHabitation() {
        return prixHabitation;
    }

    public void setPrixHabitation(double prixHabitation) {
        this.prixHabitation = prixHabitation;
    }

    public double getFraisNotaireAchat() {
        return fraisNotaireAchat;
    }

    public void setFraisNotaireAchat(double fraisNotaireAchat) {
        this.fraisNotaireAchat = fraisNotaireAchat;
    }

    public double getFraisTransformation() {
        return fraisTransformation;
    }

    public void setFraisTransformation(double fraisTransformation) {
        this.fraisTransformation = fraisTransformation;
    }

}

