import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ue1396.Pret;
import ue1396.Projet;

@DisplayName("Tests d'intégration - Ensemble des composants : ")
public class ITApp {
    @Disabled
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
    @DisplayName("Calcul total du projet d'achat + Apport minimal + Reste à emprunter: Validation des scénarios")
    public void calculTotalProjetAchatHappyPath() {
        Projet projetBasDeGamme = new Projet();
        Projet projetNominal = new Projet();
        Projet projetHautDeGamme = new Projet();

        projetBasDeGamme.setPrixHabitation(100_000.00);
        projetBasDeGamme.setRevenuCadastral(700);
        projetBasDeGamme.setFraisNotaireAchat(4_500);
        projetBasDeGamme.setFraisTransformation(50_000);

        projetNominal.setPrixHabitation(300_000.00);
        projetNominal.setRevenuCadastral(700);
        projetNominal.setFraisNotaireAchat(10_000);
        projetNominal.setFraisTransformation(50_000);

        projetHautDeGamme.setPrixHabitation(600_000.00);
        projetHautDeGamme.setRevenuCadastral(1000);
        projetHautDeGamme.setFraisNotaireAchat(25_000);
        projetHautDeGamme.setFraisTransformation(100_000);
        Assertions.assertAll(
            () -> Assertions.assertEquals(161_100.00, projetBasDeGamme.calculTotalProjetAchat(), 0.0001),
            () -> Assertions.assertEquals(23_400.00, projetBasDeGamme.calculApportMinimal(), 0.0001),
            () -> Assertions.assertEquals(137_700.00, projetBasDeGamme.calculResteAEmprunter()),
            () -> Assertions.assertEquals(378_600.00, projetNominal.calculTotalProjetAchat(), 0.001),
            () -> Assertions.assertEquals(60_900.00, projetNominal.calculApportMinimal(), 0.001),
            () -> Assertions.assertEquals(317_700.00, projetNominal.calculResteAEmprunter()),
            () -> Assertions.assertEquals(803_500.00, projetHautDeGamme.calculTotalProjetAchat(), 0.0001),
            () -> Assertions.assertEquals(168_100.00, projetHautDeGamme.calculApportMinimal(), 0.0001),
            () -> Assertions.assertEquals(635_400.00, projetHautDeGamme.calculResteAEmprunter())
        );
    }
}
