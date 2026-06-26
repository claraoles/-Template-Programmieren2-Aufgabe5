package org.htw.prog2.aufgabe1.ui;

import org.apache.commons.cli.*;
import org.htw.prog2.aufgabe1.analysis.SequenceAnalysis;
import org.htw.prog2.aufgabe1.analysis.SequenceAnalysisManager;


  //Kommandozeilen-Interface für die HIV-Diagnostik.

public class HIVDiagnosticsCLI {

    public HIVDiagnosticsCLI(String[] args) {
        CommandLine cli = parseOptions(args);
        if(cli == null) {
            System.exit(1);
        }
        try {
            SequenceAnalysis analysis = SequenceAnalysisManager.performAnalysis(
                    cli.getOptionValue('r'),
                    cli.getOptionValue('p'),
                    cli.getOptionValue('m')
            );
            System.out.println("Vorhersage der Medikamentenresistenzen:");
            System.out.println(analysis.getDrugDescriptions());
            System.out.println("Empfohlenes Medikament: " + analysis.getBestDrug());
        } catch(Exception e) {
            System.out.println("Fehler beim Einlesen einer der Dateien: " + e.getMessage());
        }
    }


     //Parst die Kommandozeilenargumente. Gibt null zurück falls ein Fehler auftritt
     //oder ein erforderliches Argument fehlt.

    public static CommandLine parseOptions(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("g")
                .hasArg(false)
                .longOpt("graphical")
                .desc("Start with graphical interface").build());
        options.addOption(Option.builder("m")
                .hasArg(true)
                .longOpt("mutationfile")
                .required(true)
                .desc("CSV file with mutation patterns.").build());
        options.addOption(Option.builder("d")
                .hasArg(true)
                .longOpt("drugname")
                .required(true)
                .desc("Drug name.").build());
        options.addOption(Option.builder("r")
                .hasArg(true)
                .longOpt("reference")
                .required(true)
                .desc("Reference sequence FASTA file.").build());
        options.addOption(Option.builder("p")
                .hasArg(true)
                .longOpt("patientseqs")
                .required(true)
                .desc("FASTA file with patient sequences.").build());
        CommandLineParser parser = new DefaultParser();
        try {
            return parser.parse(options, args);
        } catch(ParseException e) {
            System.out.println("Error: " + e.getMessage());
            new HelpFormatter().printHelp("HIVDiagnostics", options);
            return null;
        }
    }
}

//Diese Klasse braucht:

//SequenceAnalysisManager.performAnalysis() –-> startet die Analyse
//SequenceAnalysis –-> Rückgabetyp von performAnalysis()
//Apache Commons CLI Bibliothek – Options, Option, CommandLine, DefaultParser, HelpFormatter, ParseException

//Diese Klasse wird gebraucht von:

//HIVDiagnostics.main() – erstellt new HIVDiagnosticsCLI(args) wenn Argumente vorhanden
//HIVDiagnosticsCLITest – testet direkt parseOptions() mit verschiedenen Argumentkombinationen
// im Terminal eingeben: -m datei.csv -r ref.fasta -p pat.fasta -d DrugName