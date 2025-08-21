package org.furb;

import java.util.LinkedList;
import java.util.Queue;

public class Coordenador {
    private final int ID;
    private Queue<Processo> filaEspera;
    private boolean recursoLiberado;

    public Coordenador(int ID) {
        this.ID = ID;
        filaEspera = new LinkedList<Processo>();
        recursoLiberado = true;
    }

    // Recebe uma requisição de um processo e o coloca na fila ou libera o recurso
    public synchronized void receberRequisicao(Processo processo) {
        System.out.printf("O processo %d quer usar o recurso.\n", processo.getID());
        if (!filaEspera.isEmpty() && !filaEspera.contains(processo) && !recursoLiberado) {
            System.out.printf("Recurso já está sendo utilizado. O processo %d foi colocado na fila.\n", processo.getID());
            filaEspera.add(processo);
        }
        else {
            recursoLiberado = false;
            concederPermissao(processo);
        }
    }

    // Permite que um processo acesse o recurso
    private void concederPermissao(Processo processo) {
        System.out.printf("Recurso liberado para o processo %d.\n", processo.getID());
        new Thread(processo::processar).start();
    }

    // Processo chama essa função para avisar que terminou de usar o recurso
    public synchronized void liberarRecurso() {
        recursoLiberado = true;
        if (!filaEspera.isEmpty()) {
            recursoLiberado = false;
            Processo proximo = filaEspera.poll();
            concederPermissao(proximo);
        }
    }

    public void limparFila() {
        filaEspera.clear();
    }
}
