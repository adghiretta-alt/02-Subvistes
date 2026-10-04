package com.project;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private BorderPane root;

    private ComboBox<String> categoryCombo;

    private ListView<NintendoItem> listView;

    private ImageView detailImage;
    private Label detailTitle;
    private Label detailInfo;

    private String currentCategory = "Jocs";

    private final List<NintendoItem> items = new ArrayList<>();




    public BorderPane createView() {

        root = new BorderPane();

        root.setStyle("-fx-background-color: white;");

        // CABECERA
        Label header = new Label("Nintendo DB");

        header.setMaxWidth(Double.MAX_VALUE);
        header.setAlignment(Pos.CENTER);

        header.setStyle("""
                -fx-background-color: #4b9bea;
                -fx-font-size: 30px;
                -fx-padding: 25px;
                """);

        root.setTop(header);

        crearVistaDesktop();

        cargarDatos("Jocs");

        return root;
    }



    private void crearVistaDesktop() {


        VBox leftPanel = new VBox(15);

        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(380);

        categoryCombo = new ComboBox<>();

        categoryCombo.getItems().addAll(
                "Personatges",
                "Jocs",
                "Consoles"
        );

        categoryCombo.setValue(currentCategory);

        categoryCombo.setMaxWidth(Double.MAX_VALUE);

        categoryCombo.setOnAction(event -> {

            currentCategory =
                    categoryCombo.getValue();

            cargarDatos(currentCategory);
        });


        listView = new ListView<>();

        listView.setCellFactory(
                param -> new ControllerListItem()
        );

        listView.setOnMouseClicked(event -> {

            NintendoItem item =
                    listView.getSelectionModel()
                            .getSelectedItem();

            if (item != null) {

                mostrarDetalle(item);
            }
        });

        VBox.setVgrow(
                listView,
                Priority.ALWAYS
        );

        leftPanel.getChildren().addAll(
                categoryCombo,
                listView
        );

        root.setLeft(leftPanel);


  

        VBox rightPanel = new VBox(25);

        rightPanel.setAlignment(
                Pos.TOP_CENTER
        );

        rightPanel.setPadding(
                new Insets(50)
        );


        detailImage = new ImageView();

        detailImage.setFitWidth(350);
        detailImage.setFitHeight(300);
        detailImage.setPreserveRatio(true);


        detailTitle =
                UtilsViews.createTitle("Nintendo DB");


        detailInfo =
                UtilsViews.createDescription("");


        rightPanel.getChildren().addAll(
                detailImage,
                detailTitle,
                detailInfo
        );

        root.setCenter(rightPanel);
    }



    public void updateResponsiveView(
            double width
    ) {

        if (width < 700) {

            crearVistaMobile();

        } else {

            crearVistaDesktop();

            cargarDatos(currentCategory);
        }
    }


  

    private void crearVistaMobile() {

        VBox menu = new VBox();

        menu.setStyle(
                "-fx-background-color: white;"
        );


        crearBotonCategoria(
                menu,
                "Personatges"
        );

        crearBotonCategoria(
                menu,
                "Jocs"
        );

        crearBotonCategoria(
                menu,
                "Consoles"
        );


        root.setLeft(null);

        root.setCenter(menu);
    }


    private void crearBotonCategoria(
            VBox container,
            String categoria
    ) {

        Button button =
                new Button(categoria);

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.setAlignment(
                Pos.CENTER_LEFT
        );

        button.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #dddddd;
                -fx-border-width: 0 0 1 0;
                -fx-font-size: 20px;
                -fx-padding: 20px;
                """);


        button.setOnAction(event -> {

            currentCategory = categoria;

            mostrarListaMobile(categoria);
        });


        container.getChildren().add(button);
    }




    private void cargarDatos(
            String categoria
    ) {

        items.clear();


        String archivo;

        switch (categoria) {

            case "Personatges":
                archivo = "characters.json";
                break;

            case "Consoles":
                archivo = "consoles.json";
                break;

            default:
                archivo = "games.json";
                break;
        }


        try {

            InputStream input =
                    getClass().getResourceAsStream(
                            "/assets/data/" + archivo
                    );


            if (input == null) {

                System.out.println(
                        "No se encuentra: "
                                + archivo
                );

                return;
            }


            String json =
                    new String(
                            input.readAllBytes(),
                            StandardCharsets.UTF_8
                    );


            JsonArray array =
                    JsonParser
                            .parseString(json)
                            .getAsJsonArray();


            for (JsonElement element : array) {

                JsonObject obj =
                        element.getAsJsonObject();


                NintendoItem item =
                        new NintendoItem();


                // DATOS COMUNES

                item.name =
                        getString(
                                obj,
                                "name"
                        );

                item.image =
                        getString(
                                obj,
                                "image"
                        );


                // -------------------------
                // JUEGOS
                // -------------------------

                if (categoria.equals("Jocs")) {

                    item.year =
                            getString(
                                    obj,
                                    "year"
                            );

                    item.type =
                            getString(
                                    obj,
                                    "type"
                            );

                    item.plot =
                            getString(
                                    obj,
                                    "plot"
                            );
                }


                // -------------------------
                // CONSOLAS
                // -------------------------

                if (categoria.equals("Consoles")) {

                    item.date =
                            getString(
                                    obj,
                                    "date"
                            );

                    item.procesador =
                            getString(
                                    obj,
                                    "procesador"
                            );

                    item.color =
                            getString(
                                    obj,
                                    "color"
                            );

                    item.unitsSold =
                            getString(
                                    obj,
                                    "units_sold"
                            );
                }


                // -------------------------
                // PERSONAJES
                // -------------------------

                if (categoria.equals(
                        "Personatges"
                )) {

                    // Lo dejamos preparado
                    // para los campos de
                    // characters.json

                    item.description =
                            getString(
                                    obj,
                                    "description"
                            );
                }


                items.add(item);
            }


            if (listView != null) {

                listView
                        .getItems()
                        .setAll(items);


                if (!items.isEmpty()) {

                    listView
                            .getSelectionModel()
                            .select(0);

                    mostrarDetalle(
                            items.get(0)
                    );
                }
            }


        } catch (Exception e) {

            e.printStackTrace();
        }
    }




    private String getString(
            JsonObject object,
            String campo
    ) {

        if (
                object.has(campo)
                        &&
                !object.get(campo)
                        .isJsonNull()
        ) {

            return object
                    .get(campo)
                    .getAsString();
        }

        return "";
    }



    private void mostrarDetalle(
            NintendoItem item
    ) {

        detailTitle.setText(
                item.name
        );


        String texto;


        if (currentCategory.equals(
                "Jocs"
        )) {

            texto =
                    "Any: "
                            + item.year
                            + "\n\n"
                            + "Tipus: "
                            + item.type
                            + "\n\n"
                            + item.plot;
        }

        else if (
                currentCategory.equals(
                        "Consoles"
                )
        ) {

            texto =
                    "Data: "
                            + item.date
                            + "\n\n"
                            + "Processador: "
                            + item.procesador
                            + "\n\n"
                            + "Color: "
                            + item.color
                            + "\n\n"
                            + "Unitats venudes: "
                            + item.unitsSold;
        }

        else {

            texto =
                    item.description;
        }


        detailInfo.setText(texto);


        // IMAGEN

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

                detailImage.setImage(
                        new Image(stream)
                );
            }
        }
    }




    private void mostrarListaMobile(
            String categoria
    ) {

        cargarDatos(categoria);


        VBox container =
                new VBox(10);

        container.setPadding(
                new Insets(15)
        );


        Label title =
                UtilsViews.createTitle(
                        categoria
                );


        ListView<NintendoItem> lista =
                new ListView<>();


        lista.getItems().setAll(items);


        lista.setCellFactory(
                param ->
                        new ControllerListItem()
        );


        lista.setOnMouseClicked(event -> {

            NintendoItem item =
                    lista.getSelectionModel()
                            .getSelectedItem();

            if (item != null) {

                mostrarDetalleMobile(item);
            }
        });


        VBox.setVgrow(
                lista,
                Priority.ALWAYS
        );


        container.getChildren().addAll(
                title,
                lista
        );


        root.setCenter(container);
    }



    private void mostrarDetalleMobile(
            NintendoItem item
    ) {

        VBox container =
                new VBox(20);

        container.setAlignment(
                Pos.TOP_CENTER
        );

        container.setPadding(
                new Insets(20)
        );


        Button back =
                new Button("← Tornar");


        back.setOnAction(event ->
                mostrarListaMobile(
                        currentCategory
                )
        );


        ImageView image =
                new ImageView();


        image.setFitWidth(300);
        image.setFitHeight(300);
        image.setPreserveRatio(true);


        InputStream stream =
                getClass()
                        .getResourceAsStream(
                                "/assets/data/images/"
                                        + item.image
                        );


        if (stream != null) {

            image.setImage(
                    new Image(stream)
            );
        }


        Label name =
                UtilsViews.createTitle(
                        item.name
                );


        Label info =
                UtilsViews.createDescription(
                        obtenerInformacion(item)
                );


        container.getChildren().addAll(
                back,
                image,
                name,
                info
        );


        root.setCenter(container);
    }




    private String obtenerInformacion(
            NintendoItem item
    ) {

        if (currentCategory.equals(
                "Jocs"
        )) {

            return
                    "Any: " + item.year
                            + "\n\n"
                            + "Tipus: " + item.type
                            + "\n\n"
                            + item.plot;
        }


        if (currentCategory.equals(
                "Consoles"
        )) {

            return
                    "Data: " + item.date
                            + "\n\n"
                            + "Processador: "
                            + item.procesador
                            + "\n\n"
                            + "Color: "
                            + item.color
                            + "\n\n"
                            + "Unitats venudes: "
                            + item.unitsSold;
        }


        return item.description;
    }



    public static class NintendoItem {

        String name;

        String image;

        // Jocs
        String year;
        String type;
        String plot;

        // Consoles
        String date;
        String procesador;
        String color;
        String unitsSold;

        // Personatges
        String description;
    }
}