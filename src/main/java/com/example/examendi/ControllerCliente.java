package com.example.examendi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ControllerCliente {
    @javafx.fxml.FXML
    private TextField tfNombre;
    @javafx.fxml.FXML
    private TextField tfTalla;
    @javafx.fxml.FXML
    private TextField tfObservaciones;
    @javafx.fxml.FXML
    private TextField tfPeso;
    @javafx.fxml.FXML
    private Button butonGuardar;
    @javafx.fxml.FXML
    private ChoiceBox<String> cbActividad;
    @javafx.fxml.FXML
    private ChoiceBox<String> cbSexo;
    @javafx.fxml.FXML
    private Label lblInfo;
    @javafx.fxml.FXML
    private Button butonDescargar;
    @javafx.fxml.FXML
    private TextField tfEdad;


    @javafx.fxml.FXML
    public void initialize() {
        // Valores para los chBox
        ObservableList<String> datos = FXCollections.observableArrayList();
        datos.addAll("Masculino", "Femenino");
        cbSexo.setItems(datos);

        ObservableList<String> tipos = FXCollections.observableArrayList();
        tipos.addAll("Muy Activo","Activo", "Moderado", "Sedentario");
        cbActividad.setItems(tipos);
    }

    @javafx.fxml.FXML
    public void Guardar(ActionEvent actionEvent) {
        if (tfNombre.getText().isEmpty() || cbSexo.getValue() == null || tfPeso.getText().isEmpty() ||
                tfTalla.getText().isEmpty() || tfEdad.getText().isEmpty() || cbActividad.getValue() == null ||
                tfObservaciones.getText().isEmpty()) {
            lblInfo.setText("Error, no ha introducido todos los campos");
        } else {
            String nombreCliente = tfNombre.getText();
            String sexoCliente = cbSexo.getValue().toString();
            Double pesoCliente = Double.parseDouble(tfPeso.getText());
            Double tallaCliente = Double.parseDouble(tfTalla.getText());
            Integer edadCliente = Integer.valueOf(tfEdad.getText());
            String tipoActividad = cbActividad.getValue().toString();
            Double metabolismoBasal; //GER
            Double metatabolismoBasalEnergetico; //GET

            // Primer calculo segun el sexo
            if (sexoCliente == "Masculino") {
                metabolismoBasal = 66.473 + 13.751 * pesoCliente + 5.0033 * tallaCliente - 6.755 * edadCliente;
                if (tipoActividad == "Sedentario") {
                    metatabolismoBasalEnergetico = metabolismoBasal * 1.3;
                } else if (tipoActividad == "Moderado") {
                    metatabolismoBasalEnergetico = metabolismoBasal * 1.6;
                } else if (tipoActividad == "Activo") {
                    metatabolismoBasalEnergetico = metabolismoBasal * 1.7;
                } else {
                    metatabolismoBasalEnergetico = metabolismoBasal * 2.1;
                }
            } else {
                metabolismoBasal = 655.0955 + 9.463 * pesoCliente + 1.8496 * tallaCliente - 4.6756 * edadCliente;

                if (tipoActividad == "Sedentario") {
                    metatabolismoBasalEnergetico = metabolismoBasal * 1.3;
                } else if (tipoActividad == "Moderado") {
                    metatabolismoBasalEnergetico = metabolismoBasal * 1.5;
                } else if (tipoActividad == "Activo") {
                    metatabolismoBasalEnergetico = metabolismoBasal * 1.6;
                } else {
                    metatabolismoBasalEnergetico = metabolismoBasal * 1.9;
                }
            }

            lblInfo.setText("El cliente " + nombreCliente + " tiene un GER de " + metabolismoBasal + " y un GET de " + metatabolismoBasalEnergetico);
        }
    }


    @javafx.fxml.FXML
    public void Descargar(ActionEvent actionEvent) throws SQLException, JRException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/exament2di", "root", "");
        JasperPrint jasperPrint = JasperFillManager.fillReport("ExamenDI.jasper", null, connection);

        // Mostrar el informe en una ventana
        JasperViewer.viewReport(jasperPrint, false);

        JRPdfExporter exp = new JRPdfExporter();
        exp.setExporterInput(new SimpleExporterInput(jasperPrint));
        exp.setExporterOutput(new SimpleOutputStreamExporterOutput("ExamenDI.pdf"));
        exp.setConfiguration(new SimplePdfExporterConfiguration());
        exp.exportReport();
    }
}