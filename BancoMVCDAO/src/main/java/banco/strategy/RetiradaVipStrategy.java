package banco.strategy;

import banco.model.Conta;
import banco.model.ContaVIP;

public class RetiradaVipStrategy implements RetiradaStrategy{
    public boolean executarRetirada(Conta conta, double valor){
        ContaVIP contaVip = (ContaVIP) conta;
        if(valor <= 0) return false;
        if(valor <= contaVip.getSaldo()){
            contaVip.setSaldo(contaVip.getSaldo() - valor);
            return true;
        } else{
            if(contaVip.podeCredito(valor)){
                double creditoNecessario = valor - contaVip.getSaldo();
                contaVip.setFaturaCredito(contaVip.getFaturaCredito() + creditoNecessario);
                contaVip.setSaldo(0);
                return true;
            }
        }
        return false;
    }
}
