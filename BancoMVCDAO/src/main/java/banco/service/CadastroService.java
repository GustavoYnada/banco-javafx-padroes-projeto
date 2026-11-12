package banco.service;

import banco.factory.ContaFactory;
import banco.model.Conta;
import banco.dao.ContaDAO;

public class CadastroService {

    public void cadastrarConta(String nome, String cpf, String senha, String confirmacao, boolean isVIP)
            throws Exception {
        cpf = Conta.tratamentoCpf(cpf);

        if (nome.isEmpty()) {
            throw new Exception("Informe o nome!");
        }
        if (nome.length() > 64) {
            throw new Exception("Nome até 64 caracteres!");
        }
        if (!Conta.validarCpf(cpf)) {
            throw new Exception("CPF inválido!");
        }
        if (senha.length() < 4) {
            throw new Exception("Senha de no mínimo 4 caracteres!");
        }
        if (senha.length() > 26) {
            throw new Exception("Senha de no máximo 26 caracteres!");
        }
        if (!Conta.validarSenha(senha, confirmacao)) {
            throw new Exception("Senhas diferentes!");
        }
        ContaDAO contaDAO = new ContaDAO();
        if (contaDAO.buscarContaPorCPF(cpf) != null) {
            throw new Exception("CPF já cadastrado!");
        }

        Conta novaConta = ContaFactory.obter(isVIP).criarConta(nome, cpf, senha);

        if (!contaDAO.cadastrarConta(novaConta)) {
            throw new Exception("Erro ao cadastrar. Tente novamente!");
        }
    }
}