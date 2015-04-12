package univille.escalonamento.escalonadores;

import java.util.List;

import univille.escalonamento.Escalonador;
import univille.escalonamento.Processo;

/**
 * O Escalonamento segue a ordem de chegada
 */
public class FCFS extends Escalonador {
    /**
     * Inicializa um FCFS
     */
    public FCFS() {
        super();
    }

    /**
     * Inicializa um FCFS com processos
     * @param processos Processo por escalonar
     */
    public FCFS(List<Processo> processos) {
        super(processos);
    }

    @Override
    protected void addProcessoPronto(Processo processo) {
        this.prontos.add(processo);
    }

    @Override
    public String toString() {
        return "FCFS";
    }
}
