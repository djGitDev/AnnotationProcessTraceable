package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Set;

public class ExecutableElementWritter {


    public void ecrireNomEtParam(ExecutableElement elementExecutable, List<? extends VariableElement> parameters, StringBuilder texteAGenerer, String typeDeClasse) {
        if((elementExecutable.getSimpleName().toString()).equals("<init>")) {
            Element elementClass = elementExecutable.getEnclosingElement();
            if(typeDeClasse.equals("Logged")){
                texteAGenerer.append(elementClass.getSimpleName().toString()).append("Logged");
            }else{
                texteAGenerer.append(elementClass.getSimpleName().toString()).append(" create").append(elementClass.getSimpleName().toString());
            }
        }else {
            texteAGenerer.append(elementExecutable.getSimpleName().toString());
        }
        texteAGenerer.append("(");
        if (parameters.size() > 0) {
            for (VariableElement param : parameters) {
                texteAGenerer.append(param.asType().toString()).append(" ").append(param.getSimpleName().toString()).append(", ");
            }
            texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
            texteAGenerer.append(") {\n");
        }else{
            texteAGenerer.append(") {\n");

        }
    }
    public void ecrireModifieurs(Set<Modifier> modifiers, StringBuilder texteAGenerer) {
        for (Modifier mod : modifiers) {
            texteAGenerer.append("\t").append(mod.toString()).append(" ");
        }
    }
}
