package com.project;

import javafx.scene.control.Label;

public class UtilsViews {

    public static Label createTitle(String text) {

        Label label = new Label(text);

        label.setStyle("""
                -fx-font-size: 30px;
                -fx-font-weight: bold;
                """);

        return label;
    }

    public static Label createDescription(String text) {

        Label label = new Label(text);

        label.setWrapText(true);

        label.setStyle("""
                -fx-font-size: 18px;
                """);

        return label;
    }
}