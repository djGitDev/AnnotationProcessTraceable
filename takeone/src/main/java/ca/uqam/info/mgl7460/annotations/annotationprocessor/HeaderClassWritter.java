package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;

public class HeaderClassWritter {


    public void ecrireEnteteClasse(TypeElement classeDeLaMethodeAnnotee, StringBuilder texteAGenerer, String typeDeClasse){
        String nomClasseMere = classeDeLaMethodeAnnotee.getSimpleName().toString();
        String packageClasse = recupererNomPackage(classeDeLaMethodeAnnotee);
        String nomClasseGeneree = nomClasseMere + typeDeClasse;
        texteAGenerer.append("package " + packageClasse + ";\n\n");
                

        if(typeDeClasse.equals("Logged")){
            texteAGenerer.append("import java.util.logging.Logger"+ ";\n")
                .append("import java.util.logging.Level"+ ";\n")
                .append("import java.util.logging.ConsoleHandler"+ ";\n\n")
                .append("public class "+ nomClasseGeneree + " extends " + nomClasseMere + " {\n" );
        }else{
            texteAGenerer.append("public class "+ nomClasseGeneree + " {\n" );
        }
    }

    private String recupererNomPackage(TypeElement classeDeLaMethodeAnnotee) {
        PackageElement packageElement = (PackageElement) (classeDeLaMethodeAnnotee.getEnclosingElement());
        return packageElement.getQualifiedName().toString();
    }
}
