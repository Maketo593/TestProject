import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ue1396.Projet;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("Test de la classe Projet : ")
public class TestProjet {
    private static Projet projet;
    private static Projet mockedProject;

    @BeforeAll
    public static void init() {
        projet = new Projet();
        mockedProject = Mockito.spy(projet);
    }

    @Nested
    @Order(1)
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("Calcul de la TVA sur les frais de transformation")
    class TestCalculTVA {
        @ParameterizedTest
        @Order(1)
        @CsvSource({"90_000.00, 5_400.00", "0.00, 0.00", "100_000.00, 6_000.00", "59_595.00, 3_575.70", "1.00, 0.06"})
        @DisplayName("Validation des frais positifs")
        public void testCalculTVA(double fraisTransformation, double resultat) {
            projet.setFraisTransformation(fraisTransformation);
            Assertions.assertEquals(resultat, projet.calculTVAFraisTransformation());
        }


        @ParameterizedTest
        @Order(2)
        @CsvSource({"92_123.89, 5_527.4334"})
        @DisplayName("Validation des frais arrondis")
        public void testCalculTVA2(double fraisTransformation, double resultat) {
            projet.setFraisTransformation(fraisTransformation);
            Assertions.assertEquals(resultat, projet.calculTVAFraisTransformation());
        }

        @ParameterizedTest
        @Order(3)
        @ValueSource(doubles = { -90_000.00, -25_000.00})
        @DisplayName("Validation des frais negatifs")
        public void testCalculTVA3(double fraisTransformation) {
            projet.setFraisTransformation(fraisTransformation);
            Assertions.assertThrows(Exception.class, () -> projet.calculTVAFraisTransformation());
        }
    }

    @Nested
    @Order(2)
    @DisplayName("Calcul du droit de l'abbatement")
    class TestCalculDroitAbbatement {
        @ParameterizedTest
        @CsvSource({"100_000, 40_000", "200_000, 40_000", "400_000, 33_333.33", "500_000, 20_000", "600_000, 20_000"})
        @DisplayName("Calcul de l'abbatement")
        public void testCalculAbattement(double prixHabitation, double abattement) {
            projet.setPrixHabitation(prixHabitation);
            Assertions.assertEquals(abattement, projet.calculAbattement(), 0.005);
        }
    }

    @Nested
    @Order(3)
    @DisplayName("Calcul du droit d'enregistrement")
    class TestCalculDroitEnregistrement {
        @ParameterizedTest
        @CsvSource({"350_000.00, 40_000.00, 740, 18_599.999584257603", "180_000.00, 40_000.00, 575, 8_400.00", "100_000.00, 40_000.00, 700, 3_600.00"})
        @DisplayName("Revenu Cadastral inferieur à 745")
        public void calculDroitEnregistrementRevenuCadastralInferieur745(double prixHabitation, double abattement, int revenuCadastral, double resultat) {
                mockedProject.setPrixHabitation(prixHabitation);
                Mockito.doReturn(abattement).when(mockedProject).calculAbattement();
                mockedProject.setRevenuCadastral(revenuCadastral);
                Assertions.assertEquals(resultat, mockedProject.calculDroitEnregistrement(), 0.001);
            
        }

        @ParameterizedTest
        @CsvSource({"400_000.00, 33_333.333, 746, 45_833.333"})
        @DisplayName("Revenu Cadastral superieur à 745")
        public void calculDroitEnregistrementRevenuCadastralSuperieur745(double prixHabitation, double abattement, int revenuCadastral, double resultat) {
                mockedProject.setPrixHabitation(prixHabitation);
                Mockito.doReturn(abattement).when(mockedProject).calculAbattement();
                mockedProject.setRevenuCadastral(revenuCadastral);
                Assertions.assertEquals(resultat, mockedProject.calculDroitEnregistrement(), 0.001);
            
        }
    }

    @Nested
    @Order(4)
    @DisplayName("Calcul total du projet d'achat")
    class TestCalculTotalProjetAchat {
        @ParameterizedTest
        @CsvSource({"100_000.00, 700, 4_500.00, 3_600.00, 50_000.00, 3_000.00, 161_100.00",
                    "300_000.00, 700, 10_000.00, 15_600.00, 50_000.00, 3_000.00, 378_600.00",
                    "600_000.00, 1000, 25_000.00, 72_500.00, 100_000.00, 6_000.00, 803_500.00"})
        @DisplayName("Données")
        public void testCalculTotalProjetAchat(double prixHabitation, int revenuCadastral,double fraisNotaireAchat, double calculDroitEnregistrement, double fraisTransformation, double tvaFraisTransformation, double resultat) {
            mockedProject.setPrixHabitation(prixHabitation);
            mockedProject.setFraisTransformation(fraisTransformation);
            mockedProject.setFraisNotaireAchat(fraisNotaireAchat);
            mockedProject.setRevenuCadastral(revenuCadastral);
            Mockito.doReturn(tvaFraisTransformation).when(mockedProject).calculTVAFraisTransformation();
            Mockito.doReturn(calculDroitEnregistrement).when(mockedProject).calculDroitEnregistrement();
            Assertions.assertEquals(resultat, mockedProject.calculTotalProjetAchat());
        }
    }

    @Nested
    @DisplayName("Calcul de l'apport minimal")
    class TestCalculApportMinimal {
        @ParameterizedTest
        @CsvSource({"100_000.00, 700, 4_500.00, 3_600.00, 50_000.00, 3_000.00, 23_400.00",
                    "300_000.00, 700, 10_000.00, 15_600.00, 50_000.00, 3_000.00, 60_900.00",
                    "600_000.00, 1000, 25_000.00, 72_500.00, 100_000.00, 6_000.00, 168_100.00"})
        @DisplayName("Données")
        public void testCalculApportMinimal(double prixHabitation, int revenuCadastral,double fraisNotaireAchat, double calculDroitEnregistrement, double fraisTransformation, double tvaFraisTransformation, double resultat) {
            mockedProject.setPrixHabitation(prixHabitation);
            mockedProject.setFraisTransformation(fraisTransformation);
            mockedProject.setFraisNotaireAchat(fraisNotaireAchat);
            mockedProject.setRevenuCadastral(revenuCadastral);
            Mockito.doReturn(tvaFraisTransformation).when(mockedProject).calculTVAFraisTransformation();
            Mockito.doReturn(calculDroitEnregistrement).when(mockedProject).calculDroitEnregistrement();
            Assertions.assertEquals(resultat, mockedProject.calculApportMinimal());
        }
    }
}
