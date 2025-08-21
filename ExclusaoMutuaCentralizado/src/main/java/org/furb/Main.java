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
        for (int i = 0; i < 3; i++) {
            criarProcesso();
        }
        elegerNovoCoordenador();

        for (Processo processo : processosAtivos) {
            executarProcesso(processo);
        }

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

        threadCriarProcesso.start();
        threadMatarCoordenador.start();
    }

    private static Processo criarProcesso() {
        Random r = new Random();
        while (true) {
            int id = r.nextInt(0, 1000);
            Processo novoProcesso = new Processo(id, coordenador);
            if (processosAtivos.contains(novoProcesso)) {
                continue;
            }
            processosAtivos.add(novoProcesso);
            System.out.printf("Processo %d criado com sucesso!\n", novoProcesso.getID());
            return novoProcesso;
        }
    }

    private static void executarProcesso(Processo processo) {
        (new Thread(processo)).start();
    }

    private static void elegerNovoCoordenador() {
        if (processosAtivos.isEmpty()) {
            System.exit(0);
            return;
        }

        if (coordenador != null) {
            coordenador.limparFila();
        }

        // Escolhe um processo aleatório
        Random r = new Random();
        Processo processo = processosAtivos.get(r.nextInt(processosAtivos.size()));

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
        System.out.printf("O processo %d é o novo coordenador.\n", processo.getID());
    }
}