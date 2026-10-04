package com.project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.InputStream;

public class ControllerListItem
        extends ListCell<Controller.NintendoItem> {

    private final ImageView imageView =
            new ImageView();

    private final HBox box =
            new HBox(15);


    public ControllerListItem() {

        box.setAlignment(
                Pos.CENTER_LEFT
        );

        box.setPadding(
                new Insets(5)
        );


        imageView.setFitWidth(75);
        imageView.setFitHeight(65);

        imageView.setPreserveRatio(true);


        box.getChildren().add(
                imageView
        );
    }


    @Override
    protected void updateItem(
            Controller.NintendoItem item,
            boolean empty
    ) {

        super.updateItem(
                item,
                empty
        );


        if (empty || item == null) {

            setText(null);
            setGraphic(null);

            return;
        }


        setText(item.name);


        setStyle("""
                -fx-font-size: 18px;
                """);


        imageView.setImage(null);


        if (
                item.image != null
                        &&
                !item.image.isBlank()
        ) {

            InputStream stream =
                    getClass()
                            .getResourceAsStream(
                                    "/assets/data/images/"
                                            + item.image
                            );


            if (stream != null) {

                imageView.setImage(
                        new Image(stream)
                );
            }
        }


        setGraphic(box);
    }
}