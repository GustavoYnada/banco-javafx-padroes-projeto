package banco.strategy;

import banco.model.Conta;

public class RetiradaNormalStrategy implements RetiradaStrategy{
    public boolean executarRetirada(Conta conta, double valor){
        if(conta.checarSaldo(valor)){
            conta.setSaldo(conta.getSaldo() - valor);
            return true;
        }
        return false;
    }
}
