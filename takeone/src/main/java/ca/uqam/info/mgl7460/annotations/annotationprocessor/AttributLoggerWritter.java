package ca.uqam.info.mgl7460.annotations.annotationprocessor;

import javax.lang.model.element.TypeElement;

public class AttributLoggerWritter {

    public void ecrireAttributLogger(TypeElement classeDeLaMethodeAnnotee, StringBuilder texteAGenerer) {
        texteAGenerer.append("\tprivate static Logger logger = null;\n")
                .append("\tstatic {\n\t\tlogger = logger.getLogger(\"" +classeDeLaMethodeAnnotee.getQualifiedName() + "\");\n" )
                .append("\t\tlogger.addHandler(new ConsoleHandler());\n\t    }\n\n");
    }


}
