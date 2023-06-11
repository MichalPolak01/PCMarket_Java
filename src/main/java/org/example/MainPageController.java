package org.example;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * The MainPageController class is responsible for controlling the main page of the application.
 * It handles user interactions and displays various components such as products, shopping cart, and user account information.
 * The class implements the Initializable interface to initialize the JavaFX components and define the behavior of the main page.
 */
public class MainPageController implements Initializable {
        @FXML
        private Label accountLabel;

        @FXML
        private ComboBox<Integer> amount;

        @FXML
        private GridPane account_pane;

        @FXML
        private AnchorPane chosenProduct;

        @FXML
        private Label description;

        @FXML
        private AnchorPane main_anchor;

        @FXML
        private GridPane grid;

        @FXML
        private ImageView image;

        @FXML
        private Button logout;

        @FXML
        private Label name;

        @FXML
        private Label price;

        @FXML
        private ComboBox<String> product_category;

        @FXML
        private TextField searchLabel;

        @FXML
        private AnchorPane searchPane;

        @FXML
        private TableColumn<?, ?> order_amount;

        @FXML
        private TableColumn<ShoppingList, String> order_name;

        @FXML
        private TableColumn<ShoppingList, String> order_price;

        @FXML
        private TableView<ShoppingList> order_tableView;

        @FXML
        private Label o_city;

        @FXML
        private Label o_email;

        @FXML
        private Label o_name;

        @FXML
        private Label o_phone;

        @FXML
        private Label o_street;

        @FXML
        private Label o_surname;

        @FXML
        private Label o_zipCode;

        @FXML
        private TextField s_city;

        @FXML
        private TextField s_email;

        @FXML
        private TextField s_name;

        @FXML
        private TextField s_phone;

        @FXML
        private TextField s_street;

        @FXML
        private TextField s_surname;

        @FXML
        private TextField s_zipCode;

        @FXML
        private Label order_total;

        /**
         * The alert dialog used for displaying messages.
         */
        private Alert alert;

        /**
         * The listener for item click events.
         */
        private ItemListener itemListener;
        private String get_id = "0";
        private String get_name;

        /**
         * Initializes the main page by setting up the initial state and loading data.
         *
         * @param url            The URL location of the FXML file.
         * @param resourceBundle The resource bundle containing locale-specific objects.
         */
        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                chooseCategory();
                showAll();

                getPersonalData();
                getUserAdress();

                showAccount();
        }

        /**
         * Logs out the current user and redirects to the login page.
         * Prompts the user for confirmation before logging out.
         * If confirmed, hides the current window and opens the login page in a new window.
         * If canceled, no action is taken.
         */
        public void logout() {
                try {
                        alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Wylogowywanie");
                        alert.setHeaderText(null);
                        alert.setContentText("Czy na pewno chcesz się wylogować?");
                        Optional<ButtonType> option = alert.showAndWait();

                        if (option.get().equals(ButtonType.OK)) {

                                logout.getScene().getWindow().hide();
                                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login.fxml")));
                                Stage stage = new Stage();
                                Scene scene = new Scene(root);
                                stage.setResizable(false);
                                stage.setTitle("Logowanie");
                                stage.setScene(scene);
                                stage.show();
                        }
                } catch (IOException e) {
                        throw new RuntimeException(e);
                }
        }

        private String[] categories = {"Laptop", "Monitor", "Procesor", "Karta graficzna", "Płyta główna", "Pamięć RAM", "Dysk SSD",
                "Obudowa komputerowa", "Słuchawki", "Mysz", "Klawiatura", "Mikrofon", "Głośniki", "Router", "Kamera", "Inne"};

        /**
         * Allows the user to choose a category from the predefined list of categories.
         * Populates the product_category ComboBox with the available categories.
         * When a category is selected, it queries the database for products belonging to that category and displays them.
         */
        public void chooseCategory() {
                List<String> listCategories = new ArrayList<>();

                for(String category: categories) {
                        listCategories.add(category);
                }

                ObservableList listData = observableArrayList(listCategories);
                product_category.setItems(listData);

                product_category.valueProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                                String category = newValue.toString();

                                String query = "SELECT * FROM psm_computer_store.produkty WHERE kategoria like '%" + category + "%';";
                                showProducts(query);
//                                product_category.setValue(null);
                        }
                });
        }

        List<Item> items = new ArrayList<>();

        /**
         * Retrieves the products from the database based on the given query.
         *
         * @param query The SQL query used to retrieve the products.
         * @return A list of Item objects representing the retrieved products.
         */
        private List<Item> getProducts(String query) {
                Connect connect = new Connect();

                ResultSet result = connect.select(query, connect.getConnection());
                List<Item> items = new ArrayList<>();

                try {
                        while (result.next()) {
                                items.add(new Item(result.getString(1), result.getString(2), result.getString(3),
                                        result.getString(4), result.getString(5), result.getString(6), result.getString(7)
                                        , result.getString(8), result.getDate(9)));
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                connect.close();

                return items;
        }

        /**
         * Updates the product view by populating the grid with items from the items list.
         * Clears the existing items in the grid before updating.
         * Determines the number of columns based on the width of the main_anchor component.
         */
        private void updateProductView() {
                grid.getChildren().clear();

                int row = 0;
                int maxColumns = (int) (main_anchor.getWidth() / 280);

                if (maxColumns == 0) {
                        return;
                }

                for (int i = 0; i < items.size(); i++) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("item.fxml"));
                        AnchorPane anchorPane;
                        try {
                                anchorPane = fxmlLoader.load();
                        } catch (IOException e) {
                                throw new RuntimeException(e);
                        }

                        int column = i % maxColumns;

                        if (column == 0) {
                                row++;
                        }

                        ItemControler itemController = fxmlLoader.getController();
                        itemController.setData(items.get(i), itemListener);

                        grid.add(anchorPane, column, row);
                        GridPane.setMargin(anchorPane, new Insets(8));
                }
        }

        /**
         * Sets up a width change listener on the main_anchor component.
         * Whenever the width changes, it recalculates the number of columns and updates the product view accordingly.
         */
        private void setupWidthListener() {
                main_anchor.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                        int currentColumns = (int) (oldWidth.doubleValue() / 280); // Aktualna liczba kolumn
                        int newColumns = (int) (newWidth.doubleValue() / 280); // Nowa liczba kolumn po zmianie rozmiaru

                        if (newColumns > currentColumns || newColumns < currentColumns) {
                                // Dodaj lub usuń kolumny tylko wtedy, gdy liczba kolumn się zmieniła
                                updateProductView();
                        }
                });
        }

        /**
         * Sets up a font size change listener on the chosenProduct component.
         * Whenever the height changes, it adjusts the font size of the description text based on the new height.
         */
        private void setupFontListener() {
                chosenProduct.heightProperty().addListener((obs, oldHeight, newHeight) -> {
                        double anchorPaneHeight = newHeight.doubleValue();
                        double fontSize = anchorPaneHeight * 0.025;
                        description.setStyle("-fx-font-size:"+fontSize);
                });
        }

        /**
         * Displays products based on the given query.
         * Updates the searchLabel, items list, and product view.
         * Sets the chosen product as the first item in the items list.
         * Sets up width and font size change listeners.
         *
         * @param query The SQL query used to retrieve the products.
         */
        private void showProducts(String query) {
                searchLabel.setText("");
                items = getProducts(query);
                updateProductView();

                if (items.size() > 0) {
                        setChosenProduct(items.get(0));
                        itemListener = new ItemListener() {
                                @Override
                                public void onClickListener(Item item) {
                                        setChosenProduct(item);
                                        chosenProduct.setVisible(true);
                                        searchPane.setVisible(false);
                                }
                        };
                }

                setupWidthListener();
                setupFontListener();
        }

        private String productID = "";

        /**
         * Sets the chosen product and updates the corresponding UI elements.
         *
         * @param item The selected Item object representing the chosen product.
         */
        private void setChosenProduct(Item item) {
                name.setText(item.getNazwa());

                Task<Void> task = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                                Image loadImage = new Image(item.getZdjecie());
                                Platform.runLater(() -> image.setImage(loadImage));
                                return null;
                        }
                };
                new Thread(task).start();

                description.setText(item.getOpis());

                List<Integer> numbers = new ArrayList<>();
                int amountValue = Integer.parseInt(item.getIlosc());
                for (int i = 1; i <= amountValue; i++) {
                        numbers.add(i);
                }
                ObservableList listData = observableArrayList(numbers);
                amount.setItems(listData);
                amount.setValue(1);

                amount.valueProperty().addListener((observable, oldValue, newValue) -> {
                        int amountVal;
                        if (newValue == null) {
                                amountVal = 0;
                        } else {
                                amountVal = (int) newValue;
                        }
                        double priceValue = Double.parseDouble(item.getCena());

                        double totalPrice = amountVal * priceValue;

                        price.setText(String.valueOf(totalPrice)+ " zł");
                });

                productID = item.getId_produktu();

                showCart();
        }

        /**
         * Adds the selected product to the shopping cart.
         * Performs a validation check on the available amount before adding.
         * Displays an error message if the available amount is insufficient.
         * Inserts the product into the database and displays a success message.
         * Refreshes the cart view.
         */
        public void addToCart() {
                Integer valAmount = (Integer) amount.getValue();
                String queryAvailableAmount = "SELECT ilosc FROM psm_computer_store.produkty WHERE id_produktu="+productID+";";

                ResultSet resultAvaliableAmount = select(queryAvailableAmount);

                String avaliableAmount = "0";

                try {
                        if (resultAvaliableAmount.next()) {
                                avaliableAmount = resultAvaliableAmount.getString(1);
                        }
                } catch (SQLException e) {
                        throw new RuntimeException(e);
                }
                Integer avaliableAmountInt = Integer.parseInt(avaliableAmount);
                if (avaliableAmountInt < valAmount ) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Brak produktu");
                        alert.setHeaderText(null);
                        alert.setContentText("Niestety nie posiadamy takiej ilości produktu" + name.getText());
                        alert.showAndWait();

                } else {
                        String query = "INSERT INTO psm_computer_store.lista_zakupow(id_osoby, id_produktu, ilosc)" +
                                " VALUES("+data.userID+", "+productID+", "+valAmount+")";
                        insert(query);

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Dodano do koszyka");
                        alert.setHeaderText(null);
                        alert.setContentText("Poprawnie dodano produkt "+name.getText()+" do koszyka!");
                        alert.showAndWait();

                        showCart();
                }
        }

        /**
         * Retrieves the shopping list for the current user.
         * Executes an SQL query to fetch the shopping list items.
         *
         * @return An ObservableList of ShoppingList objects representing the items in the shopping list.
         */
        private ObservableList<ShoppingList> getShoppingList() {
                String query = "SELECT id_produktu_w_koszyku, id_osoby, lz.id_produktu, lz.ilosc, nazwa, cena, (lz.ilosc * cena) AS total FROM psm_computer_store.lista_zakupow lz " +
                        "INNER JOIN psm_computer_store.produkty pp ON lz.id_produktu = pp.id_produktu WHERE id_osoby ="+data.userID+";";

                ResultSet result = select(query);
                ObservableList<ShoppingList> items = observableArrayList();

                try {
                        while (result.next()) {
                                items.add(new ShoppingList(result.getString(1), result.getString(2), result.getString(3),
                                        result.getString(4), result.getString(5), result.getString(6), result.getString(7)));
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                return items;
        }

        Double total = 0.00;
        private ObservableList<ShoppingList> shoppingList;

        /**
         * Displays the shopping cart with the items from the shopping list.
         * Sets up the table view and calculates the total price of the items.
         * Updates the UI elements with the cart information.
         */
        private void showCart() {
                Label placeholderLabel = new Label("Dodaj coś do koszyka ;)");
                order_tableView.setPlaceholder(placeholderLabel);

                shoppingList = getShoppingList();
                order_name.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
                order_amount.setCellValueFactory(new PropertyValueFactory<>("ilosc"));
                order_price.setCellValueFactory(cellData -> {
                        String cenaText = cellData.getValue().getTotal();
                        Double cenaValue = Double.parseDouble(cenaText);
                        ObjectProperty<Double> property = new SimpleObjectProperty<>(cenaValue);
                        return Bindings.createStringBinding(() -> String.format("%.2f zł", property.get()), property);
                });

                order_tableView.setItems(shoppingList);

                Double total = 0.00;
                for (int i = 0; i < shoppingList.size(); i++) {
                        total += Double.parseDouble(shoppingList.get(i).getTotal());
                }

                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                String totalText = decimalFormat.format(total);
                order_total.setText(totalText + " zł");
        }

        /**
         * Retrieves the selected order from the table view.
         * Gets the product ID and name of the selected item.
         * If no item is selected, returns without further action.
         */
        public void selectedOrder() {
                ShoppingList product = order_tableView.getSelectionModel().getSelectedItem();
                int num = order_tableView.getSelectionModel().getSelectedIndex();

                if ((num - 1) < -1 ) return;

                get_id = product.getId_produktu_w_koszyku();
                get_name = product.getNazwa();
        }

        /**
         * Removes the selected product from the shopping cart.
         * Performs a validation check on the selected product.
         * Deletes the product from the database and displays a success message.
         * Refreshes the cart view.
         */
        public void removeProductFromCart() {
                if (Objects.equals(get_id, "0")) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Wybierz produkt");
                        alert.setHeaderText(null);
                        alert.setContentText("Brak wybranego produktu!");
                        alert.showAndWait();
                } else {
                        String query = "DELETE FROM psm_computer_store.lista_zakupow WHERE id_produktu_w_koszyku="+get_id+" AND id_osoby="+data.userID;
                        get_id = "0";
                        update(query);

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Usunięto z koszyka");
                        alert.setHeaderText(null);
                        alert.setContentText("Poprawnie usunięto produkt "+get_name+" z koszyka!");
                        alert.showAndWait();

                        showCart();
                }
        }

        /**
         * Executes a search query based on the searchLabel text.
         * Displays the search results in the product view.
         */
        public void search() {
                String query = "SELECT * FROM psm_computer_store.produkty WHERE nazwa like '%"+searchLabel.getText()+"%';";
                showProducts(query);
        }

        /**
         * Displays all products in the product view.
         */
        public void showAll() {
                String query = "SELECT * FROM psm_computer_store.produkty ";
                showProducts(query);
        }

        /**
         * Handles the onClick event for the search button.
         * Shows the search pane and hides the chosen product pane.
         */
        public void onClickSearch() {
                searchPane.setVisible(true);
                chosenProduct.setVisible(false);
        }

        /**
         * Creates an order based on the selected products in the shopping list.
         * Generates an order ID and order date.
         * Inserts the order and ordered products into the database.
         * Deletes the products from the shopping list.
         * Displays a success message and refreshes the cart view.
         */
        public void createOreder() {
                String orderId = "uid"+data.userID+"dt"+java.time.LocalTime.now();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String orderDate = formatter.format(date);

                String queryUpdate = "";
                String query = "INSERT INTO psm_computer_store.zamowienia(id_zamowienia, id_klienta, kwota, data_zlozenia_zamowienia) " +
                        "VALUES ('"+orderId+"', '"+data.userID+"', '"+total+"', '"+orderDate+"'); ";
                for ( ShoppingList product : shoppingList) {
                        query += " INSERT INTO psm_computer_store.zamowione_produkty(id_zamowienia, id_produktu, ilosc) " +
                                "VALUES ('"+orderId+"', '"+product.getId_produktu()+"', '"+product.getIlosc()+"'); ";

                        queryUpdate += " UPDATE psm_computer_store.produkty SET ilosc = ilosc - "+product.getIlosc()+" WHERE id_produktu = '"+product.getId_produktu()+"'; ";
                }
                insert(query);
                update(queryUpdate);
                deleteFromShoppingList(data.userID);

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Zamówienie złożone");
                alert.setHeaderText(null);
                alert.setContentText("Pomyślnie złożono zamowienie!");
                alert.showAndWait();

                searchPane.setVisible(true);
                chosenProduct.setVisible(false);
                showCart();
        }

        /**
         * Deletes all products from the shopping list for the current user.
         *
         * @param userID The ID of the current user.
         */
        private void deleteFromShoppingList(String userID) {
                String query = "DELETE FROM psm_computer_store.lista_zakupow WHERE id_osoby="+userID;

                update(query);
        }

        /**
         * Retrieves the personal data of the current user.
         * Executes an SQL query to fetch the user information.
         * Updates the UI elements with the user data.
         */
        private void getPersonalData() {
                Connect connect = new Connect();

                String query = "SELECT * FROM psm_computer_store.osoby WHERE id_osoby="+data.userID+";";
                ResultSet result = connect.select(query, connect.getConnection());

                List<User> user = new ArrayList<>();

                try {
                        while (result.next()) {
                                user.add(new User(result.getString(1), result.getString(2), result.getString(3),
                                        result.getString(4), result.getString(5), result.getString(6), result.getString(7)
                                        , result.getString(8)));
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                connect.close();

                s_name.setText(user.get(0).getImie());
                s_surname.setText(user.get(0).getNazwisko());
                s_phone.setText(user.get(0).getNr_telefonu());
                s_email.setText(user.get(0).getEmail());

                o_name.setText(user.get(0).getImie());
                o_surname.setText(user.get(0).getNazwisko());
                o_phone.setText(user.get(0).getNr_telefonu());
                o_email.setText(user.get(0).getEmail());

        }

        /**
         * Retrieves the user's address data.
         * Executes an SQL query to fetch the address information.
         * Updates the UI elements with the address data.
         */
        private void getUserAdress() {
                Connect connect = new Connect();

                String query = "SELECT * FROM psm_computer_store.adresy WHERE id_adresu="+data.addressID+";";
                ResultSet result = connect.select(query, connect.getConnection());

                List<Address> address = new ArrayList<>();

                try {
                        while (result.next()) {
                                address.add(new Address(result.getString(1), result.getString(2), result.getString(3),
                                        result.getString(4)));
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }

                connect.close();

                s_street.setText(address.get(0).getUlica());
                s_zipCode.setText(address.get(0).getKod_pocztowy());
                s_city.setText(address.get(0).getMiasto());

                o_street.setText(address.get(0).getUlica());
                o_zipCode.setText(address.get(0).getKod_pocztowy());
                o_city.setText(address.get(0).getMiasto());
        }

        /**
         * Updates the personal data of the current user.
         * Executes an SQL query to update the user information.
         * Refreshes the personal data view and displays a success message.
         */
        public void updatePersonalData() {
                String query = "UPDATE psm_computer_store.osoby SET imie='"+s_name.getText()+"', nazwisko='"+s_surname.getText()+"', " +
                        "nr_telefonu='"+s_phone.getText()+"', email='"+s_email.getText()+"' WHERE id_osoby="+data.userID ;

                update(query);
                getPersonalData();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Zaktualizowano dane");
                alert.setHeaderText(null);
                alert.setContentText("Pomyślnie zaktualizowano dane!");
                alert.showAndWait();
        }

        /**
         * Updates the address data of the current user.
         * Executes an SQL query to update the address information.
         * Refreshes the address view and displays a success message.
         */
        public void updateAddressData() {
                String query = "UPDATE psm_computer_store.adresy SET ulica='"+s_street.getText()+"', kod_pocztowy='"+s_zipCode.getText()+"', " +
                        "miasto='"+s_city.getText()+"' WHERE id_adresu="+data.addressID;

                update(query);
                getUserAdress();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Zaktualizowano dane");
                alert.setHeaderText(null);
                alert.setContentText("Pomyślnie zaktualizowano dane!");
                alert.showAndWait();
        }

        /**
         * Toggles the visibility of the account pane based on the current state.
         * Updates the text of the account label accordingly.
         * Shows or hides the main anchor and account pane accordingly.
         */
        boolean isAccountVisible = false;
        public void showAccount() {
                accountLabel.setOnMouseClicked(event -> {
                        if (isAccountVisible) {
                                accountLabel.setText("Zmień ustawienia konta");
                                main_anchor.setVisible(true);
                                account_pane.setVisible(false);
                        } else {
                                accountLabel.setText("Powrót do sklepu");
                                main_anchor.setVisible(false);
                                account_pane.setVisible(true);
                        }
                        isAccountVisible = !isAccountVisible;
                });
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
                Connect connect = new Connect();

                ResultSet data = connect.select(query, connect.getConnection());

                connect.close();

                return data;
        }

        /**
         * Executes an insert query on the database.
         * Opens a database connection, performs the insert query, and updates the data.
         * Closes the database connection.
         *
         * @param query The insert query to be executed.
         */
        private void insert(String query) {
                Connect connect = new Connect();

                connect.insert(query, connect.getConnection());

                connect.close();
        }

        /**
         * Executes an update query on the database.
         * Opens a database connection, performs the update query, and updates the data.
         * Closes the database connection.
         *
         * @param query The update query to be executed.
         */
        private void update(String query) {
                Connect connect = new Connect();

                connect.update(query, connect.getConnection());

                connect.close();
        }
}
