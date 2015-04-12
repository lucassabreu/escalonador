package univille.escalonamento.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import univille.escalonamento.Processo;

public class CSVUtil {
    /**
     * Valida se a primeira linha do CSV é compativel com a estrutura para
     * conversão em Processo
     * 
     * @see Processo
     * 
     * @param csv
     *            CSV a ser avaliado
     * @return Verdadeiro se corresponde a estrutura esperada.
     * @throws Exception
     *             Caso o número de colunas não esteja correto, ou se algum
     *             campo estiver no formato errado
     * @throws IOException
     *             Qualquer problema relacionado a leitura do CSV.
     */
    public static boolean validarCSV(File csv) throws Exception, IOException {
        BufferedReader br = new BufferedReader(new FileReader(csv));
        
        String linha = br.readLine(); // Nome;TempoEntrada;TempoExecucao;Prioridade

        String[] conteudo = linha.split(";");

        try {
            if (conteudo.length < 4)
                throw new Exception("Numero de colunas invalido!");

            for (int i = 0; i < conteudo.length; i++)
                conteudo[i] = conteudo[i].trim();

            // caso algum dos campos abaixo não seja inteiro será lancada uma
            // execução.
            Integer.valueOf(conteudo[1]);
            Integer.valueOf(conteudo[2]);
            Integer.valueOf(conteudo[3]);

        } finally {
            br.close();
        }

        return true;
    }

    /**
     * Valida o arquivo, o lê e converte para uma lista de processos.
     * 
     * @see #validarCSV(File)
     * 
     * @param csv
     *            Arquivo CSV a se converter
     * @return Lista de processos
     * @throws Exception
     * @throws IOException
     */
    public static List<Processo> csvParaProcessos(File csv) throws Exception, IOException {
        if (!validarCSV(csv))
            return null;

        BufferedReader br = new BufferedReader(new FileReader(csv));

        List<Processo> processos = new ArrayList<Processo>();
        Processo processo = null;

        String linha = null;
        String[] campos = null;

        int tempoEntrada;
        int tempoExecucao;
        int prioridade;

        int i;

        
        try {
            for (linha = br.readLine(); linha != null; linha = br.readLine()) { // Nome;TempoEntrada;TempoExecucao;Prioridade
                if (linha.length() > 0) {
                    campos = linha.split(";");

                    for (i = 0; i < campos.length; i++)
                        campos[i] = campos[i].trim();

                    tempoEntrada = Integer.valueOf(campos[1]);
                    tempoExecucao = Integer.valueOf(campos[2]);
                    prioridade = Integer.valueOf(campos[3]);

                    processo = new Processo(campos[0], tempoExecucao, prioridade, tempoEntrada);
                    processos.add(processo);

                }
            }
        } finally {
            br.close();
        }

        return processos;
    }
}
