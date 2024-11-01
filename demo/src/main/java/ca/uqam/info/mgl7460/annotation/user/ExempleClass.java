package ca.uqam.info.mgl7460.annotation.user;


import ca.uqam.info.mgl7460.annotations.annotation.Traceable;

public class ExempleClass {

    private String nom;

    @Traceable(level= "Warning")
    public void setNom(String unNom) {
        this.nom = unNom;
    }

}
