package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import ca.uqam.info.mgl7460.annotations.annotation.Traceable;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import java.util.Iterator;
import java.util.List;

public class MethodWritter {

    private ExecutableElementWritter executableElementWritter = new ExecutableElementWritter();



    public void ecrireMethodeCourante(ExecutableElement methode, StringBuilder texteAGenerer, String typeDeClasse) {
        if (verifierPresenceAnnotationTraceable(methode)) {
            redefinirMethodeAnnotee(methode, texteAGenerer, typeDeClasse);
        } else {
            redefinirMethodeNonAnnotee(methode, texteAGenerer, typeDeClasse);
        }
    }


    private boolean verifierPresenceAnnotationTraceable(ExecutableElement methode){
        return methode.getAnnotation(Traceable.class) != null ;
    }

    private void redefinirMethodeNonAnnotee(ExecutableElement methode, StringBuilder texteAGenerer, String typeDeClasse) {
        ecrireEnteteMethode(methode,texteAGenerer, typeDeClasse);
        ecrireContenuMethodeNonAnnotee(methode,texteAGenerer);
    }



    private void redefinirMethodeAnnotee(ExecutableElement methode, StringBuilder texteAGenerer, String typeDeClasse) {
        ecrireEnteteAnnotation(texteAGenerer);
        ecrireEnteteMethode(methode,texteAGenerer, typeDeClasse);
        ecrireContenuMethodeAnnotee(methode,texteAGenerer);
    }


    private void ecrireEnteteMethode(ExecutableElement methode, StringBuilder texteAGenerer, String typeDeClasse) {
        executableElementWritter.ecrireModifieurs(methode.getModifiers(),texteAGenerer);
        ecrireTypeDeRetour(methode.getReturnType(), texteAGenerer);
        executableElementWritter.ecrireNomEtParam(methode,methode.getParameters(),texteAGenerer, typeDeClasse);
    }
    private void ecrireContenuMethodeAnnotee(ExecutableElement methode, StringBuilder texteAGenerer) {
        ecrireContenuAdditionnel(methode,texteAGenerer);
        ecrireContenuMethodeNonAnnotee(methode,texteAGenerer);
    }

    private void ecrireContenuMethodeNonAnnotee(ExecutableElement elementExecutable, StringBuilder texteAGenerer) {
        texteAGenerer.append("\t\t");
        boolean retourneUneValeure = !elementExecutable.getReturnType().toString().equals("void");
        if (retourneUneValeure) {
            texteAGenerer.append("return ");
        }
        texteAGenerer.append("super");
        texteAGenerer.append("." + elementExecutable.getSimpleName().toString() + "(");

        List<? extends VariableElement> parameters = elementExecutable.getParameters();
        for (VariableElement parameter : parameters) {
            texteAGenerer.append(parameter.getSimpleName().toString()).append(", ");
        }
        if(parameters.size()>0)
            texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
        texteAGenerer.append(");\n\t}\n");
    }

    private void ecrireContenuAdditionnel(ExecutableElement methode, StringBuilder texteAGenerer) {
        Traceable annotation = methode.getAnnotation(Traceable.class);
        String levelAnnote = annotation.level();
        texteAGenerer.append("\t\tlogger.log(Level." + levelAnnote + ", \"Call to ")
                .append(methode.getSimpleName().toString()).append("(");
        List<? extends VariableElement> parameters = methode.getParameters();

        for (Iterator<? extends VariableElement> iterator = parameters.iterator(); iterator.hasNext();) {
            VariableElement parameter = iterator.next();
            String param = parameter.getSimpleName().toString();
            texteAGenerer.append(param +": \" + "+ param );
            if(iterator.hasNext()){
                texteAGenerer.append(" + \"").append(", ");
            }else{
               texteAGenerer.append(" + \")\"").append(", ");
            }
        }

        if (parameters.size() > 0) {
            texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
        } else {
            texteAGenerer.append(")\"");
        }
        texteAGenerer.append(");\n");
    }

    private void ecrireEnteteAnnotation(StringBuilder texteAGenerer) {
        texteAGenerer.append("\t@Override\n");
    }

    private void ecrireTypeDeRetour(TypeMirror typeDeRetour, StringBuilder texteAGenerer) {
        texteAGenerer.append(typeDeRetour.toString()).append(" ");
    }
}
