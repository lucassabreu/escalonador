/**
 * Objetos relacionados a escalonamento de processos.
 */
package univille.escalonamento.client;

import java.io.File;
import java.util.List;

import univille.escalonamento.Escalonador;
import univille.escalonamento.Processo;
import univille.escalonamento.escalonadores.Circular;
import univille.escalonamento.escalonadores.FCFS;
import univille.escalonamento.escalonadores.Prioridade;
import univille.escalonamento.escalonadores.SJF;
import univille.escalonamento.util.CSVUtil;

public class Character {

    /**
     * Metodo inicial do sistema
     * 
     * @param args
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Em modo caracter eh esperado ao menos um parametro.");
            aboutSintax();
            return;
        } else if (args.length > 3) {
            System.out.println("Modo caracter suporta apenas tres paramentros.");
            aboutSintax();
            return;
        }

        String sProcessosCSV = null;
        String sAlgoritmo = null;
        String sPreemptivo = null;
        String sQuantum = null;

        File fProcessosCSV = null;
        List<Processo> processos = null;

        Escalonador escalonador = null;
        char algoritmo = 0;

        boolean preemptivo = false;
        int quantum = 4;

        sProcessosCSV = args[0].trim();
        sAlgoritmo = args[1].trim().toLowerCase();

        if (args.length == 3) {
            if (sAlgoritmo.equals("circular"))
                sQuantum = args[2].trim();
            else
                sPreemptivo = args[2].trim().toLowerCase();
        }

        algoritmo = sAlgoritmo.charAt(0);

        if (algoritmo != 'f' && algoritmo != 's' && algoritmo != 'p' && algoritmo != 'c') {
            System.out.println(" * Algoritmo informado invalido! Deve ser: FCFS, SJF, Prioridade ou Circular.");
            return;
        }

        fProcessosCSV = new File(sProcessosCSV);

        if (!fProcessosCSV.exists()) {
            System.out.println(" * Arquivo CSV de processos nao existe!");
            return;
        }

        if (!fProcessosCSV.isFile()) {
            System.out.println(" * É preciso informar um arquivos CSV no primeiro parametro!");
            return;
        }

        if (!fProcessosCSV.canRead()) {
            System.out.println(" * O usuário nao tem permissao de leitura no arquivo!");
            return;
        }

        try {
            CSVUtil.validarCSV(fProcessosCSV);
        } catch (Exception e) {
            System.out.println("Arquivo CSV nao eh valido, a sequencia de colunas eh: Nome;TempoEntrada;TempoExecucao;Prioridade");
            return;
        }

        if (sAlgoritmo.equals("circular") && sQuantum != null) {
            try {
                quantum = Integer.valueOf(sQuantum);
            } catch (Exception e) {
                System.out.println(" * Quantum deve ser um número inteiro!");
            }
        } else if (sPreemptivo != null) {
            if (sPreemptivo.equals("sim") || sPreemptivo.equals("yes"))
                preemptivo = true;
            else
                preemptivo = false;
        }

        try {
            processos = CSVUtil.csvParaProcessos(fProcessosCSV);

            switch (algoritmo) {
                case 'f':
                    escalonador = new FCFS(processos);
                    break;
                case 's':
                    escalonador = new SJF(preemptivo, processos);
                    break;
                case 'p':
                    escalonador = new Prioridade(preemptivo, processos);
                    break;
                case 'c':
                    escalonador = new Circular(quantum, processos);
                    break;
            }

            StringBuilder sb = new StringBuilder();

            System.out.println(String.format("\nProgresso - %s:", escalonador
                            .toString()));
            System.out.println("-------------------------------------------------------");

            while (!escalonador.isTerminado()) {
                escalonador.passaTempo();
                if (escalonador.getCorrente() != null) {
                    System.out.println(String
                                    .format("Tempo %3d - Processo %s", escalonador
                                                    .getTempoAtual(), escalonador
                                                    .getCorrente().getNome()));
                    sb.append(String.format("%s;", escalonador.getCorrente()
                                    .getNome()));
                } else {
                    System.out.println(String
                                    .format("Tempo %3d - Nenhum processo", escalonador
                                                    .getTempoAtual()));
                    sb.append(";");
                }
            }

            System.out.println(String.format("Sequencia Execucao: %s", sb
                            .toString()));

            System.out.println("\nRelatorio:");
            System.out.println("-------------------------------------------------------");

            int acumulador = 0;

            for (Processo p : escalonador.getProcessos()) {
                System.out.println(String.format("Processo %s - %d", p
                                .getNome(), p.getTempoEmEspera()));
                acumulador += p.getTempoEmEspera();
            }

            System.out.println("-------------------------------------------------------");
            System.out.println(String
                            .format("Tempo de espera medio: %.2f", ((float) acumulador) / ((float) escalonador
                                            .getProcessos().size())));

        } catch (Exception e) {
            System.out.println("Ocorreu o seguinte erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void aboutSintax() {
        System.out.println("Sintaxe do comando: ");
        System.out.println("  java -jar Escalonador.jar univille.escalonamento.client.Character processos algoritmo [preemptivo|quantum] ");
        System.out.println("    processos  - CSV contento os processos no formato: Nome;TempoEntrada;TempoExecucao;Prioridade");
        System.out.println("    algoritmo  - Algoritmo utilizado na execução, pode ser: FCFS, SJF, Prioridade e Circular.");
        System.out.println("    preemptivo - \"sim\" para o algoritmo rodar como preemptivo, \"nao\" para nao-preemptivo. Padrao: \"nao\".");
        System.out.println("    quantum    - Caso o algoritmo escolhido seja o Circular o terceiro parametro deve ser o valor do quantum. Padrao: 4");
    }
}
