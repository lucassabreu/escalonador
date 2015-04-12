package univille.escalonamento.escalonadores;

import java.util.List;

import univille.escalonamento.Escalonador;
import univille.escalonamento.Processo;

/**
 * Escalona os processos de acordo com o tempo de execu��o
 */
public class SJF extends Escalonador {

    /**
     * Inicializa um SJF
     */
    public SJF() {
        super();
    }

    /**
     * Inicializa um SJF informando se � preemptivo, e alguns/todos processos
     * que ser�o escalonados.
     * 
     * @param preemptivo
     *            Se � preemptivo ou n�o-preemptivo
     * @param processos
     *            Processos por escalonar
     */
    public SJF(boolean preemptivo, List<Processo> processos) {
        super(preemptivo, processos);
    }

    /**
     * Inicializa um SJF informando se � preemptivo.
     * 
     * @param preemptivo
     *            Se � preemptivo ou n�o-preemptivo
     */
    public SJF(boolean preemptivo) {
        super(preemptivo);
    }

    /**
     * Inicializa um SJF informando alguns/todos processos que ser�o
     * escalonados.
     * 
     * @param processos
     *            Processos por escalonar
     */
    public SJF(List<Processo> processos) {
        super(processos);
    }

    @Override
    protected void addProcessoPronto(Processo processo) {
        if (this.prontos.isEmpty() || this.prontos.get(this.prontos.size() - 1)
                        .getTempoExecucao() <= processo.getTempoExecucao()) {
            this.prontos.add(processo);
            return;
        }

        int cur = 0;
        if (this.prontos.get(0).getTempoExecucao() > processo
                        .getTempoExecucao())
            cur = 0;
        else {
            int ini = 0;
            int fim = this.prontos.size();

            while (fim > ini) {
                cur = (ini + fim) / 2;

                if (this.prontos.get(cur).getTempoExecucao() < processo
                                .getTempoExecucao()) {
                    ini = cur + 1;
                } else {
                    if (this.prontos.get(cur).getTempoExecucao() > processo
                                    .getTempoExecucao()) {
                        fim = cur - 1;
                    } else {
                        for (cur++; cur < this.prontos.size(); cur++) {
                            if (this.prontos.get(cur).getTempoExecucao() > processo
                                            .getTempoExecucao())
                                break; // encontrou um maior
                        }
                        break; // chegou ao fim do vetor
                    }
                }
            }
        }

        if (cur == this.prontos.size()) {
            this.prontos.add(processo);
            return;
        }

        for (; cur < this.prontos.size(); cur++)
            if (this.prontos.get(cur).getTempoExecucao() > processo
                            .getTempoExecucao())
                break;

        Processo antigo = null;
        Processo atual = null;

        atual = processo;

        for (; cur < this.prontos.size(); cur++) {
            antigo = this.prontos.get(cur);
            this.prontos.set(cur, atual);
            atual = antigo;
        }

        this.prontos.add(atual);
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", getClass().getSimpleName(), this.preemptivo ? "preempitivo" : "n�o-preemptivo");
    }
}