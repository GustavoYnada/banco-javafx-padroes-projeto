package banco.dao;
import banco.model.Admin;

import java.sql.*;

public class AdminDAO {
    public boolean cadastrarAdministrador(Admin a){
        String sql = "INSERT INTO admin (token, senha) " +
                "VALUES (?, ?)";

        try (Connection con = Conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getToken());
            ps.setString(2, a.getSenha());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro no cadastro de Administrador: " + e.getMessage());
            return false;
        }
    }

    public Admin buscarAdministradorPorToken(String token){
        String sql = "SELECT token, senha FROM admin WHERE token = ?";

        try(Connection con = Conexao.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)){
            ps.setString(1, token);

            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return new Admin(token, rs.getString("senha"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro na busca: " + e.getMessage());
        }
        return null;
    }
}
