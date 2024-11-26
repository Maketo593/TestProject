import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Disabled;
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
        @CsvSource({"90_000, 5_400.00", "0, 0.00", "100_000, 6_000.00", "59_595, 3_575.70", "1, 0.06"})
        @DisplayName("Validation des frais positifs")
        public void testCalculTVAInputPositif(double fraisTransformation, double resultat) {
            projet.setFraisTransformation(fraisTransformation);
            Assertions.assertEquals(resultat, projet.calculTVAFraisTransformation());
        }


        @ParameterizedTest
        @Order(2)
        @CsvSource({"92_123.89, 5_527.4334"})
        @DisplayName("Validation des frais arrondis")
        public void testCalculTVAInputDecimal(double fraisTransformation, double resultat) {
            projet.setFraisTransformation(fraisTransformation);
            Assertions.assertEquals(resultat, projet.calculTVAFraisTransformation());
        }

        // Il faut réajuster la méthode calculTVAFraisTransformation() pour lancer une Exception si la valeur des frais de transformation est négative
        @Disabled
        @ParameterizedTest
        @Order(3)
        @ValueSource(doubles = { -90_000.00, -25_000.00})
        @DisplayName("Validation des frais negatifs")
        public void testCalculTVAInputNegatif(double fraisTransformation) {
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
        @DisplayName("Prix Habitation (Nombre entier)")
        public void testCalculAbattement(double prixHabitation, double abattement) {
            projet.setPrixHabitation(prixHabitation);
            Assertions.assertEquals(abattement, projet.calculAbattement(), 0.005);
        }

        @ParameterizedTest
        @CsvSource({"150_000.56, 40_000", "349_999.99, 40_000","450_250.33, 26_633.2893", "456_654.75, 25_779.3666", "499_999.99, 20_000.0013"})
        @DisplayName("Prix Habitation (Nombre décimal)")
        public void testCalculAbattementInputDecimal(double prixHabitation, double abattement) {
            projet.setPrixHabitation(prixHabitation);
            Assertions.assertEquals(abattement, projet.calculAbattement(), 0.005);
        }


        // Il faut réajuster la méthode calculAbattement() pour lancer une Exception si la valeur du prix d'habitation est négative
        @Disabled
        @ParameterizedTest
        @ValueSource(doubles = { -100_000, -360_000, -500_000, -600_000})
        @DisplayName("Prix Habitation (Nombre négatif)")
        public void testCalculAbattementInputNegatif(double prixHabitation) {
            projet.setPrixHabitation(prixHabitation);
            Assertions.assertEquals(Exception.class, projet.calculAbattement());
        }
    }

    @Nested
    @Order(3)
    @DisplayName("Calcul du droit d'enregistrement")
    class TestCalculDroitEnregistrement {
        @ParameterizedTest
        @CsvSource({"350_000, 40_000, 740, 18_599.999", "180_000, 40_000, 575, 8_400", "100_000, 40_000, 700, 3_600"})
        @DisplayName("Revenu Cadastral inferieur à 745")
        public void calculDroitEnregistrementRevenuCadastralInferieur745(double prixHabitation, double abattement, int revenuCadastral, double resultat) {
                mockedProject.setPrixHabitation(prixHabitation);
                Mockito.doReturn(abattement).when(mockedProject).calculAbattement();
                mockedProject.setRevenuCadastral(revenuCadastral);
                Assertions.assertEquals(resultat, mockedProject.calculDroitEnregistrement(), 0.001);
            
        }

        @ParameterizedTest
        @CsvSource({"400_000, 33_333.3333, 746, 45_833.3333"})
        @DisplayName("Revenu Cadastral superieur à 745")
        public void calculDroitEnregistrementRevenuCadastralSuperieur745(double prixHabitation, double abattement, int revenuCadastral, double resultat) {
                mockedProject.setPrixHabitation(prixHabitation);
                Mockito.doReturn(abattement).when(mockedProject).calculAbattement();
                mockedProject.setRevenuCadastral(revenuCadastral);
                Assertions.assertEquals(resultat, mockedProject.calculDroitEnregistrement(), 0.0001);
            
        }

        @ParameterizedTest
        @CsvSource({"450_250.33, 26_633.28933333, 1200, 52_952.13"})
        @DisplayName("Prix Habitation (Nombre décimal)")
        public void calculDroitEnregistrementPrixHabitationInputDecimal(double prixHabitation, double abattement, int revenuCadastral, double resultat) {
                mockedProject.setPrixHabitation(prixHabitation);
                Mockito.doReturn(abattement).when(mockedProject).calculAbattement();
                mockedProject.setRevenuCadastral(revenuCadastral);
                Assertions.assertEquals(resultat, mockedProject.calculDroitEnregistrement(), 0.0001);
            
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
    @Order(5)
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

    @Nested
    @DisplayName("Calcul du reste à emprunter à la banque")
    class TestCalculResteAEmprunter {
        @ParameterizedTest
        @CsvSource({"161_100.00, 23_400.00, 137_700.00",
                    "378_600.00, 60_900.00, 317_700.00",
                    "803_500.00, 168_100.00, 635_400.00"})
        @DisplayName("Données")
        public void testCalculResteAEmprunter(double totalProjetAchat, double apportMinimal, double resultat) {
            Mockito.doReturn(totalProjetAchat).when(mockedProject).calculTotalProjetAchat();
            Mockito.doReturn(apportMinimal).when(mockedProject).calculApportMinimal();
            Assertions.assertEquals(resultat, mockedProject.calculResteAEmprunter());
        }
    }
}
