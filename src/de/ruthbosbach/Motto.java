package de.ruthbosbach;

import java.awt.event.WindowAdapter;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.sql.*;

public class Motto extends JFrame {
        // Anfang Attribute
        private DatabaseConnector dbConnector;
        private JTextField tVorname = new JTextField();
        private JTextField tName = new JTextField();
        private JTextField tMotto = new JTextField();
        private JButton jbEintragen = new JButton();
        private JTextArea jtaAusgabe = new JTextArea("");
        private JScrollPane jtaAusgabeScrollPane = new JScrollPane(jtaAusgabe);
        private JLabel jLabel1 = new JLabel();
        private JButton jbVerbinden = new JButton();
    private JButton jbUebersicht = new JButton("Übersicht anzeigen");
    private JButton jbRangliste = new JButton("Rangliste");
    private JButton jbVorschlagAendern = new JButton("Vorschlag ändern");
    private JButton jbSQLInjection = new JButton("Wie oft gibt es den Vornamen?");
    Font f = new Font( Font.SERIF, Font.PLAIN, 28 );
        // Ende Attribute

        public Motto(String title) {
            // Frame-Initialisierung
            super(title);

            int frameWidth = 797;
            int frameHeight = 378;
            setSize(frameWidth, frameHeight);
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (d.width - getSize().width) / 2;
            int y = (d.height - getSize().height) / 2;
            setLocation(x, y);
            Container cp = getContentPane();
            cp.setLayout(null);
            // Anfang Komponenten

            tVorname.setBounds(16, 72, 105, 24);
            tVorname.setText("Vorname");
            cp.add(tVorname);
            tName.setBounds(136, 72, 105, 24);
            tName.setText("Name");
            cp.add(tName);
            tMotto.setBounds(16, 104, 225, 24);
            tMotto.setText("Mottovorschlag");
            cp.add(tMotto);
            jbEintragen.setBounds(16, 136, 227, 25);
            jbEintragen.setText("Mottovorschlag einreichen/wählen");
            jbEintragen.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    jbEintragen_ActionPerformed(evt);
                }
            });
            cp.add(jbEintragen);
            jtaAusgabeScrollPane.setBounds(272, 48, 489, 281);
            cp.add(jtaAusgabeScrollPane);
            setTitle("AbiMotto");
            jLabel1.setBounds(16, 8, 682, 20);
            jLabel1.setText("Bitte prüfen Sie: DB-Treiber in Bibliotheken vorhanden, DB abimotto.db vorhanden");
            cp.add(jLabel1);
            jbVerbinden.setBounds(16, 32, 227, 25);
            jbVerbinden.setText("Datenbankverbindung");
            jbVerbinden.setMargin(new Insets(2, 2, 2, 2));
            jbVerbinden.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    jbVerbinden_ActionPerformed(evt);
                }
            });
            cp.add(jbVerbinden);
            // Ende Komponenten

            //Anfügen des Schliessen des Fensters
            addWindowListener(new WindowHandler());
            setResizable(false);
            setVisible(true);
        }

        // Anfang Methoden
        public void jbEintragen_ActionPerformed(ActionEvent evt) {
            //Was macht die Methode trim()?
            String vorname = tVorname.getText().trim();
            String name = tName.getText().trim();
            String motto = tMotto.getText().trim();

            //Hier Quelltext einfügen
            int userid=-1;
            dbConnector.executeStatement("SELECT _id FROM user WHERE name='"+name+"' AND vorname='"+vorname+"'");
            QueryResult r = dbConnector.getCurrentQueryResult();

            if (r != null && r.getRowCount() == 0 ) {
                dbConnector.executeStatement("INSERT INTO user(name,vorname) VALUES('"+name+"','"+vorname+"')");
                dbConnector.executeStatement("SELECT _id FROM user WHERE name='"+name+"' AND vorname='"+vorname+"'");
                r = dbConnector.getCurrentQueryResult();
                //[Zeile des Resultsets][Spalte des Resultsets]
                //Was macht die Methode Integer.parseInt()?
                //Wie arbeitet r.getData()? => s. Dokumentation
                userid = Integer.parseInt(r.getData()[0][0]);
            } else {
                userid = Integer.parseInt(r.getData()[0][0]);
            }
            //Eintragen des Mottos
            int mottoid=-1;

            //Eintragen des Vorschlags


            //Ausgabe des Resultsets in der JTextArea

            jtaAusgabe.setText("Vorname\tMottovorschlag\n-------------------------------------------------------------------\n");
            
            //...

            //Ende des eigenen Quelltextes

        }
        public void jbVerbinden_ActionPerformed(ActionEvent evt) {
            //dbConnector = new DatabaseConnector("127.0.0.1", 3306, "abimotto", "", "");
            //Bei sqlite muss nur der Name der Datenbank angegeben werden, der Port wird ignoriert.
            dbConnector = new DatabaseConnector("",3306, "C:\\Users\\julia.schna\\abimottoIntelliJ0\\abimotto.db", "", "");
            String fehler = dbConnector.getErrorMessage();
            if (fehler == null) {
                jLabel1.setText("Datenbank wurde erfolgreich verbunden!");
            } else {
                jLabel1.setText("Fehlermeldung: " + fehler);
            }
   /*
     Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName("org.mariadb.jdbc.Driver");

            //STEP 3: Open a connection
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(
                    "jdbc:mariadb://192.168.28.70/otto1", "otto1", "otto1");
            System.out.println("Connected database successfully...");

        }
        catch(Exception e){
            e.printStackTrace();
        }
        */
        }

        // Ende Methoden



        /**
         * Innere Klasse zum WindowEventhandling, um die Datenbankverbindung beim Schliessen des Fensters zu trennen
         * @author Ruth Bosbach
         * @version 2021
         */

        class WindowHandler extends WindowAdapter {
            public void windowClosing(WindowEvent e) {
                //Wenn wir tatsächlich eine Verbindung zu SQLite-Datenbank aufgebaut haben, schliessen wir diese beim Schliessen des Fensters
                if (dbConnector != null)
                    dbConnector.close();
                System.exit(0); // Ende der Anwendung
            }
        }

    }


