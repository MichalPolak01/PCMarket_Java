package org.example;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class that handles the logic of the login view.
 */
public class LoginController {

    @FXML
    private Button changeSide_login;

    @FXML
    private Button changeSide_signUp;

    @FXML
    private TextField l_email;

    @FXML
    private TextField l_password;

    @FXML
    private Button log_in;

    @FXML
    private TextField s_city;

    @FXML
    private TextField s_email;

    @FXML
    private TextField s_name;

    @FXML
    private TextField s_password;

    @FXML
    private TextField s_phone;

    @FXML
    private TextField s_street;

    @FXML
    private TextField s_surname;

    @FXML
    private TextField s_zipCode;

    @FXML
    private AnchorPane side_changeSide;

    private Connect connect;

    private Alert alert;

    /**
     * Handles the login process.
     *
     * @throws IOException Thrown when there is an issue with loading the FXML file.
     */
    public void login() throws IOException {
        if (l_email.getText().isEmpty() || l_password.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Puste pola");
            alert.setHeaderText(null);
            alert.setContentText("Wprowadź dane!");
            alert.showAndWait();
        } else {
            String query = "SELECT * FROM psm_computer_store.osoby WHERE email='"+l_email.getText()+"' AND haslo='"+l_password.getText()+"'";
            ResultSet result = select(query);

            List<User> users = new ArrayList<>();


            try {
                while (result.next()) {
                    users.add(new User(result.getString(1), result.getString(2), result.getString(3),
                            result.getString(4), result.getString(5), result.getString(6), result.getString(7)
                            , result.getString(8)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(!users.isEmpty()) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Powitanie");
                alert.setHeaderText(null);
                alert.setContentText("Witaj "+users.get(0).getImie());
                alert.showAndWait();

                String role = users.get(0).getRola();
                data.username = users.get(0).getImie();
                data.userID = users.get(0).getId_osoby();
                data.addressID = users.get(0).getId_adresu();

                Stage stage = new Stage();
                Parent root;
                if (role.equals("pracownik")) {
                    root = FXMLLoader.load(getClass().getResource("admin-activity.fxml"));
                    stage.setTitle("Admin Page");
                } else {
                    root = FXMLLoader.load(getClass().getResource("customer-activity.fxml"));
                    stage.setTitle("PCMarket");
                }

                Scene scene = new Scene(root);
                stage.setResizable(true);
                stage.setMinWidth(1344);
                stage.setMinHeight(756);

                stage.setScene(scene);
                stage.show();
                log_in.getScene().getWindow().hide();

            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błędne dane");
                alert.setHeaderText(null);
                alert.setContentText("Niepoprawny e-mail lub hasło!");
                alert.showAndWait();
            }

        }
    }

    /**
     * Handles the user registration process.
     */
    public void register() {
        if (s_name.getText().isEmpty() || s_surname.getText().isEmpty() || s_phone.getText().isEmpty() || s_email.getText().isEmpty()
        || s_password.getText().isEmpty() || s_street.getText().isEmpty() || s_zipCode.getText().isEmpty() || s_city.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Puste pola");
            alert.setHeaderText(null);
            alert.setContentText("Wprowadź wszystkie dane!");
            alert.showAndWait();

        } else {
            String queryAddress = "INSERT INTO psm_computer_store.adresy(ulica, kod_pocztowy, miasto) " +
                    "VALUES('"+s_street.getText()+"', '"+s_zipCode.getText()+"', '"+s_city.getText()+"');";

            String queryPersonal = "INSERT INTO psm_computer_store.osoby (imie, nazwisko, email, nr_telefonu, haslo, id_adresu) " +
                    "VALUES ('"+s_name.getText()+"', '"+s_surname.getText()+"', '"+s_email.getText()+"', '"+s_phone.getText()+"', '"+s_password.getText()+"'" +
                    ",(SELECT id_adresu FROM psm_computer_store.adresy WHERE ulica='"+s_street.getText()+"' " +
                    "AND kod_pocztowy='"+s_zipCode.getText()+"' AND miasto='"+s_city.getText()+"'));";

            String selectAddress = "SELECT id_adresu FROM psm_computer_store.adresy WHERE ulica='"+s_street.getText()+"'" +
                    "AND kod_pocztowy='"+s_zipCode.getText()+"' AND miasto='"+s_city.getText()+"';";

            String selectUser = "SELECT id_osoby FROM psm_computer_store.osoby WHERE email='"+s_email.getText()+"';";

            String idAdresu = "";

            try{
                ResultSet isUser = select(selectUser);
                String userId = "";
                while (isUser.next()) {
                    userId = isUser.getString(1);
                }
                if (userId == "") {
                    ResultSet isAddress = select(selectAddress);

                    while (isAddress.next()) {
                        idAdresu = isAddress.getString(1);
                    }
                    if (idAdresu == "") {
                        insert(queryAddress);
                        insert(queryPersonal);
                    } else {
                        String queryPersonalWhenAddressExist = "INSERT INTO psm_computer_store.osoby (imie, nazwisko, email, nr_telefonu, haslo, id_adresu) " +
                                "VALUES ('"+s_name.getText()+"', '"+s_surname.getText()+"', '"+s_email.getText()+"', '"+s_phone.getText()+"', '"+s_password.getText()+"',"+idAdresu+");";

                        insert(queryPersonalWhenAddressExist);
                    };

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Zarejsestrowano");
                    alert.setHeaderText(null);
                    alert.setContentText("Rejestracja przebiegła pomyślnie!");
                    alert.showAndWait();

                    s_name.setText("");
                    s_surname.setText("");
                    s_email.setText("");
                    s_phone.setText("");
                    s_password.setText("");
                    s_city.setText("");
                    s_zipCode.setText("");
                    s_street.setText("");

                    TranslateTransition slider = new TranslateTransition();

                    slider.setNode(side_changeSide);
                    slider.setToX(0);

                    slider.setDuration(Duration.seconds(1));

                    slider.setOnFinished((ActionEvent e) -> {
                        changeSide_signUp.setVisible(true);
                        changeSide_login.setVisible(false);
                    });

                    slider.play();

                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Konto istnieje");
                    alert.setHeaderText(null);
                    alert.setContentText("Użytkownik z takim adresem e-mail już istenieje!");
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Executes an insert query on the database.
     * Opens a database connection, performs the insert query, and updates the data.
     * Closes the database connection.
     *
     * @param query The insert query to be executed.
     */
    private void insert(String query) {
        connect = new Connect();

        connect.insert(query, connect.getConnection());

        connect.close();
    }

    /**
     * Executes a select query on the database.
     * Opens a database connection, performs the query, and retrieves the data.
     * Closes the database connection and returns the result set.
     *
     * @param query The select query to be executed.
     * @return The result set containing the retrieved data.
     */
    private ResultSet select(String query) {
        connect = new Connect();

        ResultSet person = connect.select(query, connect.getConnection());

        connect.close();

        return person;
    }

    /**
     * Method that handles the screen change between login and registration.
     * @param event the event that triggers the screen change
     */
    public void stageSide(ActionEvent event) {

        TranslateTransition slider = new TranslateTransition();

        if (event.getSource() == changeSide_signUp) {
            slider.setNode(side_changeSide);
            slider.setToX(480);
            slider.setDuration(Duration.seconds(1));

            slider.setOnFinished((ActionEvent e) -> {
                changeSide_signUp.setVisible(false);
                changeSide_login.setVisible(true);
            });

            slider.play();
        } else if (event.getSource() == changeSide_login) {
            slider.setNode(side_changeSide);
            slider.setToX(0);

            slider.setDuration(Duration.seconds(1));

            slider.setOnFinished((ActionEvent e) -> {
                changeSide_signUp.setVisible(true);
                changeSide_login.setVisible(false);
            });

            slider.play();
        }
    }
}
