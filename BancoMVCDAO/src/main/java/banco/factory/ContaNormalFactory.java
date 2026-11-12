package banco.factory;

import banco.model.Conta;
import banco.model.ContaNormal;

public class ContaNormalFactory extends ContaFactory {
    public Conta criarConta(String nome, String cpf, String senha) {
        return new ContaNormal(nome, cpf, senha);
    }

    public Conta reconstituirConta(String nome, String cpf, String senha,
                                   double saldo, double limite, double fatura) {
        return new ContaNormal(nome, cpf, senha, saldo);
    }
}