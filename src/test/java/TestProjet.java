import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
        @Test
        @DisplayName("Calcul de la tva sur les frais de transformation : Validation des frais positifs")
        public void testCalculTVA() {
            Assertions.assertAll(() -> {
                projet.setFraisTransformation(90_000.00);
                Assertions.assertEquals(5_400.00, projet.calculTVAFraisTransformation());
            }, () -> {
                projet.setFraisTransformation(0.00);
                Assertions.assertEquals(0.00, projet.calculTVAFraisTransformation());
            }, () -> {
                projet.setFraisTransformation(100_000.00);
                Assertions.assertEquals(6_000.00, projet.calculTVAFraisTransformation());
            }, () -> {
                projet.setFraisTransformation(59_595.00);
                Assertions.assertEquals(3_575.70, projet.calculTVAFraisTransformation());
            }, () -> {
                projet.setFraisTransformation(1.00);
                Assertions.assertEquals(0.06, projet.calculTVAFraisTransformation());
            });
        }

        @Test
        @DisplayName("Calcul de la tva sur les frais de transformation : Validation des frais arrondis")
        public void testCalculTVA2() {
            Assertions.assertAll(() -> {
                projet.setFraisTransformation(92_123.89);
                Assertions.assertEquals(5_527.4334, projet.calculTVAFraisTransformation());
            });
        }

        @Disabled
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
        @Test
        @DisplayName("Calcul du droit d'enregistrement : Revenu Cadastral inferieur à 745")
        public void calculDroitEnregistrementRevenuCadastralInferieur745(){
            Assertions.assertAll(() -> {
                mockedProject.setPrixHabitation(350_000.00);
                Mockito.doReturn(40_000.00).when(mockedProject).calculAbattement();
                mockedProject.setRevenuCadastral(740);
                Assertions.assertEquals(18_599.999584257603, mockedProject.calculDroitEnregistrement());
            });
        }
    }
}
