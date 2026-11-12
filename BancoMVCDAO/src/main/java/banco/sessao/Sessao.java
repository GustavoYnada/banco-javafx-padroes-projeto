package banco.sessao;

import banco.model.Admin;
import banco.model.Conta;
import banco.model.ContaNormal;
import banco.model.ContaVIP;
import banco.observer.ContaObserver;

public class Sessao {
    public static Conta contaLogada;
    public static Admin adminLogado;

    public static void limparSessao() {
        adminLogado = null;
        contaLogada = null;
    }

    public static ContaVIP getContaVIP(){
        return (ContaVIP) contaLogada;
    }

    public static ContaNormal getContaNormal(){
        return (ContaNormal) contaLogada;
    }

    public static void adicionarObserver(ContaObserver observer) {
        if (contaLogada != null) contaLogada.adicionarObserver(observer);
    }

    public static void removerObserver(ContaObserver observer) {
        if (contaLogada != null) contaLogada.removerObserver(observer);
    }
}