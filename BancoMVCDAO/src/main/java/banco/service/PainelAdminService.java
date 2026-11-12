package banco.service;

import banco.sessao.Sessao;
import banco.dao.AdminDAO;
import banco.dao.ContaDAO;
import banco.model.Admin;
import banco.model.Conta;

public class PainelAdminService {
    public String depositarSaldo(String cpf, double valor) throws Exception {
        cpf = Conta.tratamentoCpf(cpf);

        if (valor <= 0) {
            throw new Exception("Informe um valor válido");
        }
        ContaDAO contaDAO = new ContaDAO();
        Conta conta = contaDAO.buscarContaPorCPF(cpf);
        if (conta == null) {
            throw new Exception("Conta não encontrada");
        }
        if (!Sessao.adminLogado.depositarConta(valor, conta) || !contaDAO.atualizar(conta)) {
            throw new Exception("Erro ao adicionar saldo");
        }
        return conta.getNome();
    }

    public void cadastrarAdmin(String token, String senha) throws Exception {
        if (token.isEmpty() || token.length() > 30) {
            throw new Exception("Token de 1 a 30 caracteres");
        }
        if (senha.length() < 4 || senha.length() > 26) {
            throw new Exception("Senha limitada de 4 a 26 caracteres");
        }

        AdminDAO adminDAO = new AdminDAO();
        if (adminDAO.buscarAdministradorPorToken(token) != null) {
            throw new Exception("Esse token já existe");
        }
        if (!adminDAO.cadastrarAdministrador(new Admin(token, senha))) {
            throw new Exception("Erro ao criar administrador");
        }
    }
}
