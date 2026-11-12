package banco.service;

import banco.sessao.Sessao;
import banco.dao.AdminDAO;
import banco.dao.ContaDAO;
import banco.model.Admin;
import banco.model.Conta;
import banco.model.ContaVIP;

public class LoginService {
    public String autenticarUsuario(String login, String senha) throws Exception{
        AdminDAO adminDAO = new AdminDAO();
        Admin admin = adminDAO.buscarAdministradorPorToken(login);
        if (admin != null && admin.getSenha().equals(senha)) {
            Sessao.adminLogado = admin;
            Sessao.contaLogada = null;
            return "ADM";
        }
        ContaDAO contaDAO = new ContaDAO();
        login = Conta.tratamentoCpf(login);
        Conta conta = contaDAO.buscarContaPorCPF(login);
        if (conta != null && conta.getSenha().equals(senha)) {
            Sessao.adminLogado = null;
            Sessao.contaLogada = conta;

            if (conta instanceof ContaVIP) {
                return "VIP";
            } else {
                return "NORMAL";
            }
        }
        throw new Exception("Login ou senha inválidos.");
    }
}
