package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;

public class ConstructorWritter {

    private ExecutableElementWritter executableElementWritter = new ExecutableElementWritter();

    public void ecrireConstructeurCourant(ExecutableElement constructeur, StringBuilder texteAGenerer, String typeDeClasse) {
        ecrireEnteteConstructeur(constructeur,texteAGenerer, typeDeClasse);
        ecrireContenuConstructeur(constructeur,texteAGenerer, typeDeClasse);

    }

    private void ecrireEnteteConstructeur(ExecutableElement constructeur, StringBuilder texteAGenerer, String typeDeClasse) {
        executableElementWritter.ecrireModifieurs(constructeur.getModifiers(),texteAGenerer);
        executableElementWritter.ecrireNomEtParam(constructeur,constructeur.getParameters(),texteAGenerer, typeDeClasse);
    }


    private void ecrireContenuConstructeur(ExecutableElement elementExecutable, StringBuilder texteAGenerer, String typeDeClasse) {
        if(typeDeClasse.equals("Logged")){
            texteAGenerer.append("\t\tsuper(");
        }else{
            texteAGenerer.append("\t\treturn new " + elementExecutable.getEnclosingElement().getSimpleName().toString() + "Logged(");
        }

        List<? extends VariableElement> parameters = elementExecutable.getParameters();
        for (VariableElement parameter : parameters) {
            texteAGenerer.append(parameter.getSimpleName().toString()).append(", ");
        }
        if(!parameters.isEmpty())
            texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());

       
        texteAGenerer.append(");\n\t}\n");
    }

}
