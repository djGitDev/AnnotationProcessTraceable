package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import ca.uqam.info.mgl7460.annotations.annotation.Traceable;
import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
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
                        ecrireAttributLogger(classeDeLaMethodeAnnotee,texteAGenerer);


                        // Obtenir les membres de la classe
                        List<? extends Element> elementsMembres = classeDeLaMethodeAnnotee.getEnclosedElements();

                        // Isoler les méthodes membres dans une liste
                        List<Element> membresMethods = filterElementsParGenre(elementsMembres,ElementKind.METHOD);

                        // Isoler les constructeurs membres dans une liste
                        List<Element> membresConstructeurs = filterElementsParGenre(elementsMembres,ElementKind.CONSTRUCTOR);

                        for (Element Element : membresConstructeurs) {
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println("Membre : " + Element.getSimpleName().toString());
                        }

                        // Afficher les membres
                        for (Element Element : membresMethods) {
                            ecrireMethodeCourante((ExecutableElement)Element,texteAGenerer);
                        }

                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println(texteAGenerer);

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
    private void ecrireAttributLogger(TypeElement classeDeLaMethodeAnnotee, StringBuilder texteAGenerer) {
        texteAGenerer.append("\tprivate static Logger logger = null;\n")
                .append("\tstatic{\n\t\tlogger = logger.getLogger(\"" +classeDeLaMethodeAnnotee.getQualifiedName() + "\");\n" )
                .append("\t\tlogger.addHandler(new ConsoleHandler());\n\t}\n");
    }

    private void ecrireMethodeCourante(ExecutableElement methode, StringBuilder texteAGenerer) {
        // Ajouter les annotations avant les modificateurs et le nom de la méthode
        if (verifierPresenceAnnotationTraceable(methode)) {
            redefinirMethodeAnnotee(methode, texteAGenerer);
        } else {
            redefinirMethodeNonAnnotee(methode, texteAGenerer);
        }
    }

    private void redefinirMethodeNonAnnotee(ExecutableElement methode, StringBuilder texteAGenerer) {
        ecrireEnteteMethode(methode,texteAGenerer);
        ecrireContenuMethodeNonAnnotee(methode,texteAGenerer);
    }



    private void redefinirMethodeAnnotee(ExecutableElement methode, StringBuilder texteAGenerer) {
        ecrireEnteteAnnotation(texteAGenerer);
        ecrireEnteteMethode(methode,texteAGenerer);
        ecrireContenuMethodeAnnotee(methode,texteAGenerer);
    }

    private void ecrireContenuMethodeAnnotee(ExecutableElement methode, StringBuilder texteAGenerer) {
        ecrireContenuAdditionnel(methode,texteAGenerer);
        ecrireContenuMethodeNonAnnotee(methode,texteAGenerer);
    }

    private void ecrireContenuAdditionnel(ExecutableElement methode, StringBuilder texteAGenerer) {
        Traceable annotation = methode.getAnnotation(Traceable.class);
        String levelAnnote = annotation.level();
        texteAGenerer.append("\t\tlogger.log(Level." + levelAnnote + ", \"Call to ")
                .append(methode.getSimpleName().toString()).append("(");
        List<? extends VariableElement> parameters = methode.getParameters();
        for (VariableElement parameter : parameters) {
            String param = parameter.getSimpleName().toString();
            texteAGenerer.append(param +": \" + "+ param + " + \")\"").append(", ");
        }
        texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
        texteAGenerer.append(");\n");
    }

    private void ecrireContenuMethodeNonAnnotee(ExecutableElement methode, StringBuilder texteAGenerer) {
        texteAGenerer.append("\t\tsuper.")
                .append(methode.getSimpleName().toString())
                .append("(");
        List<? extends VariableElement> parameters = methode.getParameters();
        for (VariableElement parameter : parameters) {
            texteAGenerer.append(parameter.getSimpleName().toString()).append(", ");
        }
        texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
        texteAGenerer.append(");\n\t}");
    }
    private void ecrireEnteteMethode(ExecutableElement methode, StringBuilder texteAGenerer) {
        Set<Modifier> modifiers = methode.getModifiers();
        for (Modifier mod : modifiers) {
            texteAGenerer.append("\t").append(mod.toString()).append(" ");
        }
        TypeMirror typeDeRetour = methode.getReturnType();
        texteAGenerer.append(typeDeRetour.toString()).append(" ");
        Name nomMethode = methode.getSimpleName();
        texteAGenerer.append(nomMethode.toString());
        List<? extends VariableElement> parameters = methode.getParameters();
        texteAGenerer.append("(");
        for (VariableElement param : parameters) {
            texteAGenerer.append(param.asType().toString()).append(" ").append(param.getSimpleName().toString()).append(", ");
        }
        texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
        texteAGenerer.append(") {\n");
    }

    private void ecrireEnteteAnnotation(StringBuilder texteAGenerer) {
        texteAGenerer.append("\t@Override\n");
    }

    private String recupererNomPackage(TypeElement classeDeLaMethodeAnnotee) {
        PackageElement packageElement = (PackageElement) (classeDeLaMethodeAnnotee.getEnclosingElement());
            return packageElement.getQualifiedName().toString();
    }
    private boolean verifierPresenceAnnotationTraceable(ExecutableElement methode){
        return methode.getAnnotation(Traceable.class) != null ;
    }



}


