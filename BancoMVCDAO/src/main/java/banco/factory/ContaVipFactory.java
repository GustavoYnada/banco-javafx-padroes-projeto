package banco.factory;

import banco.model.Conta;
import banco.model.ContaVIP;

public class ContaVipFactory extends ContaFactory {
    @Override
    public Conta criarConta(String nome, String cpf, String senha) {
        return new ContaVIP(nome, cpf, senha);
    }

    @Override
    public Conta reconstituirConta(String nome, String cpf, String senha,
                                   double saldo, double limite, double fatura) {
        return new ContaVIP(nome, cpf, senha, saldo, limite, fatura);
    }
}