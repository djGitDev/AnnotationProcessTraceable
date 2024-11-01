//package ca.uqam.info.mgl7460.annotation.user;
//
//import ca.uqam.info.mgl7460.annotations.annotation.Traceable;
//
//public class ExampleClass {
//
//    private String nom;
//    private String prenom;
//
//    // ne sera PAS redéfini dans AnotherClassLogged
//    public ExampleClass(){
//        nom = "Dupont";
//        prenom = "Amadou";
//    }
//
//    // sera rédéfini dans AnotherClassLogged
//    public ExampleClass(String pre){
//        nom = "Tremblay";
//        prenom = pre;
//    }
//
//    // sera rédéfini dans AnotherClassLogged
//    public ExampleClass(String n, String p){
//        nom = n;
//        prenom = p;
//    }
//
//    @Traceable(level="INFO")
//    public String getPrenom() {return prenom;}
//
//    public String getNom() {return nom;}
//
//    @Traceable(level="WARNING")
//    public String getNomComplet() {
//        return getPrenom() + " " + getNom();
//    }
//
//}
