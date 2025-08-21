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
        while (estaAtivo) {
            try {
                Random r = new Random();
                Thread.sleep(r.nextInt(10, 26) * 1000L);
                coordenadorAtual.receberRequisicao(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Pensar como parar de processar caso o Coordenador troque
    public void processar() {
        usandoRecurso = true;
        try {
            Random r = new Random();
            Thread.sleep(r.nextInt(5, 16) * 1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            usandoRecurso = false;
            System.out.printf("Processo %d liberou o recurso.\n", ID);
            coordenadorAtual.liberarRecurso();
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
