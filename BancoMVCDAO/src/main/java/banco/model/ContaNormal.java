package banco.model;

import banco.strategy.RetiradaNormalStrategy;

public class ContaNormal extends Conta {
    public ContaNormal(String nome, String cpf, String senha){
        super(nome, cpf, senha, 100.0, new RetiradaNormalStrategy());
    }

    public ContaNormal(String nome, String cpf, String senha, double saldo){
        super(nome, cpf, senha, saldo, new RetiradaNormalStrategy());
    }
}