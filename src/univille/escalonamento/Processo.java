package univille.escalonamento;

/**
 * Representação de um processo dentro do sistema
 */
public class Processo {
    private String nome;
    private int    tempoExecucao;
    private int    prioridade;
    private int    tempoEntrada  = 0;
    private int    tempoEmEspera = 0;

    /**
     * Cria uma instancia de processo com base nos parâmetros
     * 
     * @param nome
     *            Nome do processo
     * @param tempoExecucao
     *            Tempo de execução total do processo
     * @param prioridade
     *            Prioridade do processo craido
     */
    public Processo(String nome, int tempoExecucao, int prioridade) {
        super();
        this.nome = nome;
        this.tempoExecucao = tempoExecucao;
        this.prioridade = prioridade;
    }

    /**
     * Cria uma instancia de processo com momento de entrada.
     * 
     * @param nome
     *            Nome do processo
     * @param tempoExecucao
     *            Tempo de execução total do processo
     * @param prioridade
     *            Prioridade do processo craido
     * @param tempoEntrada
     *            Momento em que o processo entra no sistema
     */
    public Processo(String nome, int tempoExecucao, int prioridade,
                    int tempoEntrada) {
        this(nome, tempoExecucao, prioridade);
        this.tempoEntrada = tempoEntrada;
    }

    /**
     * Decrementa o tempo de execução dentro do processo
     * 
     * @return Tempo restante depois do decremento
     */
    public int passaTempo() {
        return --this.tempoExecucao;
    }

    /**
     * Aumenta o tempo em espera de um processo.
     * 
     * @return Tempo em espera incrementado.
     */
    public int incrementaEspera() {
        return ++this.tempoEmEspera;
    }

    /**
     * Retorna o nome do método
     * 
     * @return
     */
    public String getNome() {
        return nome;
    }

    /**
     * Retorna o tempo de execução restante.
     * 
     * @return
     */
    public int getTempoExecucao() {
        return tempoExecucao;
    }

    /**
     * Prioridade do processo
     * 
     * @return
     */
    public int getPrioridade() {
        return prioridade;
    }

    /**
     * Momento de entrada do processo na lista
     * 
     * @return
     */
    public int getTempoEntrada() {
        return tempoEntrada;
    }

    /**
     * Altera o momento de entrada do processo
     * 
     * @param tempoExecucao
     */
    void setTempoEntrada(int tempoEntrada) {
        this.tempoEntrada = tempoEntrada;
    }

    /**
     * Tempo em espera atual do processo.
     * 
     * @return tempoEmEspera
     */
    public int getTempoEmEspera() {
        return tempoEmEspera;
    }

    @Override
    public String toString() {
        return "Processo<" + nome + ">";
    }
}
