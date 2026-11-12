package banco.service;

import banco.sessao.Sessao;
import banco.dao.ContaDAO;
import banco.model.ContaVIP;

public class ContaService {

    public void excluirConta(String senhaDigitada) throws Exception {
        if (!senhaDigitada.equals(Sessao.contaLogada.getSenha())) {
            throw new Exception("Senha incorreta.");
        }

        ContaDAO contaDAO = new ContaDAO();
        if (!contaDAO.deletarConta(Sessao.contaLogada.getCpf())) {
            throw new Exception("Erro ao excluir a conta no banco de dados. Tente novamente.");
        }
    }

    public void faturaZerada() throws Exception {
        ContaVIP conta = Sessao.getContaVIP();
        if (conta.getFaturaCredito() == 0) {
            throw new Exception("A fatura está zerada.");
        }
    }

    public void faturaPaga() throws Exception {
        ContaVIP conta = Sessao.getContaVIP();
        if (conta.getFaturaCredito() > 0) {
            throw new Exception("Pague a fatura antes de excluir a conta.");
        }
    }
}