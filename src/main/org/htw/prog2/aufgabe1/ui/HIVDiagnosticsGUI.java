package org.htw.prog2.aufgabe1.ui;

import org.htw.prog2.aufgabe1.analysis.SequenceAnalysis;
import org.htw.prog2.aufgabe1.analysis.SequenceAnalysisManager;
import org.htw.prog2.aufgabe1.exceptions.FileFormatException;
import org.htw.prog2.aufgabe1.exceptions.NoValidReadersException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class HIVDiagnosticsGUI extends JFrame {

    // Gespeicherte Dateipfade
    //Drei Variablen die die Dateipfade speichern sobald der Nutzer sie auswählt.
    // Am Anfang alle null --> noch keine Datei geladen.
    private String mutationFile = null;
    private String referenceFile = null;
    private String patientFile = null;

    // Labels die den Dateinamen (oder Platzhalter) anzeigen
    //Die Labels die man im Fenster sieht –-> zuerst roter Platzhaltertext,
    // später der Dateiname bzw. das Ergebnis.
    private JLabel mutationLabel;
    private JLabel referenceLabel;
    private JLabel patientLabel;

    // Ergebnis-Labels unten
    private JLabel recommendedDrugValue;
    private JLabel resistanceValue;

    // abstrakte innere Klasse für die Lade-Buttons
    protected abstract class LoadListener implements ActionListener {
        private String extensionDescription;
        private String[] extensions;

        public LoadListener(String extensionDescription, String... extensions) {
            this.extensionDescription = extensionDescription;
            this.extensions = extensions;
        }

        @Override
        //Öffnet den Datei-Dialog wenn ein Button geklickt wird.
        // setData() ist abstrakt –-> jeder der drei Buttons implementiert sie anders: einer speichert den Pfad in mutationFile, einer in referenceFile, einer in patientFile.
        public void actionPerformed(ActionEvent actionEvent) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileNameExtensionFilter(extensionDescription, extensions));
            int result = chooser.showOpenDialog(HIVDiagnosticsGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                setData(chooser.getSelectedFile());
            }
        }

        protected abstract void setData(File file);
    }

    public HIVDiagnosticsGUI() {
        super("HIV Diagnostics Tool");
        init();
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        initMenuBar();
        initFileChoosers();

        // "Predict best drug" Button
        //Klick auf den Button: zuerst prüfen ob alle drei Dateien geladen sind.
        // Dann performAnalysis() aufrufen. Ergebnis in die Labels schreiben. Bei Fehler → Fehlerdialog.
        JButton predictButton = new JButton("Predict best drug");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 3;
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5, 5, 5, 5);
        add(predictButton, c);

        predictButton.addActionListener(e -> {
            if (mutationFile == null || referenceFile == null || patientFile == null) {
                JOptionPane.showMessageDialog(this,
                        "Please load all three files first.",
                        "Missing files",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                SequenceAnalysis analysis = SequenceAnalysisManager.performAnalysis(
                        referenceFile, patientFile, mutationFile);
                recommendedDrugValue.setText(analysis.getBestDrug());
                recommendedDrugValue.setForeground(Color.BLACK);
                resistanceValue.setText(String.valueOf(analysis.getBestDrugResistance()));
                resistanceValue.setForeground(Color.BLACK);
            } catch (FileFormatException | NoValidReadersException | IOException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error parsing file",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ergebnis-Zeilen
        c = new GridBagConstraints();
        c.insets = new Insets(2, 5, 2, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 4;
        add(new JLabel("Recommended drug:"), c);
        c.gridx = 1; c.gridy = 4;
        c.anchor = GridBagConstraints.EAST;
        recommendedDrugValue = new JLabel("N/A");
        recommendedDrugValue.setForeground(Color.RED);
        add(recommendedDrugValue, c);

        c.gridx = 0; c.gridy = 5;
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Predicted resistance for recommended drug:"), c);
        c.gridx = 1; c.gridy = 5;
        c.anchor = GridBagConstraints.EAST;
        resistanceValue = new JLabel("N/A");
        resistanceValue.setForeground(Color.RED);
        add(resistanceValue, c);

        pack();
        setMinimumSize(new Dimension(500, 200));
        setLocationRelativeTo(null);
    }
    // Erstellt das "File" Menü mit einem "About" Eintrag der einen Info-Dialog öffnet.
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");

        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "HIV-Diagnostik-Tool für Proteomdaten.",
                "Message",
                JOptionPane.INFORMATION_MESSAGE));

        fileMenu.add(aboutItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    //Für jeden der drei Buttons: Label erstellen, Button erstellen, beide ins Fenster einfügen, LoadListener anhängen.
    // Wenn Datei ausgewählt: Pfad speichern, Label-Text auf Dateinamen ändern, Farbe von Rot auf Schwarz.
    private void initFileChoosers() {
        // Mutation CSV
        mutationLabel = new JLabel("Please load a mutation CSV file");
        mutationLabel.setForeground(Color.RED);
        JButton mutationButton = new JButton(new ImageIcon());
        mutationButton.setIcon(UIManager.getIcon("FileView.directoryIcon"));

        addLoaders(mutationLabel, mutationButton, makeConstraints(0), new LoadListener("CSV files", "csv") {
            @Override
            protected void setData(File file) {
                mutationFile = file.getAbsolutePath();
                mutationLabel.setText(file.getName());
                mutationLabel.setForeground(Color.BLACK);
            }
        });

        // Referenz FASTA/FASTQ
        referenceLabel = new JLabel("Please load a reference FASTA/FASTQ file");
        referenceLabel.setForeground(Color.RED);
        JButton referenceButton = new JButton();
        referenceButton.setIcon(UIManager.getIcon("FileView.directoryIcon"));

        addLoaders(referenceLabel, referenceButton, makeConstraints(1), new LoadListener("FASTA/FASTQ files", "fasta", "fastq") {
            @Override
            protected void setData(File file) {
                referenceFile = file.getAbsolutePath();
                referenceLabel.setText(file.getName());
                referenceLabel.setForeground(Color.BLACK);
            }
        });

        // Patienten FASTA/FASTQ
        patientLabel = new JLabel("Please load a patient FASTA/FASTQ file");
        patientLabel.setForeground(Color.RED);
        JButton patientButton = new JButton();
        patientButton.setIcon(UIManager.getIcon("FileView.directoryIcon"));

        addLoaders(patientLabel, patientButton, makeConstraints(2), new LoadListener("FASTA/FASTQ files", "fasta", "fastq") {
            @Override
            protected void setData(File file) {
                patientFile = file.getAbsolutePath();
                patientLabel.setText(file.getName());
                patientLabel.setForeground(Color.BLACK);
            }
        });
    }
    //makeConstraints() erstellt die Positionsangabe für eine Zeile –-> damit nicht dreimal derselbe Code steht.
    private GridBagConstraints makeConstraints(int row) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = row;
        c.insets = new Insets(5, 5, 5, 5);
        return c;
    }
    // addLoaders() fügt Label und Button ins Fenster ein und verbindet den Button mit dem Listener.
    private void addLoaders(JLabel label, JButton button, GridBagConstraints c, ActionListener listener) {
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(label, c);

        c.gridx = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.anchor = GridBagConstraints.EAST;
        add(button, c);

        button.addActionListener(listener);
    }
}

//Diese Klasse braucht:

//JFrame, JLabel, JButton, JFileChooser, JOptionPane, GridBagLayout –-> Java Swing Bibliothek
//SequenceAnalysisManager.performAnalysis() –-> startet die Analyse
//SequenceAnalysis –-> Rückgabetyp mit getBestDrug() und getBestDrugResistance()
//FileFormatException, NoValidReadersException, IOException –-> für Fehlerbehandlung

//Diese Klasse wird gebraucht von:

//HIVDiagnostics.main() –-> erstellt new HIVDiagnosticsGUI() wenn keine Argumente angegeben werden