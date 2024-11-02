package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.List;

public class ConstructorWritter {

    private ExecutableElementWritter executableElementWritter = new ExecutableElementWritter();

    public void ecrireConstructeurCourant(ExecutableElement constructeur, StringBuilder texteAGenerer) {
        ecrireEnteteConstructeur(constructeur,texteAGenerer );
        ecrireContenuConstructeur(constructeur,texteAGenerer);

    }

    private void ecrireEnteteConstructeur(ExecutableElement constructeur, StringBuilder texteAGenerer) {
        executableElementWritter.ecrireModifieurs(constructeur.getModifiers(),texteAGenerer);
        executableElementWritter.ecrireNomEtParam(constructeur,constructeur.getParameters(),texteAGenerer);
    }


    private void ecrireContenuConstructeur(ExecutableElement elementExecutable, StringBuilder texteAGenerer) {
        texteAGenerer.append("\t\tsuper(");

        List<? extends VariableElement> parameters = elementExecutable.getParameters();
        for (VariableElement parameter : parameters) {
            texteAGenerer.append(parameter.getSimpleName().toString()).append(", ");
        }
        if(parameters.size()>0)
            texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
        texteAGenerer.append(");\n\t}\n");
    }

}
