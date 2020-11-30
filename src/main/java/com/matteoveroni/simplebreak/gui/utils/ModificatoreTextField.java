package com.matteoveroni.simplebreak.gui.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModificatoreTextField {

    private static final Logger LOG = LoggerFactory.getLogger(ModificatoreTextField.class);

    public enum TipoTextField {
        SOLONUMERICA, SOLOCARATTERI, ALFANUMERICO;
    }

    public static void settaLunghezzaMassima(TextField textField, int lunghezzaMassima) {
        settaRegoleTesto(textField, null, lunghezzaMassima);
    }

    public static void settaSoloNumeri(TextField textField, int lunghezzaMassima) {
        settaRegoleTesto(textField, TipoTextField.SOLONUMERICA, lunghezzaMassima);
    }

    public static void settaSoloCaratteriAlfabetici(TextField textField, int lunghezzaMassima) {
        settaRegoleTesto(textField, TipoTextField.SOLOCARATTERI, lunghezzaMassima);
    }

    public static void settaRegoleTesto(TextField textField, TipoTextField tipoTextField, int lunghezzaMassima) {
        settaRegoleTesto(textField, tipoTextField, lunghezzaMassima, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static void settaRegoleTesto(TextField textField, TipoTextField tipoTextField, int lunghezzaMassima, int valoreMinimo, int valoreMassimo) {
        textField.setTextFormatter(new TextFormatter<>((TextFormatter.Change change) -> {
            if (change.isAdded()) {
                String testoAttuale = textField.getText();
                String nuovoTestoDaAggiungere = change.getText();
                String testoSelezionato = textField.getSelectedText();
                boolean isTestoDaAggiungereApplicabilePerTipo = true;
                if (tipoTextField != null) {
                    switch (tipoTextField) {
                        case SOLONUMERICA:
                            isTestoDaAggiungereApplicabilePerTipo = isSoloNumeriPresenti(nuovoTestoDaAggiungere);
                            break;
                        case SOLOCARATTERI:
                            isTestoDaAggiungereApplicabilePerTipo = isSoloCaratteriPresenti(nuovoTestoDaAggiungere);
                            break;
                        case ALFANUMERICO:
                            isTestoDaAggiungereApplicabilePerTipo = isSoloNumeriECaratteriPresenti(nuovoTestoDaAggiungere);
                            break;
                    }
                }
                if (!isTestoDaAggiungereApplicabilePerTipo) {
                    return null;
                }
                if (isNuovoTestoDaAggiungereMaggioreDiLunghezzaMassima(testoAttuale, nuovoTestoDaAggiungere, testoSelezionato, lunghezzaMassima)) {
                    return null;
                }
            }
            return change;
        }));

        if (tipoTextField == TipoTextField.SOLONUMERICA && isLimitiLunghezzaImpostati(valoreMinimo, valoreMassimo)) {
            textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValueFocus, Boolean newValueFocus) -> {
                if (newValueFocus == false) {
                    try {
                        if (textField.getText() != null && !textField.getText().trim().isEmpty()) {
                            int valoreDaAggiungere = Integer.parseInt(textField.getText());
                            if (valoreDaAggiungere < valoreMinimo || valoreDaAggiungere > valoreMassimo) {
                                visualizzaErroreTextfield(textField);
                                textField.clear();
                            }
                        }
                    } catch (Exception ex) {
                        textField.clear();
                        visualizzaErroreTextfield(textField);
                        ex.printStackTrace();
                        LOG.error(ex.getMessage());
                    }
                }
            });
        }
    }

    private static boolean isNuovoTestoDaAggiungereMaggioreDiLunghezzaMassima(String testoAttuale, String nuovoTestoDaAggiungere, String testoSelezionato, int lunghezzaMassima) {
        int lunghezzaTestoAttuale = 0;
        int lunghezzaTestoSelezionato = 0;
        int lungezzaNuovoTestoDaAggiungere = 0;

        if (lunghezzaMassima < 0) {
            return false;
        }
        if (testoAttuale != null) {
            lunghezzaTestoAttuale = testoAttuale.length();
        }
        if (nuovoTestoDaAggiungere != null) {
            lungezzaNuovoTestoDaAggiungere = nuovoTestoDaAggiungere.length();
        }
        if (testoSelezionato != null) {
            lunghezzaTestoSelezionato = testoSelezionato.length();
        }
        return (lungezzaNuovoTestoDaAggiungere + (lunghezzaTestoAttuale - lunghezzaTestoSelezionato) > lunghezzaMassima);
    }

    private static boolean isSoloCaratteriPresenti(String stringa) {
        return stringa.matches("[a-zA-Z]*");
    }

    private static boolean isSoloNumeriPresenti(String stringa) {
        return stringa.matches("[0-9]*");
    }

    private static boolean isSoloNumeriECaratteriPresenti(String stringa) {
        return stringa.matches("[a-zA-Z0-9]*");
    }

    private static boolean isLimitiLunghezzaImpostati(int valoreMinimo, int valoreMassimo) {
        return (valoreMinimo > Integer.MIN_VALUE && valoreMassimo < Integer.MAX_VALUE);
    }

    private static void visualizzaErroreTextfield(TextField textField) {
        textField.setStyle(" -fx-focus-color: red; -fx-text-box-border: red; -fx-faint-focus-color: #ff000022; ");
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                v -> rimuoviVisualizzazioniErroreTextField(textField)
        ));
        timeline.play();
    }

    private static void rimuoviVisualizzazioniErroreTextField(TextField textField) {
        textField.setStyle("-fx-text-box-border: ladder(-fx-background, black 10%, derive(-fx-background, -15%) 30%); -fx-faint-focus-color: #039ED322; ");
    }
}

