package org.furb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static Coordenador coordenador;
    private static List<Processo> processosAtivos;

    public static void main(String[] args) {
        processosAtivos = new ArrayList<>();
        coordenador = null;
        // Cria 3 processos iniciais
        for (int i = 0; i < 3; i++) {
            criarProcesso();
        }
        // Elege 1 deles como coordenador
        elegerNovoCoordenador();

        // Começa a executar os processos
        for (Processo processo : processosAtivos) {
            executarProcesso(processo);
        }

        // Thread para criar um novo processo a cada 40s
        Thread threadCriarProcesso = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(40000);
                    Processo novoProcesso = criarProcesso();
                    executarProcesso(novoProcesso);
                } catch(InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        // Thread para matar um coordenador a cada 60s
        Thread threadMatarCoordenador = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(60000);
                    elegerNovoCoordenador();
                } catch(InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        });

        // Inicia as threads
        threadCriarProcesso.start();
        threadMatarCoordenador.start();
    }

    private static Processo criarProcesso() {
        Random r = new Random();
        while (true) {
            // Escolhe um ID aleatório
            int id = r.nextInt(0, 1000);
            Processo novoProcesso = new Processo(id, coordenador);
            // Verifica se já não existe um processo com esse ID, caso exista, volta o loop e escolhe um novo ID
            if (processosAtivos.contains(novoProcesso)) {
                continue;
            }
            // Adiciona o processo criado na lista de processos ativos
            processosAtivos.add(novoProcesso);
            System.out.printf("Processo %d criado com sucesso!\n", novoProcesso.getID());
            return novoProcesso;
        }
    }

    private static void executarProcesso(Processo processo) {
        // Executa a thread do processo
        (new Thread(processo)).start();
    }

    private static void elegerNovoCoordenador() {
        // Se não houverem mais processos ativos, termina a execução da aplicação.
        if (processosAtivos.isEmpty()) {
            System.exit(0);
            return;
        }

        // Se houver um coordenador anterior, limpa a fila dele.
        if (coordenador != null) {
            coordenador.limparFila();
        }

        // Escolhe um processo ativo aleatório
        Random r = new Random();
        Processo processo = processosAtivos.get(r.nextInt(processosAtivos.size()));

        // Verifica se o processo escolhido está usando um recurso. Caso esteja, procura um novo coordenador.
        if (processo.getUsandoRecurso()) {
            elegerNovoCoordenador();
            return;
        }

        // 'Mata' o processo
        processosAtivos.remove(processo);
        processo.setEstaAtivo(false);

        // Cria um novo coordenador e avisa os processos
        coordenador = new Coordenador(processo.getID());
        for (Processo p : processosAtivos) {
            p.setNovoCoordenador(coordenador);
        }
        System.out.printf("O processo %d é o novo coordenador. Fila limpa.\n", processo.getID());
    }
}