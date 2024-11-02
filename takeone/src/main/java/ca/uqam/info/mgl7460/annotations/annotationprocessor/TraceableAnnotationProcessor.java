package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@SupportedAnnotationTypes("ca.uqam.info.mgl7460.annotations.annotation.Traceable")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
@AutoService(Processor.class)
public class TraceableAnnotationProcessor  extends AbstractProcessor   {

    private static final String TRACEABLE_ANNOTATION = "ca.uqam.info.mgl7460.annotations.annotation.Traceable";
    private HeaderClassWritter headerWritter = new HeaderClassWritter();
    private AttributLoggerWritter attributLoggerWritter = new AttributLoggerWritter();
    private SubClasseGenartor subClassGenerator = new SubClasseGenartor();
    private ConstructorWritter constructorWritter = new ConstructorWritter();
    private  MethodWritter methodWritter = new MethodWritter();

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
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
                        headerWritter.ecrireEnteteClasse(classeDeLaMethodeAnnotee,texteAGenerer);
                        attributLoggerWritter.ecrireAttributLogger(classeDeLaMethodeAnnotee,texteAGenerer);


                        // Obtenir les membres de la classe
                        List<? extends Element> elementsMembres = classeDeLaMethodeAnnotee.getEnclosedElements();

                        // Isoler les méthodes membres dans une liste
                        List<Element> membresMethods = filterElementsParGenre(elementsMembres,ElementKind.METHOD);

                        // Isoler les constructeurs membres dans une liste
                        List<Element> membresConstructeurs = filterElementsParGenre(elementsMembres,ElementKind.CONSTRUCTOR);

                        for (Element Element : membresConstructeurs) {
                            constructorWritter.ecrireConstructeurCourant((ExecutableElement)Element,texteAGenerer);
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println("Membre : " + Element.getSimpleName().toString());
                        }

                        // Afficher les membres
                        for (Element Element : membresMethods) {
                            methodWritter.ecrireMethodeCourante((ExecutableElement)Element,texteAGenerer);
                        }
                        texteAGenerer.append("}");
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                            System.out.println(texteAGenerer);

                        //Afficher le nom de la classe encapsulante
                            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println(nomDeclassDeElementAnnote.toString());

                        subClassGenerator.genererSousClasse(texteAGenerer,nomDeclassDeElementAnnote.toString(),processingEnv.getFiler());

                    }
                }
            }
        }

        return true;
    }

    private List<Element> filterElementsParGenre(List<? extends javax.lang.model.element.Element> elementsMembres, ElementKind genre){
        return elementsMembres.stream().filter(e -> e.getKind() == genre).collect(Collectors.toList());
    }



















































//    private void genererSousClasse(StringBuilder texteAGenerer, String nomClasseMere,Filer filer)  {
//        try {
//            JavaFileObject file = filer.createSourceFile("generatedClasses." +nomClasseMere+"Logged");
//
//            // Écrire le contenu dans le fichier
//            try (Writer writer = file.openWriter()) {
//                writer.write(texteAGenerer.toString());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }



//    private void ecrireEnteteClasse(TypeElement classeDeLaMethodeAnnotee, StringBuilder texteAGenerer){
//        String nomClasseMere = classeDeLaMethodeAnnotee.getSimpleName().toString();
//        String packageClasse = recupererNomPackage(classeDeLaMethodeAnnotee);
//        String nomClasseGeneree = nomClasseMere + "Logged";
//        texteAGenerer.append("package " + packageClasse + ";\n\n")
//                .append("import java.util.logging.Logger"+ ";\n")
//                .append("import java.util.logging.Level"+ ";\n")
//                .append("import java.util.logging.ConsoleHandler"+ ";\n\n")
//                .append("public class "+ nomClasseGeneree + " extends " + nomClasseMere+ "{\n" );
//    }
//    private void ecrireAttributLogger(TypeElement classeDeLaMethodeAnnotee, StringBuilder texteAGenerer) {
//        texteAGenerer.append("\tprivate static Logger logger = null;\n")
//                .append("\tstatic{\n\t\tlogger = logger.getLogger(\"" +classeDeLaMethodeAnnotee.getQualifiedName() + "\");\n" )
//                .append("\t\tlogger.addHandler(new ConsoleHandler());\n\t}\n");
//    }

//    private void ecrireConstructeurCourant(ExecutableElement constructeur, StringBuilder texteAGenerer) {
//        ecrireEnteteConstructeur(constructeur,texteAGenerer );
//        ecrireContenuConstructeur(constructeur,texteAGenerer);
//
//    }
//
//    private void ecrireEnteteConstructeur(ExecutableElement constructeur, StringBuilder texteAGenerer) {
//        ecrireModifieurs(constructeur.getModifiers(),texteAGenerer);
//        ecrireNomEtParam(constructeur,constructeur.getParameters(),texteAGenerer);
//    }



//    private void ecrireMethodeCourante(ExecutableElement methode, StringBuilder texteAGenerer) {
//        if (verifierPresenceAnnotationTraceable(methode)) {
//            redefinirMethodeAnnotee(methode, texteAGenerer);
//        } else {
//            redefinirMethodeNonAnnotee(methode, texteAGenerer);
//        }
//    }

//    private void redefinirMethodeNonAnnotee(ExecutableElement methode, StringBuilder texteAGenerer) {
//        ecrireEnteteMethode(methode,texteAGenerer);
//        ecrireContenuMethodeNonAnnotee(methode,texteAGenerer);
//    }
//
//
//
//    private void redefinirMethodeAnnotee(ExecutableElement methode, StringBuilder texteAGenerer) {
//        ecrireEnteteAnnotation(texteAGenerer);
//        ecrireEnteteMethode(methode,texteAGenerer);
//        ecrireContenuMethodeAnnotee(methode,texteAGenerer);
//    }

//    private void ecrireContenuMethodeAnnotee(ExecutableElement methode, StringBuilder texteAGenerer) {
//        ecrireContenuAdditionnel(methode,texteAGenerer);
//        ecrireContenuMethodeNonAnnotee(methode,texteAGenerer);
//    }

//    private void ecrireContenuAdditionnel(ExecutableElement methode, StringBuilder texteAGenerer) {
//        Traceable annotation = methode.getAnnotation(Traceable.class);
//        String levelAnnote = annotation.level();
//        texteAGenerer.append("\t\tlogger.log(Level." + levelAnnote + ", \"Call to ")
//                .append(methode.getSimpleName().toString()).append("(");
//        List<? extends VariableElement> parameters = methode.getParameters();
//        for (VariableElement parameter : parameters) {
//            String param = parameter.getSimpleName().toString();
//            texteAGenerer.append(param +": \" + "+ param + " + \")\"").append(", ");
//        }
//        texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
//        texteAGenerer.append(");\n");
//    }

//    private void ecrireContenuMethodeNonAnnotee(ExecutableElement elementExecutable, StringBuilder texteAGenerer) {
//        texteAGenerer.append("\t\tsuper(");
//
//        List<? extends VariableElement> parameters = elementExecutable.getParameters();
//        for (VariableElement parameter : parameters) {
//            texteAGenerer.append(parameter.getSimpleName().toString()).append(", ");
//        }
//        if(parameters.size()>0)
//            texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
//        texteAGenerer.append(");\n\t}\n");
//    }

//    private void ecrireContenuConstructeur(ExecutableElement elementExecutable, StringBuilder texteAGenerer) {
//        texteAGenerer.append("\t\tsuper");
//        texteAGenerer.append("." + elementExecutable.getSimpleName().toString() + "(");
//
//        List<? extends VariableElement> parameters = elementExecutable.getParameters();
//        for (VariableElement parameter : parameters) {
//            texteAGenerer.append(parameter.getSimpleName().toString()).append(", ");
//        }
//        if(parameters.size()>0)
//            texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
//        texteAGenerer.append(");\n\t}\n");
//    }
//    private void ecrireEnteteMethode(ExecutableElement methode, StringBuilder texteAGenerer) {
//        ecrireModifieurs(methode.getModifiers(),texteAGenerer);
//        ecrireTypeDeRetour(methode.getReturnType(), texteAGenerer);
//        ecrireNomEtParam(methode,methode.getParameters(),texteAGenerer);
//    }

//    private void ecrireNomEtParam(ExecutableElement elementExecutable, List<? extends VariableElement> parameters, StringBuilder texteAGenerer) {
//        if((elementExecutable.getSimpleName().toString()).equals("<init>")) {
//           Element elementClass = elementExecutable.getEnclosingElement();
//            texteAGenerer.append(elementClass.getSimpleName().toString())
//                    .append("Logged");
//        }else {
//            texteAGenerer.append(elementExecutable.getSimpleName().toString());
//        }
//        texteAGenerer.append("(");
//        if (parameters.size() > 0) {
//            for (VariableElement param : parameters) {
//                texteAGenerer.append(param.asType().toString()).append(" ").append(param.getSimpleName().toString()).append(", ");
//            }
//            texteAGenerer.delete(texteAGenerer.length() - 2, texteAGenerer.length());
//            texteAGenerer.append(") {\n");
//        }else{
//            texteAGenerer.append(") {\n");
//
//        }
//    }

//    private void ecrireTypeDeRetour(TypeMirror typeDeRetour, StringBuilder texteAGenerer) {
//        texteAGenerer.append(typeDeRetour.toString()).append(" ");
//    }

//    private void ecrireModifieurs(Set<Modifier> modifiers, StringBuilder texteAGenerer) {
//        for (Modifier mod : modifiers) {
//            texteAGenerer.append("\t").append(mod.toString()).append(" ");
//        }
//    }

//    private void ecrireEnteteAnnotation(StringBuilder texteAGenerer) {
//        texteAGenerer.append("\t@Override\n");
//    }

//    private String recupererNomPackage(TypeElement classeDeLaMethodeAnnotee) {
//        PackageElement packageElement = (PackageElement) (classeDeLaMethodeAnnotee.getEnclosingElement());
//            return packageElement.getQualifiedName().toString();
//    }
//    private boolean verifierPresenceAnnotationTraceable(ExecutableElement methode){
//        return methode.getAnnotation(Traceable.class) != null ;
//    }


}


