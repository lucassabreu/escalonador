package univille.escalonamento;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe base dos escalonadores.
 */
public abstract class Escalonador {

    /**
     * Processos registrados no sistema para execu��o.<br/>
     * Na primeira itera��o do metodo <code>passaTempo</code>, esta lista �
     * copiada para <code>processosNaoProntos</code>.
     * 
     * @see #passaTempo()
     * @see #processosNaoProntos
     * @see #addProcesso(Processo)
     */
    protected List<Processo> processos;
    /**
     * Processos que cujo tempo de entrada ainda n�o foi atingido.<br/>
     * Conforme o tempo passa os processos entram e saem desta lista.
     * 
     * @see #passaTempo()
     * @see #addProcesso(Processo)
     * @see #tempoAtual
     * @see #processos
     * @see #prontos
     */
    protected List<Processo> processosNaoProntos;
    /**
     * Processos cujo tempo de entrada j� passou, inclui o processo em execu��o
     * e outros que n�o terminaram.<br/>
     * Quando o tempo de entrada de um processo � alcan�ado na lista
     * <code>processosNaoProntos</code> estes s�o escalonados dentro desta lista
     * pelo m�todo <code>addProcessoPronto</code>.<br/>
     * Quando o tempo de execu��o o processo sai desta lista.
     */
    protected List<Processo> prontos;
    /**
     * Processo em execu��o na ultima itera��o do m�doto "passaTempo".
     * 
     * @see #passaTempo()
     */
    protected Processo       corrente   = null;
    /**
     * Qualifica o processo como preemptivo ou n�o-preemptivo.
     */
    protected boolean        preemptivo = false;

    /**
     * Tempo atual da execu��o do escalonador. Se o valor for -1, ent�o ainda
     * n�o houve execu��o.
     * 
     * @see #passaTempo()
     */
    private int              tempoAtual = -1;

    /**
     * Inicializa o escalonador informando se � preemptivo, e alguns/todos
     * processos que ser�o escalonados.
     * 
     * @param preemptivo
     *            Se � preemptivo ou n�o-preemptivo
     * @param processos
     *            Processos por escalonar
     */
    public Escalonador(boolean preemptivo, List<Processo> processos) {
        super();
        this.addProcessos(processos);
        this.preemptivo = preemptivo;
    }

    /**
     * Inicializa o escalonador informando alguns/todos processos que ser�o
     * escalonados.
     * 
     * @param processos
     *            Processos por escalonar
     */
    public Escalonador(List<Processo> processos) {
        this(false, processos);
    }

    /**
     * Inicializa o escalonador informando se � preemptivo.
     * 
     * @param preemptivo
     *            Se � preemptivo ou n�o-preemptivo
     */
    public Escalonador(boolean preemptivo) {
        this(preemptivo, new ArrayList<Processo>());
    }

    /**
     * Inicializa o escalonador como n�o-preemptivo.
     */
    public Escalonador() {
        this(false);
    }

    /**
     * Adiciona o processo a lista de processos.<br/>
     * <br/>
     * Caso o escalonador j� esteja em execu��o, ent�o o tempo de entrada do
     * processo ser� mudado para o <code>tempoAtual</code> da execu��o, e este
     * j� ser� ranqueado na lista de prontos.
     * 
     * @param processo
     *            Processo a ser adicionado
     * @see #processos
     * @see #tempoAtual
     * @see #prontos
     * @see #addProcessoPronto(Processo)
     */
    public Escalonador addProcesso(Processo processo) {
        if (this.processos == null)
            this.processos = new ArrayList<Processo>();
        else {
            if (this.processos.contains(processo))
                return this;
        }

        if (this.processos.isEmpty() || this.processos.get(this.processos
                        .size() - 1).getTempoEntrada() <= processo
                        .getTempoEntrada()) {
            this.processos.add(processo);
            return this;
        }

        int cur = 0;
        if (this.processos.get(0).getTempoEntrada() > processo
                        .getTempoEntrada())
            cur = 0;
        else {
            int ini = 0;
            int fim = this.processos.size();

            while (fim > ini) {
                cur = (ini + fim) / 2;

                if (this.processos.get(cur).getTempoEntrada() < processo
                                .getTempoEntrada()) {
                    ini = cur + 1;
                } else {
                    if (this.processos.get(cur).getTempoEntrada() > processo
                                    .getTempoEntrada()) {
                        fim = cur - 1;
                    } else {
                        for (cur++; cur < this.processos.size(); cur++) {
                            if (this.processos.get(cur).getTempoEntrada() > processo
                                            .getTempoEntrada())
                                break; // encontrou um maior
                        }
                        break; // chegou ao fim do vetor
                    }
                }
            }

        }

        if (cur == this.processos.size()) {
            this.processos.add(processo);
            return this;
        }
        

        for (; cur < this.processos.size(); cur++)
            if (this.processos.get(cur).getTempoEntrada() > processo
                            .getTempoEntrada())
                break;

        Processo antigo = null;
        Processo atual = null;

        atual = processo;

        for (; cur < this.processos.size(); cur++) {
            antigo = this.processos.get(cur);
            this.processos.set(cur, atual);
            atual = antigo;
        }

        this.processos.add(atual);
        
        if (this.tempoAtual != -1) {
            processo.setTempoEntrada(this.tempoAtual);
            this.addProcessoPronto(processo);
        }
        
        return this;
    }

    /**
     * Adiciona uma lista de processos aos processos do sistema.
     * 
     * @param processos
     * 
     * @see #addProcesso(Processo)
     */
    public Escalonador addProcessos(List<Processo> processos) {
        for (Processo p : processos) {
            this.addProcesso(p);
        }

        return this;
    }

    /**
     * Adiciona um processo a lista de prontos.
     * 
     * @param processo
     *            Processo a entrar na mem�ria
     * 
     * @see #prontos
     */
    abstract protected void addProcessoPronto(Processo processo);

    /**
     * Realiza o escalonamento dos processos inseridos no sistema. <br/>
     * <br/>
     * <ul>
     * <li>Alimenta a lista <code>processosNaoProntos</code> com uma c�pia da
     * lista <code>processos</code>.</li>
     * <li>Incrementa o <code>tempoAtual</code></li>
     * <li>Alimenta a lista de <code>prontos</code> com os processo vindos da
     * lista <code>processosNaoProntos</code> que tenham o
     * <code>tempoEntrada</code> igual ao <code>tempoAtual</code>, esses
     * processo s�o removidos da lista <code>processosNaoProntos</code></li>
     * <li>Decrementa o <code>tempoExecu��o</code> do proceso
     * <code>corrente</code>.</li>
     * <li>Incrementa o <code>tempoEspera</code> dos processos na lista de
     * <code>prontos</code> execu��o.</li>
     * <li>Quando o processo <code>corrente</code> alcan�a o fim da execu��o (
     * <code>tempoExecucao</code> == 0), tira-o da lista de <code>prontos</code>
     * .</li>
     * </ul>
     * 
     * @see #corrente
     * @see #processos
     * @see #processosNaoProntos
     * @see #prontos
     * @see #tempoAtual
     * 
     * @return Verdadeiro se algum processo foi escalonado para o
     *         <code>tempoAtual</code>.
     */
    public boolean passaTempo() {
        if (this.tempoAtual == -1) { // copia a lista de processos para a lista
                                     // de processos n�o prontos
            this.tempoAtual = 0;

            this.processosNaoProntos = new ArrayList<Processo>();

            for (Processo p : this.processos)
                this.processosNaoProntos.add(p);

            this.processaEntradas();
        } else
            this.tempoAtual++; // incrementa a passagem de tempo

        if (this.prontos.isEmpty())
            return false;

        Processo corrente = null;
        this.processaEntradas();

        if (this.preemptivo || this.corrente == null || this.corrente
                        .getTempoExecucao() <= 0) {
            // pegua o primeiro processo da lista de espera para execultar
            this.corrente = corrente = this.prontos.get(0);
        } else
            // no n�o preemptivo se o processo ainda pode ser execultado, ent�o
            // ele continua at� o fim
            corrente = this.corrente;

        corrente.passaTempo();

        for (Processo pronto : this.prontos) {
            if (pronto != corrente) // para este caso esta certo o "!=" pois
                                    // quero saber se � o mesmo objeto, n�o se
                                    // possuem os mesmos valores
                pronto.incrementaEspera();
        }

        if (corrente.getTempoExecucao() <= 0) {
            this.prontos.remove(corrente);
        }

        return true;
    }

    /**
     * Processa a lista de processos, para inclu�-los a espera
     */
    protected void processaEntradas() {
        if (this.prontos == null)
            this.prontos = new ArrayList<Processo>();

        List<Processo> entrando = new ArrayList<Processo>();

        for (int i = 0; i < this.processosNaoProntos.size(); i++) {
            if (this.processosNaoProntos.get(i) == null || this.processosNaoProntos
                            .get(i).getTempoEntrada() != this.tempoAtual)
                break;

            this.addProcessoPronto(this.processosNaoProntos.get(i));
            entrando.add(this.processosNaoProntos.get(i));
        }

        this.processosNaoProntos.removeAll(entrando);
    }

    /**
     * Retorna se o escalonador chegou ao final da execu��o.
     * 
     * @return Verdadeiro se n�o h� processos por executar.
     */
    public boolean isTerminado() {
        if (this.tempoAtual == -1)
            return false;

        if (this.prontos.isEmpty() && this.processosNaoProntos.isEmpty())
            return true;
        else
            return false;
    }

    /**
     * Retorna os processos registrados no escalonador.
     * 
     * @see #processos
     */
    public List<Processo> getProcessos() {
        return processos;
    }

    /**
     * Retorna os processos que ainda n�o est�o prontos.
     * 
     * @see #processosNaoProntos
     */
    public List<Processo> getProcessosNaoProntos() {
        return processosNaoProntos;
    }

    /**
     * Retorna os processo prontos no sistema.
     * 
     * @see #prontos
     */
    public List<Processo> getProntos() {
        return prontos;
    }

    /**
     * Retorna o processo corrente.
     * 
     * @see #corrente
     */
    public Processo getCorrente() {
        return corrente;
    }

    /**
     * Retorna se o escalonador � preemptivo.
     * 
     * @see #preemptivo
     */
    public boolean isPreemptivo() {
        return preemptivo;
    }

    /**
     * Retorna qual o tempo atual do escalonador.
     * 
     * @see #tempoAtual
     */
    public int getTempoAtual() {
        return tempoAtual;
    }

}
