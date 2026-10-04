+package com.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        Controller controller =
                new Controller();


        BorderPane root =
                controller.createView();


        Scene scene =
                new Scene(
                        root,
                        1200,
                        750
                );


        stage.setTitle(
                "Nintendo DB"
        );

        stage.setScene(scene);

        stage.setMinWidth(400);
        stage.setMinHeight(500);

        stage.show();


        // RESPONSIVE

        controller.updateResponsiveView(
                stage.getWidth()
        );


        stage.widthProperty()
                .addListener(
                        (obs, oldValue, newValue) -> {

                            controller
                                    .updateResponsiveView(
                                            newValue
                                                    .doubleValue()
                                    );
                        }
                );
    }


    public static void main(
            String[] args
    ) {

        launch(args);
    }
}