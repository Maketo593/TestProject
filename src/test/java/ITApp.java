import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ue1396.Pret;
import ue1396.Projet;

@DisplayName("Tests d'intégration - Ensemble des composants : ")
public class ITApp {
    @Test
    @DisplayName("Validation du meilleur scénario")
    public void happyPath() {
        Projet projet = new Projet();
        projet.setPrixHabitation(100_000.00);
        projet.setRevenuCadastral(700);
        projet.setFraisNotaireAchat(4_150);
        projet.setFraisTransformation(60_000);
        double apportPersonnel = projet.calculApportMinimal();
        double montantEmprunt = projet.calculResteAEmprunter();
        Pret pret = new Pret();
        pret.setFraisDossierBancaire(500);
        pret.setFraisNotaireCredit(5_475);
        pret.setNombreAnnees(15);
        pret.setTauxAnnuel(0.04);
        double apportBancaire = pret.calculRestantDu(montantEmprunt);
        Assertions.assertEquals(apportPersonnel + apportBancaire, 30_084.99991953373);
    }
    
    @Test
    @DisplayName("Calcul total du projet d'achat : Validation du meilleur scénario")
    public void happyPath2() {
        Projet projet2 = new Projet();
        projet2.setPrixHabitation(100_000.00);
        projet2.setRevenuCadastral(700);
        projet2.setFraisNotaireAchat(4_500);
        projet2.setFraisTransformation(50_000);
        Assertions.assertEquals(161_100.00, projet2.calculTotalProjetAchat(), 0.0001);
    }
}
