//package ca.uqam.info.mgl7460.annotation.user;
//
//import java.util.*;
//
//import ca.uqam.info.mgl7460.annotations.annotation.Traceable;
//
//public class Client {
//
//    private String prenom;
//
//    private String nom;
//
//    private Set<Produit> panier;
//
//    public Client(String p, String n) {
//        prenom = p;
//        nom = n;
//        panier = new HashSet<>();
//    }
//
//    public String getPrenom() {
//        return prenom;
//    }
//
//    @Traceable(level="WARNING")
//    public String getNom () {
//        return nom;
//    }
//
//    @Traceable(level="WARNING")
//    public void ajouteProduit(Produit p){
//        panier.add(p);
//    }
//
//    @Traceable(level="WARNING")
//    public boolean dejaPrisProduit(Produit p){
//        return panier.contains(p);
//    }
//
//
//}
