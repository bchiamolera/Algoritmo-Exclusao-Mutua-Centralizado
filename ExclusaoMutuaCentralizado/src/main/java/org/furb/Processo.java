package org.furb;

import java.util.Random;

public class Processo implements Runnable {
    private final int ID;
    private Coordenador coordenadorAtual;
    private boolean estaAtivo;
    private boolean usandoRecurso;

    public Processo(int id, Coordenador coordenador) {
        ID = id;
        estaAtivo = true;
        usandoRecurso = false;
        this.coordenadorAtual = coordenador;
    }

    @Override
    public void run() {
        // Enquanto o processo está ativo (vivo) e não está usando o recurso.
        while (estaAtivo && !usandoRecurso) {
            try {
                Random r = new Random();
                Thread.sleep(r.nextInt(10, 26) * 1000L);
                // Se continua ativo e ele mesmo não começou a usar o recurso no tempo de espera, manda uma requisição.
                if (estaAtivo && !usandoRecurso) coordenadorAtual.receberRequisicao(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void processar() {
        // Ocupa o recurso
        usandoRecurso = true;
        try {
            Random r = new Random();
            Thread.sleep(r.nextInt(5, 16) * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            // Libera o recurso e avisa o coordenador.
            usandoRecurso = false;
            System.out.printf("Processo %d liberou o recurso.\n", ID);
            coordenadorAtual.liberarRecurso();
            // Reinicia sua thread
            new Thread(this).start();
        }
    }

    public void setNovoCoordenador(Coordenador novoCoordenador) {
        this.coordenadorAtual = novoCoordenador;
    }

    public void setEstaAtivo(boolean estaAtivo) {
        this.estaAtivo = estaAtivo;
    }

    public int getID() {
        return this.ID;
    }

    public boolean getUsandoRecurso() {
        return usandoRecurso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Processo processo = (Processo) o;
        return processo.ID == ID;
    }
}
