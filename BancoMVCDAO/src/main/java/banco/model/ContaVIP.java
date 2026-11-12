package banco.model;

import banco.strategy.RetiradaVipStrategy;

public class ContaVIP extends Conta {
    private double limiteCredito, faturaCredito;
    public ContaVIP(String nome, String cpf, String senha){
        super(nome, cpf, senha, 200.0, new RetiradaVipStrategy());
        this.limiteCredito = 1000;
        this.faturaCredito = 0;
    }

    public ContaVIP(String nome, String cpf, String senha, double saldo, double limiteCredito, double faturaCredito){
        super(nome, cpf, senha, saldo, new RetiradaVipStrategy());
        this.limiteCredito = limiteCredito;
        this.faturaCredito = faturaCredito;
    }

    public boolean podeCredito(double valor){
        double creditoNecessario = valor - this.getSaldo();
        if(creditoNecessario <= 0) return false;
        if(limiteCredito - (creditoNecessario + faturaCredito) >= 0) return true;
        return false;
    }

    public boolean pagarFatura(double valor){
        if(valor <= 0) return false;
        if(valor > faturaCredito) valor = faturaCredito;

        if(this.checarSaldo(valor)){
            faturaCredito -= valor;
            this.setSaldo(this.getSaldo() - valor);
            return true;
        }
        return false;
    }

    public void setFaturaCredito(double faturaCredito){
        this.faturaCredito = faturaCredito;
        notificar();
    }

    public double getLimiteCredito(){ return limiteCredito; }
    public double getFaturaCredito(){ return faturaCredito; }
}