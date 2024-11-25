import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ue1396.Projet;

@DisplayName("Test de la classe Projet")
public class TestProjet {
    private static Projet projet;
    private static Projet mockedProject;

    @BeforeAll
    public static void init() {
        projet = new Projet();
        mockedProject = Mockito.spy(projet);
    }

    @Nested
    @DisplayName("Test de la classe Calcul de la TVA sur les frais de transformation")
    class TestCalculTVA {
        @ParameterizedTest
        @CsvSource({"90_000.00, 5_400.00", "0.00, 0.00", "100_000.00, 6_000.00", "59_595.00, 3_575.70", "1.00, 0.06"})
        @DisplayName("Calcul de la tva sur les frais de transformation : Validation des frais positifs")
        public void testCalculTVA( double fraisTransformation, double resultat) {
            projet.setFraisTransformation(fraisTransformation);
            Assertions.assertEquals(resultat, projet.calculTVAFraisTransformation());
        }


        @ParameterizedTest
        @CsvSource({"92_123.89, 5_527.4334"})
        @DisplayName("Calcul de la tva sur les frais de transformation : Validation des frais arrondis")
        public void testCalculTVA2( double fraisTransformation, double resultat) {
            projet.setFraisTransformation(fraisTransformation);
            Assertions.assertEquals(resultat, projet.calculTVAFraisTransformation());
        }

        @ParameterizedTest
        @ValueSource(doubles = { -90_000.00, -25_000.00})
        @DisplayName("Calcul de la tva sur les frais de transformation : Validation des frais negatifs")
        public void testCalculTVA3(double fraisTransformation) {
            projet.setFraisTransformation(fraisTransformation);
            Assertions.assertThrows(Exception.class, () -> projet.calculTVAFraisTransformation());
        }
    }

    @Nested
    @DisplayName("Test de la classe Calcul du droit de l'abbatement")
    class TestCalculDroitAbbatement {
        @ParameterizedTest
        @CsvSource({"200_000, 40_000", "400_000, 33_333.33", "500_000, 20_000", "600_000, 20_000"})
        @DisplayName("Calcul de l'abbatement")
        public void testCalculAbattement(double prixHabitation, double abattement) {
            projet.setPrixHabitation(prixHabitation);
            Assertions.assertEquals(abattement, projet.calculAbattement(), 0.005);
        }
    }

    @Nested
    @DisplayName("Test de la classe Calcul du droit d'enregistrement")
    class TestCalculDroitEnregistrement {
        @ParameterizedTest
        @CsvSource({"350_000.00, 40_000.00, 740, 18_599.999584257603", "180_000.00, 40_000.00, 575, 8_400.00", "100_000.00, 40_000.00, 700, 3_600.00"})
        @DisplayName("Calcul du droit d'enregistrement : Revenu Cadastral inferieur à 745")
        public void calculDroitEnregistrementRevenuCadastralInferieur745( double prixHabitation, double abattement, int revenuCadastral, double resultat) {
            Assertions.assertAll(() -> {
                mockedProject.setPrixHabitation(prixHabitation);
                Mockito.doReturn(abattement).when(mockedProject).calculAbattement();
                mockedProject.setRevenuCadastral(revenuCadastral);
                Assertions.assertEquals(resultat, mockedProject.calculDroitEnregistrement(), 0.001);
            });
        }

        @ParameterizedTest
        @CsvSource({"400_000.00, 33_333.333, 746, 45_833.333"})
        @DisplayName("Calcul du droit d'enregistrement : Revenu Cadastral superieur à 745")
        public void calculDroitEnregistrementRevenuCadastralSuperieur745( double prixHabitation, double abattement, int revenuCadastral, double resultat) {
            Assertions.assertAll(() -> {
                mockedProject.setPrixHabitation(prixHabitation);
                Mockito.doReturn(abattement).when(mockedProject).calculAbattement();
                mockedProject.setRevenuCadastral(revenuCadastral);
                Assertions.assertEquals(resultat, mockedProject.calculDroitEnregistrement(), 0.001);
            });
        }
    }
}
