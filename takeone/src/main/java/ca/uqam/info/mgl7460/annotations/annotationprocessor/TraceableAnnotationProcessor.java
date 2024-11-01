package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import ca.uqam.info.mgl7460.annotations.annotation.Traceable;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;


@SupportedAnnotationTypes("ca.uqam.info.mgl7460.annotations.annotation.Traceable")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class TraceableAnnotationProcessor  extends AbstractProcessor   {



    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {


        try {
            // Créer un fichier nommé "GeneratedHello.java"
            JavaFileObject file = processingEnv.getFiler().createSourceFile("generatedClasses.GeneratedHello");

            // Écrire le contenu dans le fichier
            try (Writer writer = file.openWriter()) {
                writer.write("package generatedClasses;\n");
                writer.write("\n");
                writer.write("public class GeneratedHello {\n");
                writer.write("    public static void sayHello() {\n");
                writer.write("        System.out.println(\"Hello from GeneratedHello!\");\n");
                writer.write("    }\n");
                writer.write("}\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }







}
