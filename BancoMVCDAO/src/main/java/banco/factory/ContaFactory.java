package banco.factory;

import banco.model.Conta;

public abstract class ContaFactory {

    public abstract Conta criarConta(String nome, String cpf, String senha);
    public abstract Conta reconstituirConta(String nome, String cpf, String senha,
                                            double saldo, double limite, double fatura);

    public static ContaFactory obter(boolean isVip) {
        return isVip ? new ContaVipFactory() : new ContaNormalFactory();
    }
}