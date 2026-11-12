package banco.observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Observer {
    private final List<ContaObserver> observers = new ArrayList<>();

    public void adicionarObserver(ContaObserver observer) {
        if (!observers.contains(observer)) observers.add(observer);
    }

    public void removerObserver(ContaObserver observer) {
        observers.remove(observer);
    }

    protected void notificar() {
        for (ContaObserver observer : new ArrayList<>(observers)) {
            observer.contaAtualizada();
        }
    }
}