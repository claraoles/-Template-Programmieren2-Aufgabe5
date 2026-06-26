package org.htw.prog2.aufgabe1.analysis;

import org.htw.prog2.aufgabe1.files.Mutation;
import org.htw.prog2.aufgabe1.files.MutationFile;
import org.htw.prog2.aufgabe1.files.SequenceFile;


 // Analysiert Patientensequenzen auf Medikamentenresistenzen durch Vergleich
 //der vollständigen Sequenz mit bekannten Mutationsmustern
// geht jede bekannte Mutation durch und schaut ob der Patient diese Mutation hat

public class FullLengthSequenceAnalysis extends SequenceAnalysis {

    public FullLengthSequenceAnalysis(String reference, SequenceFile sequences, MutationFile mutations) {
        super(reference, sequences, mutations);
    }

    @Override
    public void calculateResistances() {
        //Geh durch alle 825 bekannten Mutationen eine nach der anderen
        for(Mutation mutation : mutations.getMutations()) {
            // Berechne wie die HIV-Sequenz aussehen würde WENN diese Mutation vorhanden wäre.
            // Mutierte Sequenz aus Referenz + Mutationsmuster berechnen
            String mutatedSequence = mutation.getSequence(reference);

            // Prüfen ob irgendeine Patientensequenz dieser Mutation entspricht
            // Ist sie dabei?
            if(sequences.containsSequence(mutatedSequence)) {
                // Falls ja: Resistenzwert pro Medikament mit bisherigem Maximum vergleichen
                // Falls ja: Für jedes Medikament prüfe ob dieser Resistenzwert schlechter ist als der bisher bekannte
                // Immer nur das Maximum behalten
                // Warum Maximum ? --> Weil es viele Mutationen geben kann
                // wir wollen die schlimmste Resistenz wissen.
                for(String drug : mutation.getResistances().keySet()) {
                    double newValue = mutation.getResistances().get(drug);
                    double currentMax = resistances.getOrDefault(drug, 0.0);
                    if(newValue > currentMax) {
                        resistances.put(drug, newValue);
                    }
                }
            }
        }
    }
}
//macht die eigentliche medizinische Analyse
//Welche Mutationen hat der Patient, und welches Medikament wirkt noch?

//welche Dateien oder Methoden sind davon abhängig?
//Diese Klasse braucht:

//SequenceAnalysis –-> die abstrakte Elternklasse, gibt die Felder mutations, sequences, reference, resistances bereit
//Mutation.getSequence() –-> berechnet die mutierte Sequenz
//Mutation.getResistances() –-> gibt die Resistenzwerte zurück
//SequenceFile.containsSequence() –-> prüft ob Patientensequenz vorkommt
//MutationFile.getMutations() –-> gibt alle 825 Mutationen zurück

// Diese Klasse wird gebraucht von:
//SequenceAnalysisManager.performAnalysis() –-> erstellt das Objekt und gibt es zurück
//HIVDiagnosticsCLI –-> ruft getBestDrug() und getDrugDescriptions() auf dem Ergebnis auf
//HIVDiagnosticsGUI –-> ruft getBestDrug() und getBestDrugResistance() auf