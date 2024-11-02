package ca.uqam.info.mgl7460.annotations.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.uqam.info.mgl7460.annotation.user.Client;
import ca.uqam.info.mgl7460.annotation.user.ClientFactory;
import ca.uqam.info.mgl7460.annotation.user.Produit;
import ca.uqam.info.mgl7460.annotation.user.ProduitFactory;

public class TestAnnotationClient {

    private static ClientFactory fabriqueClient;

    private static ProduitFactory fabriqueProduit;

//    private final static ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final static ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final static PrintStream originalOut = System.out;
    private final static PrintStream originalErr = System.err;


    private static Produit chaise;

    private static Produit table;

    private static Produit lampe;

    private static final String AJOUTE_PRODUIT = "ajouteProduit";
    private static final String DEJA_PRIS_PRODUIT= "dejaPrisProduit";
    private static final String GET_NOM = "getNom";

    @BeforeAll
    public static void initializeFactories() {
        fabriqueClient = new ClientFactory();
        fabriqueProduit = new ProduitFactory();
        System.setErr(new PrintStream(errContent));
        chaise = fabriqueProduit.createProduit("Chaise de table à manger");
        table = fabriqueProduit.createProduit("Table","Table for salle à manger");
        lampe = fabriqueProduit.createProduit("Lampe", "Lampe de bureau");

    }
    
    @Test
    public void testerCreationClient() {
        Client francois = fabriqueClient.createClient("François", "Tremblay");
        Assertions.assertNotNull(francois);
    }

    @Test
    public void testerMethodeNonTracee() {
        Client helene = fabriqueClient.createClient("Hélène", "Boulay");
        Assertions.assertNotNull(helene);
        Assertions.assertEquals("Hélène", helene.getPrenom());
        Assertions.assertFalse(errContent.toString().contains("getPrenom"));
    }

    @Test
    public void testerTracerDejaPrisProduit() {
        Client mohammed = fabriqueClient.createClient("Mohammed", "Jalal");
        Assertions.assertFalse(mohammed.dejaPrisProduit(chaise));
//        System.out.println("DUMPING THE LOG in testerTracerDejaPrisProduit: "+errContent.toString());
        Assertions.assertFalse(errContent.toString().contains(AJOUTE_PRODUIT));
        Assertions.assertTrue(errContent.toString().contains(DEJA_PRIS_PRODUIT));
    }

    @Test
    public void testerTracerAjouteProduit() {
        Client francois = fabriqueClient.createClient("François", "Tremblay");
        francois.ajouteProduit(chaise);
//        System.out.println("DUMPING THE LOG in testerTracerAjouteProduit: "+errContent.toString());
        Assertions.assertTrue(francois.dejaPrisProduit(chaise), "Francois n'a pas commande une chaise");
        Assertions.assertFalse(francois.dejaPrisProduit(lampe), "François a commandé une lampe");
        Assertions.assertTrue(errContent.toString().contains(AJOUTE_PRODUIT), "Trace ne contient pas ajouteProduit");
        Assertions.assertTrue(errContent.toString().contains(DEJA_PRIS_PRODUIT));
    }

    @Test
    public void testerClasseOriginalePasTracee() {
        Client amadou = new Client("Amadou", "Diallo");
        amadou.ajouteProduit(chaise); 
        Assertions.assertFalse(errContent.toString().contains(AJOUTE_PRODUIT));
        Assertions.assertTrue(amadou.dejaPrisProduit(chaise));
        Assertions.assertFalse(errContent.toString().contains(DEJA_PRIS_PRODUIT));

    }

    @Test
    public void testerTraceGetNom() {
        Client felix = fabriqueClient.createClient("Félix", "Lavallée");
        String nom = felix.getNom();
//        System.out.println("DUMPING THE LOG in testerTraceGetNom: "+errContent.toString());
        Assertions.assertTrue(errContent.toString().contains(GET_NOM));
        Assertions.assertEquals("Lavallée",nom);
    }

    @AfterEach
    public void flushLog() {    
        errContent.reset();
    }

    @AfterAll
    public static void resetStreams() {
        System.setErr(originalErr);
    }
}
