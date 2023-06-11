package org.example;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * The controller class for the main page of the administrator.
 * It handles the navigation, user interface components, and actions related to the administrator's tasks.
 */
public class AdminMainPageController implements Initializable {
    @FXML
    private GridPane grid;

    @FXML
    private AnchorPane addPane;

    @FXML
    private AnchorPane updatePane;

    @FXML
    private AnchorPane deletePane;

    @FXML
    private TextField add_product_amount;

    @FXML
    private ComboBox<?> add_product_category;

    @FXML
    private TextArea add_product_description;

    @FXML
    private TextField add_product_mark;

    @FXML
    private TextField add_product_name;

    @FXML
    private TextField add_product_price;

    @FXML
    private TextField add_product_link_img;

    @FXML
    private TextField update_product_amount;

    @FXML
    private ComboBox<String> update_product_category;

    @FXML
    private TextArea update_product_description;

    @FXML
    private TextField update_product_id;

    @FXML
    private TextField update_product_link_img;

    @FXML
    private TextField update_product_mark;

    @FXML
    private TextField update_product_name;

    @FXML
    private TextField update_product_price;

    @FXML
    private Button update_product_button;

    @FXML
    private TextField delete_product_id;

    @FXML
    private Button admin_panel;

    @FXML
    private Button dashboard;

    @FXML
    private Button logout;

    @FXML
    private ComboBox<String> operation;

    @FXML
    private Label operation_name;

    @FXML
    private Button orders;

    @FXML
    private Button storage;

    @FXML
    private Label label_welcome;

    @FXML
    private TableColumn<Item, String> inventory_col_amount;

    @FXML
    private TableColumn<Item, String>inventory_col_category;

    @FXML
    private TableColumn<Item, String> inventory_col_date;

    @FXML
    private TableColumn<Item, String> inventory_col_description;

    @FXML
    private TableColumn<Item, String> inventory_col_link_img;

    @FXML
    private TableColumn<Item, String> inventory_col_mark;

    @FXML
    private TableColumn<Item, String> inventory_col_name;

    @FXML
    private TableColumn<Item, String> inventory_col_price;

    @FXML
    private TableColumn<Item, String> inventory_col_product_id;

    @FXML
    private TableView<Item> inventory_tableView;

    @FXML
    private BarChart<?, ?> dashboard_items;

    @FXML
    private Label dashboard_monthly_revenues;

    @FXML
    private AreaChart<?, ?> dashboard_revenues;

    @FXML
    private Label dashboard_sold_items;

    @FXML
    private Label dashboard_todays_revenues;

    @FXML
    private Label dashboard_users;

    @FXML
    private AnchorPane scene_dashboard;

    @FXML
    private AnchorPane scene_inventory;

    @FXML
    private AnchorPane scene_admin_panel;

    @FXML
    private AnchorPane scene_orders;

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
    private TextField worker_email;

    @FXML
    private ScrollPane scroll;

    private Alert alert;

    /**
     * Initializes the controller.
     * This method is automatically called after the FXML file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        navigation();
        updateProductView();

        displayUsername();

        chooseOperation();
        addProduct();
        updateProduct();

        chooseCategory();

        showItems();

        dashboardDisplayInfo();

        getPersonalData();
        getUserAdress();
    }

    /**
     * Handles the navigation between different sections of the administrator's page.
     * Switches the visibility of the corresponding anchor panes to show the selected section.
     */
    private void navigation() {
        dashboard.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene_inventory.setVisible(false);
                scene_admin_panel.setVisible(false);
                scene_dashboard.setVisible(true);
                scene_orders.setVisible(false);
            }
        });
        storage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene_inventory.setVisible(true);
                scene_admin_panel.setVisible(false);
                scene_dashboard.setVisible(false);
                scene_orders.setVisible(false);
            }
        });
        admin_panel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene_inventory.setVisible(false);
                scene_admin_panel.setVisible(true);
                scene_dashboard.setVisible(false);
                scene_orders.setVisible(false);
            }
        });
        orders.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                scene_inventory.setVisible(false);
                scene_admin_panel.setVisible(false);
                scene_dashboard.setVisible(false);
                scene_orders.setVisible(true);
            }
        });;
    }

    /**
     * Logs out the administrator and redirects to the login page.
     * Displays a confirmation dialog before logging out.
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

    /**
     * Displays the username of the logged-in administrator.
     * The username is capitalized and appended to the welcome label text.
     */
    public void displayUsername() {
        String user = data.username;
        user = user.substring(0, 1).toUpperCase() + user.substring(1);

        label_welcome.setText("Witaj, "+user);
    }

    private String[] categories = {"Laptop", "Monitor", "Procesor", "Karta graficzna", "Płyta główna", "Pamięć RAM", "Dysk SSD",
            "Obudowa komputerowa", "Słuchawki", "Mysz", "Klawiatura", "Mikrofon", "Głośniki", "Router", "Kamera", "Inne"};

    /**
     * Sets up the category selection ComboBox with the predefined list of categories.
     */
    public void chooseCategory() {
        List<String> listCategories = new ArrayList<>();

        for(String category: categories) {
            listCategories.add(category);
        }

        ObservableList listData = observableArrayList(listCategories);
        add_product_category.setItems(listData);
        update_product_category.setItems(listData);
    }

    private String[] operations = {"Add", "Update", "Delete"};

    /**
     * Sets up the operation selection ComboBox with the predefined list of operations.
     * Updates the UI components based on the selected operation.
     */
    public void chooseOperation() {
        List<String> listOperations = new ArrayList<>();

        for(String operation: operations) {
            listOperations.add(operation);
        }

        ObservableList listData = observableArrayList(listOperations);
        operation.setItems(listData);

        operation.setValue("Add");
        addPane.setVisible(true);
        updatePane.setVisible(false);
        deletePane.setVisible(false);
        operation_name.setText("Dodaj nowy produkt:");

        operation.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("Add")) {
                addPane.setVisible(true);
                updatePane.setVisible(false);
                deletePane.setVisible(false);
                operation_name.setText("Dodaj nowy produkt:");
            } else if (newValue.equals("Update")) {
                updatePane.setVisible(true);
                addPane.setVisible(false);
                deletePane.setVisible(false);
                operation_name.setText("Aktualizuj produkt");
            } else if (newValue.equals("Delete")) {
                deletePane.setVisible(true);
                addPane.setVisible(false);
                updatePane.setVisible(false);
                operation_name.setText("Usuń produkt:");
            }
        });
    }

    /**
     * Retrieves the products from the database and populates the inventory table with the data.
     *
     * @return The list of products retrieved from the database.
     */
    private ObservableList<Item> inventoryList;
    private ObservableList<Item> getProducts() {
        Connect connect = new Connect();

        String query = "SELECT * FROM psm_computer_store.produkty ";

        ResultSet result = connect.select(query, connect.getConnection());
        ObservableList<Item> items = observableArrayList();

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
     * Displays the list of products in the inventory table.
     * The products are sorted by date of addition in descending order.
     */
    public void  showItems() {
        inventoryList = getProducts();
        inventory_col_product_id.setCellValueFactory(new PropertyValueFactory<>("id_produktu"));
        inventory_col_name.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
        inventory_col_mark.setCellValueFactory(new PropertyValueFactory<>("marka"));
        inventory_col_price.setCellValueFactory(cellData -> {
            String cenaText = cellData.getValue().getCena();
            Double cenaValue = Double.parseDouble(cenaText);
            ObjectProperty<Double> property = new SimpleObjectProperty<>(cenaValue);
            return Bindings.createStringBinding(() -> String.format("%.2f zł", property.get()), property);
        });
        inventory_col_amount.setCellValueFactory(new PropertyValueFactory<>("ilosc"));
        inventory_col_category.setCellValueFactory(new PropertyValueFactory<>("kategoria"));
        inventory_col_link_img.setCellValueFactory(new PropertyValueFactory<>("zdjecie"));
        inventory_col_description.setCellValueFactory(new PropertyValueFactory<>("opis"));
        inventory_col_date.setCellValueFactory(new PropertyValueFactory<>("data_dodania"));

        inventoryList.sort((product1, product2) -> product2.getData_dodania().compareTo(product1.getData_dodania()));
        inventory_tableView.setItems(inventoryList);
    }

    /**
     * Validates the input for adding a product.
     * It checks if the price and amount fields have valid formats and restricts the input accordingly.
     */
    private void addProduct() {
        add_product_price.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("(\\d)*(\\.\\d{0,2})?")) {
                add_product_price.setText(oldValue);
            }
        });

        add_product_amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                add_product_amount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /**
     * Handles the event when the "Add Product" button is clicked.
     * It validates the input fields, inserts the new product into the database,
     * displays a success message, updates the displayed list of products,
     * and clears the input fields.
     */
    public void onClickAddProduct() {
        if(add_product_name.getText().isEmpty() || add_product_mark.getText().isEmpty() || add_product_link_img.getText().isEmpty()
        || add_product_description.getText().isEmpty() || add_product_price.getText().isEmpty() || add_product_amount.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Puste pola");
            alert.setHeaderText(null);
            alert.setContentText("Wypełnij wszystkie pola!");
            alert.showAndWait();
        } else {
            String query = "INSERT INTO psm_computer_store.produkty (nazwa, marka, cena, zdjecie, ilosc, opis, kategoria) " +
                    "VALUES ('"+add_product_name.getText()+"', '"+add_product_mark.getText()+"', '"+add_product_price.getText()+"'" +
                    ", '"+add_product_link_img.getText()+"', '"+add_product_amount.getText()+"', '"+add_product_description.getText()+"'" +
                    ", '"+add_product_category.getValue()+"')";

            insert(query);

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodano produkt");
            alert.setHeaderText(null);
            alert.setContentText("Produkt "+add_product_name.getText()+" został pomyślnie dodany!");
            alert.showAndWait();

            showItems();

            add_product_name.setText("");
            add_product_mark.setText("");
            add_product_price.setText("");
            add_product_amount.setText("");
            add_product_link_img.setText("");
            add_product_description.setText("");
            add_product_category.setValue(null);
        }
    }

    /**
     * Validates the input for updating a product.
     * It checks if the ID, price, and amount fields have valid formats and restricts the input accordingly.
     */
    private void updateProduct() {
        update_product_id.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                update_product_id.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        update_product_price.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("(\\d)*(\\.\\d{0,2})?")) {
                update_product_price.setText(oldValue);
            }
        });

        update_product_amount.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("\\d*")) {
                update_product_amount.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        update_product_id.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                boolean found = false;
                for (Item item : inventoryList) {
                    if (item.getId_produktu().equals(newValue)) {
                        update_product_name.setDisable(false);
                        update_product_mark.setDisable(false);
                        update_product_category.setDisable(false);
                        update_product_link_img.setDisable(false);
                        update_product_description.setDisable(false);
                        update_product_price.setDisable(false);
                        update_product_amount.setDisable(false);
                        update_product_button.setDisable(false);

                        update_product_name.setText(item.getNazwa());
                        update_product_mark.setText(item.getMarka());
                        update_product_category.setValue(item.getKategoria().toString());
                        update_product_link_img.setText(item.getZdjecie());
                        update_product_description.setText(item.getOpis());
                        update_product_price.setText(item.getCena());
                        update_product_amount.setText(item.getIlosc());

                        found = true;
                        break;
                    }
                }
                if (!found) {
                    update_product_name.setDisable(true);
                    update_product_mark.setDisable(true);
                    update_product_category.setDisable(true);
                    update_product_link_img.setDisable(true);
                    update_product_description.setDisable(true);
                    update_product_price.setDisable(true);
                    update_product_amount.setDisable(true);
                    update_product_button.setDisable(true);
                }
            } else {
                update_product_name.setDisable(true);
                update_product_mark.setDisable(true);
                update_product_category.setDisable(true);
                update_product_link_img.setDisable(true);
                update_product_description.setDisable(true);
                update_product_price.setDisable(true);
                update_product_amount.setDisable(true);
                update_product_button.setDisable(true);
            }
        });
    }

    /**
     * Handles the event when the "Update Product" button is clicked.
     * It validates the input fields, updates the product in the database,
     * displays a success message, updates the displayed list of products,
     * and clears the input fields.
     */
    public void onClickUpdateProduct() {
        if (update_product_id.getText().isEmpty() || update_product_name.getText().isEmpty() || update_product_mark.getText().isEmpty()
                || update_product_link_img.getText().isEmpty() || update_product_description.getText().isEmpty()
                || update_product_price.getText().isEmpty() || update_product_amount.getText().isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Puste pola");
            alert.setHeaderText(null);
            alert.setContentText("Wypełnij wszystkie pola!");
            alert.showAndWait();
        } else {
            String query = "UPDATE psm_computer_store.produkty SET nazwa='"+update_product_name.getText()+"', marka='"+update_product_mark.getText()+"', " +
                    "cena='"+update_product_price.getText()+"', zdjecie='"+update_product_link_img.getText()+"', ilosc='"+update_product_amount.getText()+"', " +
                    "opis='"+update_product_description.getText()+"', kategoria='"+update_product_category.getValue()+"' WHERE id_produktu='"+update_product_id.getText()+"';";

            update(query);

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Zaktualizowano produkt");
            alert.setHeaderText(null);
            alert.setContentText("Pomyślnie zaktualizowano dane produktu "+update_product_name.getText()+"!");
            alert.showAndWait();

            showItems();

            update_product_id.setText("");
            update_product_name.setText("");
            update_product_mark.setText("");
            update_product_category.setValue("");
            update_product_link_img.setText("");
            update_product_description.setText("");
            update_product_price.setText("");
            update_product_amount.setText("");
        }
    }

    /**
     * Handles the event when the "Delete Product" button is clicked.
     * It validates the input field, prompts for confirmation, deletes the product from the database,
     * displays a success message, and updates the displayed list of products.
     */
    public void onClickDeleteProduct() {
        if(!delete_product_id.getText().isEmpty()) {
            boolean found = false;

            for (Item item : inventoryList) {
                if(item.getId_produktu().equals(delete_product_id.getText())) {

                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Usuwanie produktu");
                    alert.setHeaderText(null);
                    alert.setContentText("Czy na pewno chcesz usunąć produkt: "+item.getNazwa()+"?");
                    Optional<ButtonType> optional = alert.showAndWait();

                    if(optional.get().equals(ButtonType.OK)) {
                        String query = "DELETE FROM psm_computer_store.produkty WHERE id_produktu='"+delete_product_id.getText()+"';";

                        update(query);

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Usunięto produkt");
                        alert.setHeaderText(null);
                        alert.setContentText("Poprawnie usunięto produkt "+item.getNazwa()+"!");
                        alert.showAndWait();

                        showItems();
                    }

                    found = true;
                    break;
                }
            }
            if(!found) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Niepoprawne id");
                alert.setHeaderText(null);
                alert.setContentText("Nie ma produktu z takim id w bazie!");
                alert.showAndWait();
            }
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Puste pole");
            alert.setHeaderText(null);
            alert.setContentText("Podaj id produktu, który chcesz usunąć!");
            alert.showAndWait();
        }
    }

    /**
     * Displays various information on the dashboard using multiple threads to improve performance.
     * It creates a task that performs several database queries concurrently using thread pooling.
     * The results are then used to update the UI components on the dashboard using the JavaFX Platform.runLater() method.
     */
    private void dashboardDisplayInfo() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ExecutorService executor = Executors.newFixedThreadPool(6);

                Future<Integer> noUsersFuture = executor.submit(() -> {
                    ResultSet resultNoUsers = select("SELECT COUNT(id_osoby) AS count_users FROM psm_computer_store.osoby WHERE rola like 'klient'");
                    return resultNoUsers.next() ? resultNoUsers.getInt("count_users") : 0;
                });

                Future<Double> todayRevenuesFuture = executor.submit(() -> {
                    ResultSet resultTodayRevenues = select("SELECT SUM(kwota) AS today_revenues FROM psm_computer_store.zamowienia WHERE data_zlozenia_zamowienia = CURRENT_DATE;");
                    return resultTodayRevenues.next() ? resultTodayRevenues.getDouble("today_revenues") : 0.0;
                });

                Future<Double> monthlyRevenuesFuture = executor.submit(() -> {
                    ResultSet resultMonthlyRevenues = select("SELECT SUM(kwota) AS monthly_revenues FROM psm_computer_store.zamowienia WHERE data_zlozenia_zamowienia >= CURRENT_DATE - INTERVAL '1 month'");
                    return resultMonthlyRevenues.next() ? resultMonthlyRevenues.getDouble("monthly_revenues") : 0.0;
                });

                Future<Integer> noProductsFuture = executor.submit(() -> {
                    ResultSet resultNoProducts = select("SELECT SUM(ilosc) AS count_products FROM psm_computer_store.zamowione_produkty");
                    return resultNoProducts.next() ? resultNoProducts.getInt("count_products") : 0;
                });

                Future<XYChart.Series> incomeChartFuture = executor.submit(() -> {
                    ResultSet resultIncomeChart = select("SELECT data_zlozenia_zamowienia, SUM(kwota) FROM psm_computer_store.zamowienia GROUP BY data_zlozenia_zamowienia;");
                    XYChart.Series chart = new XYChart.Series<>();

                    while (resultIncomeChart.next()) {
                        chart.getData().add(new XYChart.Data<>(resultIncomeChart.getString(1), resultIncomeChart.getFloat(2)));
                    }
                    return chart;
                });

                Future<XYChart.Series> soldProducts = executor.submit(() -> {
                    ResultSet resultSoldItems = select("SELECT data_zlozenia_zamowienia, COUNT(id_zamowionego_produktu)FROM psm_computer_store.zamowienia csz " +
                            "INNER JOIN psm_computer_store.zamowione_produkty cszp ON csz.id_zamowienia = cszp.id_zamowienia GROUP BY data_zlozenia_zamowienia;");
                    XYChart.Series chart = new XYChart.Series<>();

                    while (resultSoldItems.next()) {
                        chart.getData().add(new XYChart.Data<>(resultSoldItems.getString(1), resultSoldItems.getFloat(2)));
                    }
                    return chart;
                });

                executor.shutdown();

                int noUsers = noUsersFuture.get();
                double todayRevenues = todayRevenuesFuture.get();
                double monthlyRevenues = monthlyRevenuesFuture.get();
                int noProducts = noProductsFuture.get();
                XYChart.Series incomeChart = incomeChartFuture.get();
                XYChart.Series soldProductsChart = soldProducts.get();

                Platform.runLater(() -> {
                    dashboard_users.setText(String.valueOf(noUsers));
                    dashboard_todays_revenues.setText(String.format("%.2f zł", todayRevenues));
                    dashboard_monthly_revenues.setText(String.format("%.2f zł", monthlyRevenues));
                    dashboard_sold_items.setText(String.valueOf(noProducts));
                    dashboard_revenues.getData().add(incomeChart);
                    dashboard_items.getData().add(soldProductsChart);
                });
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

    }

    /**
     * Retrieves personal data for the currently logged-in user from the database and populates the corresponding UI fields.
     * It executes a database query based on the user ID and retrieves the user's information into a User object.
     * The retrieved data is then used to set the values of the UI fields.
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
    }

    /**
     * Retrieves user address data for the currently logged-in user from the database and populates the corresponding UI fields.
     * It executes a database query based on the address ID and retrieves the address information into an Address object.
     * The retrieved data is then used to set the values of the UI fields.
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
    }

    /**
     * Updates the personal data of the currently logged-in user in the database.
     * It constructs an SQL query to update the user's information based on the values entered in the UI fields.
     * After updating the data, it retrieves the updated personal data and displays a success message.
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
     * Updates the address data of the currently logged-in user in the database.
     * It constructs an SQL query to update the user's address information based on the values entered in the UI fields.
     * After updating the data, it retrieves the updated address data and displays a success message.
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
     * Adds a new worker by updating the role of an existing user in the database.
     * It checks if the worker's email field is empty and displays an error message if it is.
     * Otherwise, it constructs an SQL query to update the user's role to "pracownik" based on the entered email.
     * Finally, it displays a success message.
     */
    public void addNewWorker() {
        if(worker_email.getText().equals("")) {

            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Puste pole");
            alert.setHeaderText(null);
            alert.setContentText("Podaj e-mail użytkownika, którego chcesz zatrudnić!");
            alert.showAndWait();

        } else {
            String query = "UPDATE psm_computer_store.osoby SET rola = 'pracownik' WHERE email like '"+worker_email.getText()+"';";
            update(query);

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodano pracownika");
            alert.setHeaderText(null);
            alert.setContentText("Dodano nowego pracownika!");
            alert.showAndWait();
        }
    }

    List<Order> orderItems = new ArrayList<>();

    /**
     * Retrieves a list of orders in "kompletowanie" status from the database.
     * It executes a database query to fetch the orders and creates a list of Order objects to store the data.
     * The list is then returned.
     *
     * @return The list of orders in "kompletowanie" status.
     */
    private List<Order> getOrderProduct() {
        Connect connect = new Connect();

        String query = "SELECT * FROM psm_computer_store.zamowienia WHERE status_zamowienia like 'kompletowanie'";

        ResultSet result = connect.select(query, connect.getConnection());
        orderItems.clear();
        try {
            while (result.next()) {
                orderItems.add(new Order(result.getString(1), result.getString(2), result.getString(3),
                        result.getString(4)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        connect.close();

        return orderItems;
    }

    /**
     * Updates the product view on the UI by dynamically generating and adding ListItems to a GridPane.
     * It retrieves the list of orders and iterates over them to create ListItem objects using a FXMLLoader.
     * The ListItems are then added to the GridPane in a vertical layout.
     */
    public void updateProductView() {
        getOrderProduct();
        grid.getChildren().clear();

        int row = 1 ;

        for (int i = 0; i < orderItems.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("list_item.fxml"));
            HBox hBox;
            try {
                hBox = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ListItemController itemController = fxmlLoader.getController();
            itemController.setData(orderItems.get(i), this);

            grid.add(hBox, 1, row);

            GridPane.setMargin(hBox, new Insets(10));

            hBox.prefWidthProperty().bind(scroll.widthProperty().subtract(40));

            row++;
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
}
