package org.example;


import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.opencsv.CSVReader;

public class Main {

    public static void main(String[] args) {
        String csvFile = "C:\\Users\\HP G3\\Bureau\\projet.csv";
        Connection con = null;
        String url = "jdbc:mysql://127.0.0.1:3306/est";
        String user = "root";
        String password = "";
        PreparedStatement pst = null;

        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            String[] nextLine;

            reader.readNext();
           //etablir la connexion avec la base de donnees
            con = DriverManager.getConnection(url, user, password);
            //preparer une instruction sql pour l'insertion des donnees
            pst = con.prepareStatement("INSERT INTO Datas (`Identifiant`, `Nom`, `Prénom`, `Age`, `Téléphone`, `Ville`) VALUES (?, ?, ?, ?, ?, ?)");

           //la boucle while pour lire le fichier csv et stocker ses donnees dans la bd
            while ((nextLine = reader.readNext()) != null) {

            //cette instruction pour diviser la ligne csv en un tableau de valeurs
                String[] values = nextLine[0].split(";");


                String cleanedIdentifiant = values[0].replaceAll("[^\\d]", "");

                pst.setInt(1, Integer.parseInt(cleanedIdentifiant));
                pst.setString(2, values[1]);
                pst.setString(3, values[2]);
                pst.setInt(4, Integer.parseInt(values[3]));
                pst.setInt(5, Integer.parseInt(values[4]));
                pst.setString(6, values[5]);
                //executer la mise a jour sql pour chaque ligne
                pst.executeUpdate();
            }


            System.out.println("Insertions réussies dans votre table 'Datas'");
        } catch (SQLException ex) {
            //cree un objet logger pour enregistrer les msgs lies aux exceptions sql il utilise le nom de la classe main comme source de journalisation
            Logger lgr = Logger.getLogger(Main.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (Exception e) {
            //cree un objet logger pour enregistrer les msgs lies aux exceptions il utilise le nom de la classe main comme source de journalisation
            Logger lgr = Logger.getLogger(Main.class.getName());
            //enregistre l'exception dans le journal avec niveau de severite eleve(severe) cela inclut le msg d'erreur ex.getMessage
            lgr.log(Level.SEVERE, "une exception non gérée s'est produite merci de la vérifier!", e);
        } finally {
            try {
                //ferme l'instruction sql preparee
                if (pst != null) {
                    pst.close();
                }
                //ferme la connexion a la base de donnees
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Main.class.getName());
                //enregistre l'exception dans le journal avec niveau de severite avertissant(severe) cela inclut le msg d'erreur ex.getMessage
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}

