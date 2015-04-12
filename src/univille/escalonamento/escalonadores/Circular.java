package univille.escalonamento.escalonadores;

import java.util.List;

import univille.escalonamento.Processo;

/**
 * Escalona os processos de acordo com o momento de chegada, mas alterna entre
 * eles a cada quantum
 */
public class Circular extends FCFS {
    /**
     * Contador para a troca de contexto.<br/>
     * Começa com o valor de <code>quantum</code>, e descrece com o tempo, até
     * chegar a zero. <br/>
     * Quando atinge o zero a troca de contexto ocorre.
     */
    private int   contadorTrocaContexto = 0;
    /**
     * Indice que armazena o último processo que foi execultado.<br/>
     * Sempre que o valor do <code>size</code> da lista de <code>prontos</code>
     * é alçado este indice é zerado.
     */
    private int   ultimoExecutado       = 0;

    /**
     * Numero de "tempos" até a troca de processo
     */
    protected int quantum               = 0;

    /**
     * Inicializa um Circular
     */
    public Circular() {
        super();
    }

    /**
     * Inicializa um Circular informando se é preemptivo, e alguns/todos
     * processos que serão escalonados.
     * 
     * @param quantum
     *            Tempo entre as trocas de processos
     * @param processos
     *            Processos por escalonar
     * @see #quantum
     */
    public Circular(int quantum, List<Processo> processos) {
        super(processos);
        this.quantum = quantum;
    }

    /**
     * Inicializa um Circular informando se é preemptivo.
     * 
     * @param quantum
     *            Tempo entre as trocas de processos
     * @see #quantum
     */
    public Circular(int quantum) {
        super();
        this.quantum = quantum;
    }

    @Override
    public boolean passaTempo() {
        if (this.getTempoAtual() != -1) {
            if (this.corrente.getTempoExecucao() <= 0)
                this.contadorTrocaContexto = this.quantum;

            if (this.contadorTrocaContexto == 0 || this.corrente == null) {
                this.contadorTrocaContexto = this.quantum;
                this.ultimoExecutado++;
            }

            if (this.ultimoExecutado >= prontos.size())
                this.ultimoExecutado = 0;

            this.corrente = this.prontos.get(this.ultimoExecutado);

            this.contadorTrocaContexto--;

        } else {
            this.contadorTrocaContexto = this.quantum - 1;
            this.ultimoExecutado = 0;
        }

        return super.passaTempo();
    }

    @Override
    public String toString() {
        return String.format("%s (%d tempos)", getClass().getSimpleName(), this.quantum);
    }
}