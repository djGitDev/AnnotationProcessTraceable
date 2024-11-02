package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public class SubClasseGenartor {

    public void genererSousClasse(StringBuilder texteAGenerer, String nomClasseMere, Filer filer)  {
        try {
            JavaFileObject file = filer.createSourceFile("generatedClasses." +nomClasseMere+"Logged");

            // Écrire le contenu dans le fichier
            try (Writer writer = file.openWriter()) {
                writer.write(texteAGenerer.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
