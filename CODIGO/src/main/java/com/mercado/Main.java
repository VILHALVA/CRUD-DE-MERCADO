package com.mercado;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private TableView<Tabela> table;
    private TextField txtNome, txtPreco;
    private MercadoDAO mercadoDAO;

    @Override
    public void start(Stage primaryStage) {
        mercadoDAO = new MercadoDAO();

        VBox root = new VBox(10);
        root.setPadding(new javafx.geometry.Insets(10));

        table = new TableView<>();
        TableColumn<Tabela, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<Tabela, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));

        TableColumn<Tabela, String> colPreco = new TableColumn<>("Preço");
        colPreco.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(String.valueOf(data.getValue().getPreco())));

        table.getColumns().addAll(colId, colNome, colPreco);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        txtNome = new TextField();
        txtPreco = new TextField();

        form.add(new Label("NOME:"), 0, 0);
        form.add(txtNome, 1, 0);
        form.add(new Label("PREÇO:"), 0, 1);
        form.add(txtPreco, 1, 1);

        Button btnAdd = new Button("ADICIONAR");
        btnAdd.setOnAction(e -> addProduto());

        Button btnUpdate = new Button("ATUALIZAR");
        btnUpdate.setOnAction(e -> updateProduto());

        Button btnDelete = new Button("APAGAR");
        btnDelete.setOnAction(e -> deleteProduto());

        form.add(btnAdd, 0, 2);
        form.add(btnUpdate, 1, 2);
        form.add(btnDelete, 2, 2);

        root.getChildren().addAll(form, table);

        loadProdutos();

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("CRUD DE MERCADO");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadProdutos() {
        ObservableList<Tabela> produtos = FXCollections.observableArrayList(mercadoDAO.getAllProdutos());
        table.setItems(produtos);
    }

    private void addProduto() {
        String nome = txtNome.getText();
        double preco = Double.parseDouble(txtPreco.getText());
        mercadoDAO.addProduto(new Tabela(0, nome, preco));
        loadProdutos();
    }

    private void updateProduto() {
        Tabela selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setNome(txtNome.getText());
            selected.setPreco(Double.parseDouble(txtPreco.getText()));
            mercadoDAO.updateProduto(selected);
            loadProdutos();
        }
    }

    private void deleteProduto() {
        Tabela selected = table.getSelectionModel().getSelectedItem();
        if (selected != null) {
            mercadoDAO.deleteProduto(selected.getId());
            loadProdutos();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
