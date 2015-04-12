package univille.escalonamento.escalonadores;

import java.util.List;

import univille.escalonamento.Escalonador;
import univille.escalonamento.Processo;

/**
 * Escalona os processos de acordo com a prioridade
 * 
 * Escalona prioridade menor
 */
public class Prioridade extends Escalonador {

    /**
     * Inicializa um Prioridade
     */
    public Prioridade() {
        super();
    }

    /**
     * Inicializa um Prioridade informando se é preemptivo, e alguns/todos
     * processos que serão escalonados.
     * 
     * @param preemptivo
     *            Se é preemptivo ou não-preemptivo
     * @param processos
     *            Processos por escalonar
     */
    public Prioridade(boolean preemptivo, List<Processo> processos) {
        super(preemptivo, processos);
    }

    /**
     * Inicializa um Prioridade informando se é preemptivo.
     * 
     * @param preemptivo
     *            Se é preemptivo ou não-preemptivo
     */
    public Prioridade(boolean preemptivo) {
        super(preemptivo);
    }

    /**
     * Inicializa um Prioridade informando alguns/todos processos que serão
     * escalonados.
     * 
     * @param processos
     *            Processos por escalonar
     */
    public Prioridade(List<Processo> processos) {
        super(processos);
    }

    @Override
    protected void addProcessoPronto(Processo processo) {
        if (this.prontos.isEmpty() || this.prontos.get(this.prontos.size() - 1)
                        .getPrioridade() <= processo.getPrioridade()) {
            this.prontos.add(processo);
            return;
        }

        int cur = 0;
        if (this.prontos.get(0).getPrioridade() > processo.getPrioridade())
            cur = 0;
        else {
            int ini = 0;
            int fim = this.prontos.size();

            while (fim > ini) {
                cur = (ini + fim) / 2;

                if (this.prontos.get(cur).getPrioridade() < processo
                                .getPrioridade()) {
                    ini = cur + 1;
                } else {
                    if (this.prontos.get(cur).getPrioridade() > processo
                                    .getPrioridade()) {
                        fim = cur - 1;
                    } else {
                        for (cur++; cur < this.prontos.size(); cur++) {
                            if (this.prontos.get(cur).getPrioridade() > processo
                                            .getPrioridade())
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
            if (this.prontos.get(cur).getPrioridade() > processo
                            .getPrioridade())
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
        return String.format("%s (%s)", getClass().getSimpleName(), this.preemptivo ? "preempitivo" : "não-preemptivo");
    }
}