package org.htw.prog2.aufgabe1.files;

import java.util.HashMap;


 //Repräsentiert ein einzelnes Mutationsmuster mit seinen Auswirkungen auf die Wirksamkeit
 //verschiedener Medikamente

 //variant ist das Mutationsmuster als Text
 // resistances ist eine Map: Medikamentenname → Resistenzwert
 // nicht mehr änderbar
public class Mutation {
    private final String variant;
    private final HashMap<String, Double> resistances;


     //Variantendefinition, z.B. "9M"
     // Map: Medikamentenname -> Veränderung der Wirksamkeit (foldn-Wert)
    // nimmt die Werte entgegen und speichert sie
    public Mutation(String variant, HashMap<String, Double> resistances) {
        this.variant = variant;
        this.resistances = resistances;
    }

    public String getVariant() {
        return variant;
    }

    //getResistances() wird in FullLengthSequenceAnalysis benutzt um die Resistenzwerte abzurufen
    // wenn eine Mutation beim Patienten gefunden wurde
    public HashMap<String, Double> getResistances() {
        return resistances;
    }


      //Wendet die Mutation auf eine Referenzsequenz an und gibt die veränderte Sequenz zurück.
     //Das Varianten-Format ist z.B. "46I,54V,82A,90M": Zahl = Position (1-basiert),
     //Buchstabe = Aminosäure, durch die ersetzt wird.

    public String getSequence(String reference) {
        //Die Referenzsequenz wird in ein Array von einzelnen Zeichen umgewandelt
        char[] seq = reference.toCharArray();
        //Der Variantenstring wird an Kommas aufgeteilt
        for(String singleVariant : variant.split(",")) {
            // Letztes Zeichen ist die neue Aminosäure, alles davor ist die Position
            char aminoAcid = singleVariant.charAt(singleVariant.length() - 1);
            int position = Integer.parseInt(singleVariant.substring(0, singleVariant.length() - 1));
            // Position ist 1-basiert, Array-Index ist 0-basiert
            seq[position - 1] = aminoAcid;
        }
        return new String(seq);
    }
}

// Jede der 825 Zeilen aus der CSV-Datei wird zu einem Mutation-Objekt.
// Diese werden in MutationFile gesammelt und später einzeln von FullLengthSequenceAnalysis verarbeitet.

//Diese Klasse braucht:
//HashMap aus java.util --> für die Resistenzwerte
//Sonst nichts, sie ist bewusst einfach gehalten

//Diese Klasse wird gebraucht von:
// MutationFile.addMutation(Mutation variant) --> speichert Mutation-Objekte in einer Liste
// MutationFile.getMutations() --> gibt Liste von Mutation-Objekten zurück
// CSVFileReader.readFile()--> erstellt für jede CSV-Zeile ein neues Mutation-Objekt
// FullLengthSequenceAnalysis.calculateResistances() --> ruft getSequence() und getResistances() auf
// MutationTest --> testet direkt getSequence()

