package banco.dao;

import banco.factory.ContaFactory;
import banco.model.Conta;
import banco.model.ContaVIP;

import java.sql.*;

public class ContaDAO {
    public boolean cadastrarConta(Conta c){
        String sql = "INSERT INTO conta (nome, cpf, senha, vip, saldo, limite_credito, fatura_credito) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNome());
            ps.setString(2, c.getCpf());
            ps.setString(3, c.getSenha());
            ps.setDouble(5, c.getSaldo());
            if (c instanceof ContaVIP) {
                ContaVIP v = (ContaVIP) c;
                ps.setBoolean(4, true);
                ps.setDouble(6, v.getLimiteCredito());
                ps.setDouble(7, v.getFaturaCredito());
            } else{
                ps.setBoolean(4, false);
                ps.setDouble(6, 0.0);
                ps.setDouble(7, 0.0);
            }

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro no cadastro: " + e.getMessage());
            return false;
        }
    }

    public Conta buscarContaPorCPF(String cpf){
        String sql = "SELECT nome, senha, vip, saldo, limite_credito, fatura_credito FROM conta WHERE cpf = ?";
        cpf = Conta.tratamentoCpf(cpf);

        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    boolean isVip = rs.getBoolean("vip");
                    return ContaFactory.obter(isVip).reconstituirConta(
                            rs.getString("nome"), cpf, rs.getString("senha"),
                            rs.getDouble("saldo"), rs.getDouble("limite_credito"),
                            rs.getDouble("fatura_credito"));
                }
            }

        } catch (SQLException e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return null;
    }

    public boolean atualizarSaldo(String cpf, double novoSaldo, double novaFatura) {
        String sql = "UPDATE conta SET saldo = ?, fatura_credito = ? WHERE cpf = ?";

        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, novoSaldo);
            ps.setDouble(2, novaFatura);
            ps.setString(3, cpf);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar saldo: " + e.getMessage());
            return false;
        }
    }

    public boolean deletarConta(String cpf) {
        String sql = "DELETE FROM conta WHERE cpf = ?";
        cpf = Conta.tratamentoCpf(cpf);

        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, cpf);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao deletar conta: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizar(Conta c) {
        double fatura = (c instanceof ContaVIP) ? ((ContaVIP) c).getFaturaCredito() : 0.0;
        return atualizarSaldo(c.getCpf(), c.getSaldo(), fatura);
    }
}