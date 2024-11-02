package ca.uqam.info.mgl7460.annotation.user;

import ca.uqam.info.mgl7460.annotations.annotation.Traceable;

public class AverageClass {

   private String prenom;

   private String nom;

   // sera redéfini dans AverageClassLogged
   public AverageClass(String pre, String nm){
       prenom = pre;
       nom = nm;
   }

   // sera redéfini dans AverageClassLogged
   public AverageClass(String pre){
       this(pre,"Tremblay");
   }

   // sera redéfini dans AverageClassLogged
   public AverageClass() {
       this("François", "Tremblay");
   }

   @Traceable(level="INFO")
   public String getPrenom() {return prenom;}

   @Traceable(level="WARNING")
   public void setPrenom(String pre) {
       prenom = pre;
   }

   @Traceable(level="SEVERE")
   public String getNomComplet() { return prenom + " " + nom;}

}
