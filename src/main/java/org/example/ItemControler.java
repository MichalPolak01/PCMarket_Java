package org.example;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ItemControler{

    @FXML
    private ImageView labelImage;

    @FXML
    private Label labelName;

    @FXML
    private Label labelPrice;

    private Item item;
    private ItemListener itemListener;

    /**
     * Handles the click event on the item. Invokes the onClickListener of the itemListener with the associated item.
     *
     * @param mouseEvent The MouseEvent representing the click event.
     */
    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        itemListener.onClickListener(item);
    }

    /**
     * Sets the data for the item in the UI and associates it with the provided itemListener.
     * It populates the labelName with the item's name, asynchronously loads and sets the labelImage with the item's image,
     * and sets the labelPrice with the item's price.
     *
     * @param item         The item to display.
     * @param itemListener The listener for item click events.
     */
    public void setData(Item item, ItemListener itemListener) {
        this.item = item;
        this.itemListener = itemListener;
        labelName.setText(item.getNazwa());

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Image image = new Image(item.getZdjecie());
                Platform.runLater(() -> labelImage.setImage(image));
                return null;
            }
        };

        new Thread(task).start();

        labelPrice.setText(item.getCena());
    }
}
