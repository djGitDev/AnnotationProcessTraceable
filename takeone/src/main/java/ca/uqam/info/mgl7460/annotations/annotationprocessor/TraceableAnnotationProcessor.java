package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.HashSet;


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
        
        Set<String> processedClasses = new HashSet<>();

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
                    TypeElement classeDeLaMethodeAnnotee = (TypeElement) element.getEnclosingElement();
                    Name nomDeclassDeElementAnnote = classeDeLaMethodeAnnotee.getQualifiedName();

                    if (!processedClasses.contains(nomDeclassDeElementAnnote.toString())) {
                        processedClasses.add(nomDeclassDeElementAnnote.toString());
                        switch (element.getKind()) {
                            case METHOD:

                                StringBuilder texteAGenererLogger = new StringBuilder();
                                StringBuilder texteAGenererFactory = new StringBuilder();

                                headerWritter.ecrireEnteteClasse(classeDeLaMethodeAnnotee,texteAGenererLogger,"Logged");
                                headerWritter.ecrireEnteteClasse(classeDeLaMethodeAnnotee,texteAGenererFactory,"Factory");
                                attributLoggerWritter.ecrireAttributLogger(classeDeLaMethodeAnnotee,texteAGenererLogger);


                                // Obtenir les membres de la classe
                                List<? extends Element> elementsMembres = classeDeLaMethodeAnnotee.getEnclosedElements();

                                // Isoler les méthodes membres dans une liste
                                List<Element> membresMethods = filterElementsParGenre(elementsMembres,ElementKind.METHOD);

                                // Isoler les constructeurs membres dans une liste
                                List<Element> membresConstructeurs = filterElementsParGenre(elementsMembres,ElementKind.CONSTRUCTOR);

                                for (Element Element : membresConstructeurs) {
                                    constructorWritter.ecrireConstructeurCourant((ExecutableElement)Element,texteAGenererLogger,"Logged");
                                    constructorWritter.ecrireConstructeurCourant((ExecutableElement)Element,texteAGenererFactory,"Factory");
                                }

                                // Afficher les membres
                                for (Element Element : membresMethods) {
                                    methodWritter.ecrireMethodeCourante((ExecutableElement)Element,texteAGenererLogger,"Logged");
                                }
                                texteAGenererLogger.append("}");
                                texteAGenererFactory.append("}");
                                
                                subClassGenerator.genererSousClasse(texteAGenererLogger,nomDeclassDeElementAnnote.toString(),processingEnv.getFiler(),"Logged");
                                subClassGenerator.genererSousClasse(texteAGenererFactory,nomDeclassDeElementAnnote.toString(),processingEnv.getFiler(),"Factory");
                               
                        }
                    }  
                }
            }
        }

        return true;
    }

    private List<Element> filterElementsParGenre(List<? extends javax.lang.model.element.Element> elementsMembres, ElementKind genre){
        return elementsMembres.stream().filter(e -> e.getKind() == genre).collect(Collectors.toList());
    }
}


