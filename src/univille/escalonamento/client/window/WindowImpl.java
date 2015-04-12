package univille.escalonamento.client.window;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import univille.escalonamento.Escalonador;
import univille.escalonamento.Processo;
import univille.escalonamento.escalonadores.Circular;
import univille.escalonamento.escalonadores.FCFS;
import univille.escalonamento.escalonadores.Prioridade;
import univille.escalonamento.escalonadores.SJF;
import univille.escalonamento.util.CSVUtil;

/**
 * Implementação lógica da tela.
 */
public class WindowImpl extends Window {
    protected List<Processo>  processos;

    private static final long serialVersionUID = 1L;

    @Override
    protected void cmbAlgoritmosActionPerformed(ActionEvent evt) {

        String algNome = null;

        algNome = this.cmbAlgoritmos.getSelectedItem().toString().toLowerCase();

        switch (algNome.charAt(0)) {
            case 'f':
                this.txQuantum.setEnabled(false);
                this.chPreemptivo.setEnabled(false);

                this.txQuantum.setValue(4);
                this.chPreemptivo.setSelected(false);
                break;
            case 's':
                this.txQuantum.setEnabled(false);
                this.chPreemptivo.setEnabled(true);

                this.txQuantum.setValue(4);
                break;
            case 'p':
                this.txQuantum.setEnabled(false);
                this.chPreemptivo.setEnabled(true);

                this.txQuantum.setValue(4);
                break;
            case 'c':
                this.txQuantum.setEnabled(true);
                this.chPreemptivo.setEnabled(false);

                this.txQuantum.setValue(4);
                this.chPreemptivo.setSelected(false);
                break;
        }

        if (evt != null) {
            this.pnExecucao.removeAll();
            this.pnExecucao.updateUI();
            this.txTempoMEspera.setText("0");
        }
    }

    @Override
    protected void btCarregarActionPerformed(ActionEvent evt) {
        String arquivo = null;

        arquivo = this.txArquivo.getText();

        if (arquivo.equals(""))
            arquivo = "resources/processos.csv";

        JFileChooser jfc = new JFileChooser(new File(arquivo));
        jfc.setFileFilter(new FileNameExtensionFilter("Arquivos CSV", "csv"));

        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            arquivo = jfc.getSelectedFile().getAbsolutePath();
            this.txArquivo.setText(arquivo);
            this.loadCSV(arquivo);
        }
    }

    @Override
    protected void btExecutarActionPerformed(ActionEvent evt) {
        if (this.btExecutar.getText().toLowerCase().equals("executar")) {
            String sAlgoritmo = this.cmbAlgoritmos.getSelectedItem().toString()
                            .toLowerCase();

            boolean preemptivo = this.chPreemptivo.isSelected();

            int quantum = Integer.parseInt(this.txQuantum.getText());

            Escalonador escalonador = null;

            if (this.processos == null || this.processos.isEmpty()) {
                this.showMessage("Nenhum processo encontrado para escalonar.");
                return;
            }

            switch (sAlgoritmo.charAt(0)) {
                case 'f':
                    escalonador = new FCFS(this.processos);
                    break;
                case 's':
                    escalonador = new SJF(preemptivo, this.processos);
                    break;
                case 'p':
                    escalonador = new Prioridade(preemptivo, this.processos);
                    break;
                case 'c':
                    escalonador = new Circular(quantum, this.processos);
                    break;
            }

            this.cmbAlgoritmos.setEnabled(false);
            this.chPreemptivo.setEnabled(false);
            this.txQuantum.setEnabled(false);
            this.txTempo.setEnabled(false);
            this.tbProcessos.setEnabled(false);

            this.btCarregar.setEnabled(false);
            this.btExecutar.setText("Parar");

            this.pnExecucao.start(escalonador, Integer.parseInt(this.txTempo
                            .getText().replace(".", "")));
        } else {
            this.pnExecucao.stop();
        }
    }

    protected void loadCSV(String arquivo) {

        File fArquivo = new File(arquivo);

        if (!fArquivo.exists()) {
            this.showMessage("Arquivo CSV de processos nao existe!");
            return;
        }

        if (!fArquivo.isFile()) {
            this.showMessage("É preciso informar um arquivos CSV no primeiro parametro!");
            return;
        }

        if (!fArquivo.canRead()) {
            this.showMessage("O usuário nao tem permissao de leitura no arquivo!");
            return;
        }

        try {
            CSVUtil.validarCSV(fArquivo);
        } catch (Exception e) {
            this.showMessage("Arquivo CSV nao eh valido, a sequencia de colunas eh: Nome;TempoEntrada;TempoExecucao;Prioridade");
            return;
        }

        try {
            this.processos = CSVUtil.csvParaProcessos(fArquivo);

            this.tbProcessos.removeAll();
            this.colors.clear();

            for (int i = 0; i < this.processos.size(); i++)
                this.colors.add(this.generateColor());

            this.tbProcessos.setProcessos(processos);
            this.pnExecucao.removeAll();
            this.pnExecucao.updateUI();
            this.txTempoMEspera.setText("0");
        } catch (Exception e) {
            this.showMessage(String.format("%s: %s", e.getClass().getName(), e
                            .getMessage()));
        }
    }

    protected Color generateColor() {

        boolean unique = false;
        Color c = null;

        while (!unique) {
            unique = true;

            c = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math
                            .random() * 255));

            for (Color col : this.colors)
                if (col.getRGB() == c.getRGB()) {
                    unique = false;
                    break;
                }
        }

        return c;
    }

    @Override
    public void fimEscalonamento() {
        int tempoTotalEmpresa = 0;

        for (int i = 0; i < this.processos.size(); i++) {
            this.tbProcessos.setValueAt(this.processos.get(i)
                            .getTempoEmEspera(), i, 5);
            tempoTotalEmpresa += this.processos.get(i).getTempoEmEspera();
        }

        this.txTempoMEspera
                        .setText(String.format("%.2f", ((float) tempoTotalEmpresa) / this.processos
                                        .size()));

        try {
            this.processos = CSVUtil.csvParaProcessos(new File(this.txArquivo
                            .getText()));
        } catch (Exception e) {
            this.showMessage(String.format("%s: %s", e.getClass().getName(), e
                            .getMessage()));
        } finally {
            this.cmbAlgoritmos.setEnabled(true);
            this.chPreemptivo.setEnabled(true);
            this.txQuantum.setEnabled(true);
            this.txTempo.setEnabled(true);
            this.tbProcessos.setEnabled(true);

            this.cmbAlgoritmosActionPerformed(null);

            this.btCarregar.setEnabled(true);
            this.btExecutar.setText("Executar");
        }
    }

    @Override
    public List<Color> getColors() {
        return this.colors;
    }

    @Override
    public Color getColorOfProcesso(Processo p) {
        int index = this.processos.indexOf(p);
        return this.colors.get(index);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

}
