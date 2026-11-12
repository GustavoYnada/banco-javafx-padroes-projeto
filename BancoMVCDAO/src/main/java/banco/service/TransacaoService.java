package banco.service;

import banco.sessao.Sessao;
import banco.dao.ContaDAO;
import banco.model.Conta;
import banco.model.ContaVIP;

public class TransacaoService {
    public void validarSaque(double valor) throws Exception{
        if (valor <= 0) {
            throw new Exception("Informe um valor maior que zero");
        }
    }

    public void fazerSaque(double valor) throws Exception {
        Conta conta = Sessao.contaLogada;
        ContaDAO contaDAO = new ContaDAO();
        if (!conta.retirar(valor)) {
            throw new Exception("Saldo insuficiente");
        }
        if (!contaDAO.atualizar(conta)) {
            throw new Exception("Erro ao salvar. Tente novamente");
        }
    }

    public String validarTransferencia(double valor, String cpfDestino) throws Exception{
        Conta conta = Sessao.contaLogada;
        cpfDestino = Conta.tratamentoCpf(cpfDestino);

        if (valor <= 0) {
            throw new Exception("Informe um valor válido");
        }
        if (!Conta.validarCpf(cpfDestino)) {
            throw new Exception("CPF do destinatário inválido");
        }
        if (cpfDestino.equals(conta.getCpf())) {
            throw new Exception("Você não pode transferir para você mesmo");
        }
        ContaDAO contaDAO = new ContaDAO();
        Conta contaDestino = contaDAO.buscarContaPorCPF(cpfDestino);
        if (contaDestino == null) {
            throw new Exception("Destinatário não encontrado");
        }
        return contaDestino.getNome();
    }

    public void fazerTransferencia(double valor, String cpfDestino) throws Exception {
        Conta conta = Sessao.contaLogada;
        ContaDAO contaDAO = new ContaDAO();
        cpfDestino = Conta.tratamentoCpf(cpfDestino);
        Conta contaDestino = contaDAO.buscarContaPorCPF(cpfDestino);
        if (!conta.transferencia(contaDestino, valor)) {
            throw new Exception("Saldo insuficiente");
        }
        if (!contaDAO.atualizar(conta) || !contaDAO.atualizar(contaDestino)) {
            throw new Exception("Erro ao salvar. Confira seu saldo e tente novamente");
        }
    }

    public double validarFatura(double valor) throws Exception {
        ContaVIP conta = Sessao.getContaVIP();

        if (conta.getFaturaCredito() <= 0) {
            throw new Exception("Você não tem fatura para pagar");
        }
        if (valor <= 0) {
            throw new Exception("Informe um valor válido");
        }

        return Math.min(valor, conta.getFaturaCredito());
    }

    public void pagarFatura(double valor) throws Exception {
        ContaVIP conta = Sessao.getContaVIP();
        ContaDAO contaDAO = new ContaDAO();

        if (!conta.pagarFatura(valor)) {
            throw new Exception("Saldo insuficiente para pagar a fatura");
        }
        if (!contaDAO.atualizar(conta)) {
            throw new Exception("Erro ao salvar. Tente novamente");
        }
    }
}
