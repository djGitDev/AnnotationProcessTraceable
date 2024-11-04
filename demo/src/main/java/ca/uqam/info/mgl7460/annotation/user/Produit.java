package ca.uqam.info.mgl7460.annotation.user;

import ca.uqam.info.mgl7460.annotations.annotation.Traceable;

public class Produit {

   private static int nombre_produit = 0;

   private String idProduit;

   private String nom;

   private String description;

   private float prixUnitaire;

   public Produit() {
       nombre_produit = nombre_produit+1;
       idProduit = "PROD_"+ nombre_produit;
   }

   public Produit(String n){
       this();
       nom = n;
   }

   public Produit(String n, String d){
       this(n);
       description = d;
   }

   @Traceable(level="INFO")
   public String toString() {
       return idProduit + "[ nom: " + nom + ", prix unitaire: "+prixUnitaire + "]";
   }

   @Traceable
   public void setDescription(String d){
       description = d;
   }

   public String getDescription() { return description;}

   @Traceable
   public void setPrixUnitaire(float pu){
       prixUnitaire = pu;
   }
}
