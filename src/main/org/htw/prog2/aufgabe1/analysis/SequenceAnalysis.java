package org.htw.prog2.aufgabe1.analysis;

import org.htw.prog2.aufgabe1.files.MutationFile;
import org.htw.prog2.aufgabe1.files.SequenceFile;

import java.util.HashMap;


// Abstrakte Basisklasse für die Analyse von Patientensequenzen auf Medikamentenresistenzen.
// Einmal gesetzt und von der Unterklasse benutzt

public abstract class SequenceAnalysis {
    protected String reference;           // die normale HIV-Sequenz (99 Zeichen)
    protected SequenceFile sequences;     // die 1000 Patientensequenzen
    protected MutationFile mutations;     // die 825 bekannten Mutationen
    protected HashMap<String, Double> resistances;  //Ergebnis: Medikament → Resistenzwert

    // der Konstruktor --> nimmt die drei Eingaben entgegen und speichert sie
    public SequenceAnalysis(String reference, SequenceFile sequences, MutationFile mutations) {
        this.reference = reference;
        this.sequences = sequences;
        this.mutations = mutations;
        resistances = new HashMap<>();
        // Alle Medikamente mit 0.0 initialisieren, da noch keine Resistenz bekannt ist
        for(String drug : mutations.getDrugs()) {
            resistances.put(drug, 0.0);
        }
        // eigentliche Berechnung die die Unterklasse implementiert
        calculateResistances();
    }


     // Berechnet die Resistenzen für alle Patientensequenzen.
     // Wird automatisch im Konstruktor aufgerufen.
    // FullLengthSequenceAnalysis() erbt

    public abstract void calculateResistances();


     //Gibt die berechneten Resistenzwerte pro Medikament zurück.
     // Map: Medikamentenname -> maximaler Resistenzwert

    public HashMap<String, Double> getResistances() {
        return resistances;
    }


     //Gibt das Medikament mit der niedrigsten Wert (Resistenz) zurück (= am besten wirksam).
     // Gibt den Namen des besten Medikaments
    //Double.MAX_VALUE ist der größtmögliche Double-Wert als Startwert,
    // damit ist garantiert dass der erste echte Wert immer kleiner ist

    public String getBestDrug() {
        String best = "";
        double bestValue = Double.MAX_VALUE;
        for(String drug : resistances.keySet()) {
            if(resistances.get(drug) < bestValue) {
                bestValue = resistances.get(drug);
                best = drug;
            }
        }
        return best;
    }


     //Gibt den Resistenzwert des besten (am wenigsten resistenten) Medikaments zurück.
     // @return Niedrigster Resistenzwert
      //Der ?-Operator ist eine Kurzform für if-else: falls noch kein Wert gefunden wurde (HashMap leer),
     //gib 0.0 zurück, sonst den gefundenen Wert
      //Ergebnis: 1.88

    public double getBestDrugResistance() {
        double bestValue = Double.MAX_VALUE;
        for(double value : resistances.values()) {
            if(value < bestValue) {
                bestValue = value;
            }
        }
        return bestValue == Double.MAX_VALUE ? 0.0 : bestValue;
    }


     //Gibt alle Medikamente mit ihren Resistenzwerten als formatierten String zurück(eine Zeile pro Medikament)
     // @return Formatierter String mit allen Medikamenten und Resistenzwerten
    // Benutzt mutations.getDrugs() statt resistances.keySet() damit die Reihenfolge der CSV-Datei erhalten bleibt
    public String getDrugDescriptions() {
        StringBuilder sb = new StringBuilder();
        for(String drug : mutations.getDrugs()) {
            sb.append(drug).append(": ").append(resistances.get(drug)).append("\n");
        }
        return sb.toString().strip();
    }
}

//Diese Klasse braucht:

//SequenceFile --> für das sequences-Feld
//MutationFile --> für das mutations-Feld und getDrugs()
//HashMap aus java.util --> für resistances

//Diese Klasse wird gebraucht von:

//FullLengthSequenceAnalysis extends SequenceAnalysis --> erbt alles, implementiert calculateResistances()
//SequenceAnalysisManager.performAnalysis() --> gibt SequenceAnalysis als Rückgabetyp zurück
//HIVDiagnosticsCLI --> ruft getDrugDescriptions() und getBestDrug() auf
//HIVDiagnosticsGUI --> ruft getBestDrug() und getBestDrugResistance() auf