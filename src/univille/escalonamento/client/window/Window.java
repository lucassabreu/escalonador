package univille.escalonamento.client.window;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import univille.escalonamento.Processo;
import java.awt.Component;

public abstract class Window extends JFrame {

    private static final long     serialVersionUID = 7439943351633186238L;

    protected JPanel              pnContainer;

    protected JPanel              pnParams;

    protected JButton             btCarregar;
    protected JButton             btExecutar;

    protected JLabel              lbAlgoritmos;
    protected JComboBox<String>   cmbAlgoritmos;

    protected JLabel              lbArquivo;
    protected JTextField          txArquivo;

    protected JCheckBox           chPreemptivo;

    protected JLabel              lbQuantum;
    protected JFormattedTextField txQuantum;

    protected JLabel              lbTempo;
    protected JFormattedTextField txTempo;

    protected JTextField          txTempoMEspera;

    private JScrollPane           scrllExecucao;
    protected JPanelEscalonamento pnExecucao;

    protected JTableProcesso      tbProcessos;

    protected List<Color>         colors           = new ArrayList<Color>();

    /**
     * Create the application.
     */
    public Window() {
        setTitle("Algoritmos de Escalonamento");
        initialize();
    }

    abstract public Color getColorOfProcesso(Processo p);

    abstract public List<Color> getColors();

    abstract public void fimEscalonamento();

    abstract protected void cmbAlgoritmosActionPerformed(ActionEvent evt);

    abstract protected void btCarregarActionPerformed(ActionEvent evt);

    abstract protected void btExecutarActionPerformed(ActionEvent evt);

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        this.setBounds(100, 100, 666, 261);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DefaultFormatterFactory dff = new DefaultFormatterFactory(new NumberFormatter(NumberFormat
                        .getIntegerInstance()));

        pnContainer = new JPanel();
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(groupLayout
                        .createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(pnContainer, GroupLayout.PREFERRED_SIZE, 616, Short.MAX_VALUE)
                                        .addContainerGap()));
        groupLayout.setVerticalGroup(groupLayout
                        .createParallelGroup(Alignment.LEADING)
                        .addGroup(groupLayout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(pnContainer, GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                                        .addContainerGap()));

        pnParams = new JPanel();
        pnParams.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

        JScrollPane scrllTable = new JScrollPane();

        scrllExecucao = new JScrollPane();
        GroupLayout gl_pnContainer = new GroupLayout(pnContainer);
        gl_pnContainer.setHorizontalGroup(gl_pnContainer
                        .createParallelGroup(Alignment.LEADING)
                        .addComponent(pnParams, GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                        .addComponent(scrllTable, GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
                        .addComponent(scrllExecucao, GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE));
        gl_pnContainer.setVerticalGroup(gl_pnContainer
                        .createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_pnContainer
                                        .createSequentialGroup()
                                        .addComponent(pnParams, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(scrllExecucao, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(ComponentPlacement.RELATED)
                                        .addComponent(scrllTable, GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)));

        pnExecucao = new JPanelEscalonamento();
        scrllExecucao.setViewportView(pnExecucao);
        pnExecucao.setWindow(this);

        tbProcessos = new JTableProcesso(this.colors);
        scrllTable.setViewportView(tbProcessos);

        lbArquivo = new JLabel("Arquivo:");

        txArquivo = new JTextField();
        txArquivo.setText("resources/processos.csv");
        txArquivo.setEditable(false);
        txArquivo.setColumns(10);

        btCarregar = new JButton("Carregar");
        lbArquivo.setLabelFor(btCarregar);
        btCarregar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btCarregarActionPerformed(e);
            }
        });

        lbAlgoritmos = new JLabel("Algoritmos:");

        btExecutar = new JButton("Executar");
        btExecutar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btExecutarActionPerformed(e);
            }
        });

        cmbAlgoritmos = new JComboBox<String>();
        lbAlgoritmos.setLabelFor(cmbAlgoritmos);
        cmbAlgoritmos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                cmbAlgoritmosActionPerformed(arg0);
            }
        });
        cmbAlgoritmos.setModel(new DefaultComboBoxModel<String>(new String[] {
                "FCFS", "SJF", "Prioridade", "Circular" }));

        chPreemptivo = new JCheckBox("Preemptivo");
        chPreemptivo.setEnabled(false);

        lbQuantum = new JLabel("Quantum:");

        txQuantum = new JFormattedTextField();
        lbQuantum.setLabelFor(txQuantum);
        txQuantum.setFormatterFactory(dff);
        txQuantum.setEnabled(false);
        txQuantum.setHorizontalAlignment(SwingConstants.RIGHT);
        txQuantum.setText("4");

        lbTempo = new JLabel("Tempo (milisegundos):");

        txTempo = new JFormattedTextField();
        lbTempo.setLabelFor(txTempo);
        txTempo.setFormatterFactory(dff);
        txTempo.setHorizontalAlignment(SwingConstants.RIGHT);
        txTempo.setText("0");

        JLabel lbTempoMEspera = new JLabel("Tempo M\u00E9dio Espera:");

        txTempoMEspera = new JTextField();
        txTempoMEspera.setForeground(Color.BLACK);
        txTempoMEspera.setBackground(Color.YELLOW);
        txTempoMEspera.setHorizontalAlignment(SwingConstants.RIGHT);
        txTempoMEspera.setText("0");
        txTempoMEspera.setEnabled(false);
        txTempoMEspera.setColumns(10);
        GroupLayout gl_pnParams = new GroupLayout(pnParams);
        gl_pnParams.setHorizontalGroup(
            gl_pnParams.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_pnParams.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_pnParams.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_pnParams.createSequentialGroup()
                            .addComponent(lbArquivo)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(txArquivo, GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btCarregar))
                        .addGroup(gl_pnParams.createSequentialGroup()
                            .addGroup(gl_pnParams.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(lbTempo, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lbAlgoritmos, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(gl_pnParams.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(txTempo)
                                .addComponent(cmbAlgoritmos, 0, 139, Short.MAX_VALUE))
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addGroup(gl_pnParams.createParallelGroup(Alignment.TRAILING)
                                .addGroup(gl_pnParams.createSequentialGroup()
                                    .addComponent(chPreemptivo)
                                    .addPreferredGap(ComponentPlacement.UNRELATED)
                                    .addComponent(lbQuantum))
                                .addComponent(lbTempoMEspera))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(gl_pnParams.createParallelGroup(Alignment.LEADING, false)
                                .addComponent(txTempoMEspera)
                                .addComponent(txQuantum, GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))
                            .addPreferredGap(ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                            .addComponent(btExecutar)))
                    .addContainerGap())
        );
        gl_pnParams.setVerticalGroup(
            gl_pnParams.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_pnParams.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_pnParams.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lbArquivo)
                        .addComponent(txArquivo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(btCarregar))
                    .addPreferredGap(ComponentPlacement.UNRELATED)
                    .addGroup(gl_pnParams.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_pnParams.createSequentialGroup()
                            .addGroup(gl_pnParams.createParallelGroup(Alignment.BASELINE)
                                .addComponent(lbAlgoritmos)
                                .addComponent(cmbAlgoritmos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(chPreemptivo)
                                .addComponent(lbQuantum)
                                .addComponent(txQuantum, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addGroup(gl_pnParams.createParallelGroup(Alignment.BASELINE)
                                .addComponent(lbTempo)
                                .addComponent(txTempo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbTempoMEspera)
                                .addComponent(txTempoMEspera, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                        .addComponent(btExecutar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gl_pnParams.linkSize(SwingConstants.HORIZONTAL, new Component[] {btCarregar, btExecutar});
        pnParams.setLayout(gl_pnParams);
        pnContainer.setLayout(gl_pnContainer);
        getContentPane().setLayout(groupLayout);
    }
}
