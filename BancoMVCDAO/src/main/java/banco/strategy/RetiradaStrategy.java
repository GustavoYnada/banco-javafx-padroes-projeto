package banco.strategy;

import banco.model.Conta;

public interface RetiradaStrategy {
    public boolean executarRetirada(Conta conta, double valor);
}
