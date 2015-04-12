package univille.escalonamento.client.window;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import univille.escalonamento.Escalonador;
import univille.escalonamento.Processo;

/**
 * Grafico de Escalonamento
 */
public class JPanelEscalonamento extends JPanel implements Runnable {

    private static final long serialVersionUID = 6878053371054362239L;
    protected int             tempoMili        = 0;
    protected Thread          thread           = null;
    protected int             tempoTotal       = 1;
    protected Window          window           = null;
    protected Escalonador     escalonador      = null;
    protected boolean         executando       = false;

    /**
     * Abre uma Thread para a execução do escalonador
     * 
     * @param escalonador
     *            Escalonador a ser executado
     * @param tempoMili
     *            Tempo de espera entre os "tempos"
     */
    public void start(Escalonador escalonador, int tempoMili) {
        this.thread = new Thread(this, "thread_escalonador");
        this.escalonador = escalonador;
        this.tempoMili = tempoMili;
        this.thread.start();
    }

    @Override
    public void run() {
        Color bg = null;
        Color fg = null;
        List<JLabel> labels = null;
        JLabel label = null;

        this.executando = true;

        this.tempoTotal = 0;
        for (Processo p : this.escalonador.getProcessos()) {
            this.tempoTotal += p.getTempoExecucao();
        }

        ((GridLayout) this.getLayout()).setColumns(this.tempoTotal);

        this.removeAll();

        labels = new ArrayList<JLabel>();

        for (int i = 0; i < this.tempoTotal; i++) {
            label = new JLabel(String.format("%5d", i));
            label.setOpaque(true);
            label.setBackground(this.getBackground());
            label.setForeground(this.getBackground());
            label.setBorder(new LineBorder(Color.black, 1));
            this.add(label);
            labels.add(label);
        }

        this.updateUI();

        try {
            while (!this.escalonador.isTerminado() && this.executando) {
                if (this.tempoMili > 0)
                    Thread.sleep(this.tempoMili);

                escalonador.passaTempo();

                if (escalonador.getCorrente() != null) {
                    bg = this.window.getColorOfProcesso(this.escalonador
                                    .getCorrente());
                    fg = new Color(255 - bg.getRed(), 255 - bg.getGreen(), 255 - bg
                                    .getBlue());

                    label = labels.get(this.escalonador.getTempoAtual());
                    label.setBackground(bg);
                    label.setForeground(fg);
                    
                    // rola até o label
                    this.scrollRectToVisible(label.getBounds());
                    this.updateUI();
                }
            }

            this.window.fimEscalonamento();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.executando = false;
        }
    }

    /**
     * Creates new form JPanelEscalonamento
     */
    public JPanelEscalonamento() {
        initComponents();
    }

    /**
     * Para a execução do JPanelEscalonamento
     */
    public void stop() {
        this.executando = false;
    }

    public Window getWindow() {
        return this.window;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public Escalonador getEscalonador() {
        return this.escalonador;
    }

    public void setEscalonador(Escalonador escalonador) {
        this.escalonador = escalonador;
    }

    public boolean isExecutando() {
        return this.executando;
    }

    private void initComponents() {
        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory
                        .createLineBorder(new java.awt.Color(0, 0, 0)));
        setLayout(new GridLayout(1, 100));
    }
}
