package banco.model;

import banco.observer.Observer;
import banco.strategy.RetiradaStrategy;

public abstract class Conta extends Observer {
    private String nome, cpf, senha;
    private double saldo;
    private RetiradaStrategy estrategiaRetirada;

    public Conta(String nome, String cpf, String senha, double saldo, RetiradaStrategy estrategiaRetirada){
        this.nome = nome;
        this.cpf = tratamentoCpf(cpf);
        this.senha = senha;
        this.saldo = saldo;
        this.estrategiaRetirada = estrategiaRetirada;
    }

    public static String tratamentoCpf(String cpf){
        return cpf.replaceAll("\\D", "");
    }

    public static boolean validarCpf(String cpf){
        if(cpf.length() != 11) return false;

        int n1 = cpf.charAt(0) - '0';
        boolean numerosIguais = true;
        for(int i = 0 ; i < 2; i ++){
            int peso = 10 + i;
            int somatoria = 0;
            int stop = 11 - i;
            for(int j = 0; j < stop; j++){
                int n = cpf.charAt(j) - '0';
                if(j < 9 + i){
                    somatoria += n * peso;
                    peso--;
                }
                if(i == 0 && n1 != n){
                    numerosIguais = false;
                }
            }
            if(i == 0 && numerosIguais) return false;
            int resultado = ((somatoria * 10) % 11) % 10;
            int numeroDigitado = cpf.charAt(9 + i) - '0';
            if (resultado != numeroDigitado) return false;
        }
        return true;
    }

    public static boolean validarSenha(String senha1, String senha2){
        if(senha1.length() < 4 || senha1.length() > 26) return false;
        if(!senha1.equals(senha2)) return false;
        return true;
    }


    public boolean checarSaldo(double valor){
        if(valor <= 0) return false;
        if((this.getSaldo() - valor) >= 0) return true;
        return false;
    }

    public boolean depositar(double valor){
        if(valor <= 0) return false;
        this.saldo += valor;
        notificar();
        return true;
    }

    public boolean retirar(double valor){
        return estrategiaRetirada.executarRetirada(this, valor);
    }

    public boolean transferencia(Conta destinatario, double valor){
        if(destinatario.getCpf().equals(this.getCpf())) return false;
        if(!retirar(valor)) return false;

        destinatario.depositar(valor);
        return true;
    }

    public void setNome(String nome){
        this.nome = nome;
    }
    public void setCpf(String cpf){
        this.cpf = tratamentoCpf(cpf);
    }
    public void setSenha(String senha){
        this.senha = senha;
    }
    public void setSaldo(double saldo){
        this.saldo = saldo;
        notificar();
    }

    public String getNome(){ return nome; }
    public String getCpf(){ return cpf; }
    public String getSenha(){ return senha; }
    public double getSaldo(){ return saldo; }
}
