package org.example;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;

import static javafx.collections.FXCollections.observableArrayList;

public class ListItemController {

    @FXML
    private TableColumn<?, ?> amount;

    @FXML
    private TableColumn<?, ?> name;

    @FXML
    private TableView<OrderItem> table;

    Alert alert;

    private Order order;

    String orderID;

    private AdminMainPageController adminMainPageController;

    /**
     * Sets the data for the ListItemController.
     *
     * @param order                   The order to display.
     * @param adminMainPageController The controller for the admin main page.
     */
    public void setData(Order order, AdminMainPageController adminMainPageController) {
        this.order =order;
        this.adminMainPageController = adminMainPageController;

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                orderID = order.getId_zamowienia();
                ObservableList<OrderItem> shoppingList = getShoppingList();
                name.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
                amount.setCellValueFactory(new PropertyValueFactory<>("ilosc"));

                table.getItems().clear();
                table.setItems(shoppingList);

                return null;
            }
        };

        new Thread(task).start();
    }

    /**
     * Marks the order as sent in the database and updates the view.
     */
    public void sentOrder() {
        String query = "UPDATE psm_computer_store.zamowienia SET status_zamowienia = 'wysłane' WHERE id_zamowienia ='"+orderID+"';";

        update(query);

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wysłano produkt");
        alert.setHeaderText(null);
        alert.setContentText("Poprawnie potwierdzono wysłanie produktu!");
        alert.showAndWait();

        adminMainPageController.updateProductView();
    }

    /**
     * Retrieves the list of ordered items for a specific order from the database.
     *
     * @return The list of ordered items as an ObservableList<OrderItem>.
     */
    private ObservableList<OrderItem> getShoppingList() {
        String query = "SELECT id_zamowionego_produktu, id_zamowienia, zp.id_produktu, zp.ilosc, nazwa FROM psm_computer_store.zamowione_produkty zp " +
                "INNER JOIN psm_computer_store.produkty pp ON zp.id_produktu::int = pp.id_produktu WHERE id_zamowienia='"+orderID+"';";

        ResultSet result = select(query);
        ObservableList<OrderItem> items = observableArrayList();

        try {
            while (result.next()) {
                items.add(new OrderItem(result.getString(1), result.getString(2), result.getString(3),
                        result.getString(4), result.getString(5)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
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
