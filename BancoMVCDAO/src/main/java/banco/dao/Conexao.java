package banco.dao;

import java.sql.*;

public class Conexao {
    private static final String URL  = "jdbc:postgresql://localhost:5432/banco";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void criarTabelas() {

        String sqlAdmin = """
                CREATE TABLE IF NOT EXISTS admin(
                	token VARCHAR(30) PRIMARY KEY,
                	senha VARCHAR(26) NOT NULL
                ); """;

        String sqlConta = """            
                CREATE TABLE IF NOT EXISTS conta(
                	nome VARCHAR(64) NOT NULL,
                	cpf VARCHAR(11) PRIMARY KEY,
                	senha VARCHAR(26) NOT NULL,
                	vip BOOLEAN NOT NULL DEFAULT FALSE,
                	saldo NUMERIC(16,2) NOT NULL,
                	limite_credito NUMERIC(16,2) DEFAULT 0.0 NOT NULL,
                	fatura_credito NUMERIC(16,2) DEFAULT 0.0 NOT NULL,
                	CHECK(length(cpf) = 11),
                	CHECK (saldo >= 0),
                	CHECK (limite_credito >= 0),
                	CHECK (fatura_credito >= 0),
                	CHECK (fatura_credito <= limite_credito)
                ); """;

        try (Connection con = getConnection();
             Statement st = con.createStatement()) {
             st.execute(sqlAdmin);
             st.execute(sqlConta);

            System.out.println("Tabelas Admin e Conta criadas.");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }
}