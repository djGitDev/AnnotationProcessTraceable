package ca.uqam.info.mgl7460.annotation.user;

import ca.uqam.info.mgl7460.annotations.annotation.Traceable;

public class NewExampleClass {

   private String nom;

   @Traceable(level="INFO")
   protected String methodOne(String argumentOne) {
       if (argumentOne.length() < 10) nom = "Jean";
       return "Bonjour de la part de " + nom;
   }

   @Traceable(level="SEVERE")
   protected String methodTwo(String argumentOne, String argumentTwo) {
       if (argumentOne.length() < 10) nom = "Jean";
       return "Bonjour de la part de " + nom;
   }
}
