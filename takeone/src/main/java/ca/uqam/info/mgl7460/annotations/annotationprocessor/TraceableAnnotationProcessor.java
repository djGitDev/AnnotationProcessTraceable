package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import ca.uqam.info.mgl7460.annotations.annotation.Traceable;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@SupportedAnnotationTypes("ca.uqam.info.mgl7460.annotations.annotation.Traceable")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class TraceableAnnotationProcessor  extends AbstractProcessor   {

    private static final String TRACEABLE_ANNOTATION = "ca.uqam.info.mgl7460.annotations.annotation.Traceable";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        // demo de generation de fichier a supprimer apres
//        try {
//            // Créer un fichier nommé "GeneratedHello.java"
//            JavaFileObject file = processingEnv.getFiler().createSourceFile("generatedClasses.GeneratedHello");
//
//            // Écrire le contenu dans le fichier
//            try (Writer writer = file.openWriter()) {
//                writer.write("package generatedClasses;\n");
//                writer.write("\n");
//                writer.write("public class GeneratedHello {\n");
//                writer.write("    public static void sayHello() {\n");
//                writer.write("        System.out.println(\"Hello from GeneratedHello!\");\n");
//                writer.write("    }\n");
//                writer.write("}\n");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //Parcourir tous les éléments annotations trouvés par le compilateur sur un round
        for (TypeElement annotation : annotations) {

            //Récupérer le nom de l'élément sélectionné de la liste
            Name annotationName = annotation.getQualifiedName();
            // Vérifie si l'annotation courante est de type Traceable
            if (annotationName.toString().equals(TRACEABLE_ANNOTATION)) {

                // Récupère tous les éléments annotés avec @Traceable
                Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);

                // Parcourir et traiter chaque element
                for (Element element : elements) {
                    //Confirmer si l'annotation est sur une methode
                    switch (element.getKind()) {
                        case METHOD:
                        // Récupérer l'élément encapsulant de la methode qui est une classe
                        TypeElement classeDeLaMethodeAnnotee = (TypeElement) element.getEnclosingElement();
                        Name nomDeclassDeElementAnnote = classeDeLaMethodeAnnotee.getQualifiedName();

                        StringBuilder texteAGenerer = new StringBuilder();
                        ecrireEnteteClasse(classeDeLaMethodeAnnotee,texteAGenerer);
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println(texteAGenerer);

                        // Obtenir les membres de la classe
                        List<? extends Element> elementsMembres = classeDeLaMethodeAnnotee.getEnclosedElements();

                        // Isoler les méthodes membres dans une liste
                        List<Element> membresMethods = filterElementsParGenre(elementsMembres,ElementKind.METHOD);

                        // Isoler les constructeurs membres dans une liste
                        List<Element> membresConstructeurs = filterElementsParGenre(elementsMembres,ElementKind.CONSTRUCTOR);


                        // Afficher les membres
                        for (Element Element : membresMethods) {
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println("Membre : " + Element.getSimpleName().toString());
                        }
                        for (Element Element : membresConstructeurs) {
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println("Membre : " + Element.getSimpleName().toString());
                        }
                        //Afficher le nom de la classe encapsulante
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println(nomDeclassDeElementAnnote.toString());

                    }
                }
            }
        }

        return true;
    }

    private List<Element> filterElementsParGenre(List<? extends javax.lang.model.element.Element> elementsMembres, ElementKind genre){
        return elementsMembres.stream().filter(e -> e.getKind() == genre).collect(Collectors.toList());
    }

    private void ecrireEnteteClasse(TypeElement classeDeLaMethodeAnnotee, StringBuilder texteAGenerer){
        String nomClasseMere = classeDeLaMethodeAnnotee.getSimpleName().toString();
        String packageClasse = recupererNomPackage(classeDeLaMethodeAnnotee);
        String nomClasseGeneree = nomClasseMere + "Logged";
        texteAGenerer.append("package " + packageClasse + ";\n")
                .append("import java.util.logging.Logger"+ ";\n")
                .append("import java.util.logging.Level"+ ";\n")
                .append("import java.util.logging.ConsoleHandler"+ ";\n")
                .append("public class "+ nomClasseGeneree + " extends " + nomClasseMere+ "{\n" );
    }

    private String recupererNomPackage(TypeElement classeDeLaMethodeAnnotee) {
        PackageElement packageElement = (PackageElement) (classeDeLaMethodeAnnotee.getEnclosingElement());
            return packageElement.getQualifiedName().toString();
    }



}


