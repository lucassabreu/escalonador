package univille.escalonamento.client.window;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import univille.escalonamento.Processo;

/**
 * JTable orientada para objetos do tipo Processo
 */
public class JTableProcesso extends JTable {
    private static final long serialVersionUID = -231463163988352959L;

    public JTableProcesso(List<Color> colors) {
        super();

        this.setModel(new TableModel(new Object[][] {}));

        this.getTableHeader().setVisible(true);
        this.getTableHeader().setReorderingAllowed(false);
        
        this.getColumnModel().getColumn(0).setPreferredWidth(30);
        this.getColumnModel().getColumn(0).setMinWidth(30);
        this.getColumnModel().getColumn(0).setMaxWidth(30);
        
        this.getColumnModel().getColumn(1).setResizable(false);
        this.getColumnModel().getColumn(2).setResizable(false);
        this.getColumnModel().getColumn(3).setResizable(false);
        this.getColumnModel().getColumn(4).setResizable(false);
        this.getColumnModel().getColumn(5).setResizable(false);

        this.setDefaultRenderer(Object.class, new TableCellRenderer(colors));
    }

    public void setProcessos(List<Processo> processos) {
        Object[][] data = new Object[processos.size()][6];

        for (int i = 0; i < data.length; i++) {
            data[i][0] = "";
            data[i][1] = processos.get(i).getNome();
            data[i][2] = processos.get(i).getTempoEntrada();
            data[i][3] = processos.get(i).getTempoExecucao();
            data[i][4] = processos.get(i).getPrioridade();
            data[i][5] = processos.get(i).getTempoEmEspera();
        }

        this.setModel(new TableModel(data));
        this.getColumnModel().getColumn(1).setWidth(10);
    }

    class TableCellRenderer extends DefaultTableCellRenderer {
        public List<Color>        colors           = new ArrayList<Color>();
        private static final long serialVersionUID = -4222569836701058574L;

        public TableCellRenderer(List<Color> colors) {
            this.colors = colors;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (column == 0)
                this.setBackground(this.colors.get(row));
            else if (!isSelected)
                this.setBackground(Color.white);

            return this;
        }
    }

    class TableModel extends DefaultTableModel {
        private static final long serialVersionUID = -4291411585107469787L;

        public TableModel(Object[][] data) {
            super(data, new String[] { "Cor", "Nome", "Entrada", "Execução",
                    "Prioridade", "Espera" });
        }

        @SuppressWarnings("unchecked")
        Class<Object>[] types   = new Class[] { java.lang.String.class,
                                        java.lang.String.class,
                                        java.lang.Integer.class,
                                        java.lang.Integer.class,
                                        java.lang.Integer.class,
                                        java.lang.Integer.class };
        boolean[]       canEdit = new boolean[] { false, false, false, false,
                                        false, false };

        public Class<Object> getColumnClass(int columnIndex) {
            return types[columnIndex];
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit[columnIndex];
        }
    }
}
