package com.mercado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MercadoDAO {
    private static final String URL = "jdbc:sqlite:DATABASE.db";
    private Connection connection;

    public MercadoDAO() {
        try {
            connection = DriverManager.getConnection(URL);
            createTableIfNotExists();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTableIfNotExists() {
        String sql = "CREATE TABLE IF NOT EXISTS mercadorias (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "nome TEXT NOT NULL," +
                     "preço FLOAT)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addProduto(Tabela produto) {
        String sql = "INSERT INTO mercadorias (nome, preço) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tabela> getAllProdutos() {
        List<Tabela> produtos = new ArrayList<>();
        String sql = "SELECT * FROM mercadorias";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                produtos.add(new Tabela(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preço")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produtos;
    }

    public void updateProduto(Tabela produto) {
        String sql = "UPDATE mercadorias SET nome = ?, preço = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, produto.getNome());
            stmt.setDouble(2, produto.getPreco());
            stmt.setInt(3, produto.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduto(int id) {
        String sql = "DELETE FROM mercadorias WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
