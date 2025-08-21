package org.furb;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class Coordenador {
    private final int ID;
    private Queue<Processo> filaEspera;
    private static boolean recursoLiberado = true;

    public Coordenador(int ID) {
        this.ID = ID;
        filaEspera = new LinkedList<Processo>();
    }

    // Recebe uma requisição de um processo e o coloca na fila ou libera o recurso
    public synchronized void receberRequisicao(Processo processo) {
        System.out.printf("O processo %d quer usar o recurso.\n", processo.getID());

        // Verifica se o recurso está liberado.
        if (recursoLiberado) {
            // Se estiver liberado, o recurso agora fica ocupado.
            recursoLiberado = false;
            concederPermissao(processo);
        }
        // Se o processo já estiver na fila, não o adiciona novamente.
        else if (filaEspera.contains(processo)) {
            System.out.printf("Recurso ocupado. O processo %d já está na fila.\n", processo.getID());
        }
        else {
            // Se o recurso estiver ocupado e o processo não estiver na fila, adiciona o processo na fila de espera.
            System.out.printf("Recurso ocupado. O processo %d foi colocado na fila.\n", processo.getID());
            filaEspera.add(processo);
            imprimirFila();
        }
    }

    // Permite que um processo acesse o recurso
    private void concederPermissao(Processo processo) {
        System.out.printf("Recurso liberado para o processo %d.\n", processo.getID());
        new Thread(processo::processar).start();
    }

    // Processo chama essa função para avisar que terminou de usar o recurso
    public synchronized void liberarRecurso() {
        // Verifica se a fila de espera não está vazia.
        if (!filaEspera.isEmpty()) {
            // Pega o próximo processo da fila.
            Processo proximo = filaEspera.poll();
            // Concede a permissão para o próximo processo.
            // O recurso continua como 'ocupado' (recursoLiberado = false).
            concederPermissao(proximo);
        } else {
            // Se a fila estiver vazia, o recurso é marcado como liberado.
            recursoLiberado = true;
        }
    }

    private void imprimirFila() {
        String fila = filaEspera.stream()
                .map(p -> String.valueOf(p.getID()))
                .collect(Collectors.joining(", "));
        System.out.println("Fila de espera atual: [" + fila + "]");
    }

    public void limparFila() {
        filaEspera.clear();
    }
}
