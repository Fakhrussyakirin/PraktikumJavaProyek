package frame;

import helpers.Koneksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class KabupatenViewFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel cariPanel;
    private JTextField cariTextField;
    private JButton cariButton;
    private JTable viewTable;
    private JButton tambahButton;
    private JButton ubahButton;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton cetakButton;
    private JButton tutupButton;
    private JScrollPane viewScrollPane;
    private JPanel buttonPanel;

    public KabupatenViewFrame(){

        ubahButton.addActionListener( e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih<0){
                JOptionPane.showMessageDialog(null,
                        "pilih data dulu",
                        "Validasi pilih data dulu",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            TableModel tm = viewTable.getModel();
            int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());
            KabupatenInputFrame inputFrame = new KabupatenInputFrame();
            inputFrame.setId(id);
            inputFrame.isiKomponen();
            inputFrame.setVisible(true);
        });

        tambahButton.addActionListener( e -> {
            KabupatenInputFrame inputFrame = new KabupatenInputFrame();
            inputFrame.setVisible(true);
        });

        hapusButton.addActionListener( e -> {
            int barisTerpilih = viewTable.getSelectedRow();
            if (barisTerpilih<0){
                JOptionPane.showMessageDialog(null,
                        "pilih data dulu",
                        "Validasi pilih data dulu",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int pilihan = JOptionPane.showConfirmDialog( null,
                    "Yakin hapus?",
                    "Konfirmasi Hapus",
                    JOptionPane.YES_NO_OPTION);

            if (pilihan == 0 ){
                TableModel tm = viewTable.getModel();
                int id = Integer.parseInt(tm.getValueAt(barisTerpilih, 0).toString());

                Connection c = Koneksi.getConnection();
                String deleteSQL = "DELETE FROM kabupaten WHERE id = ?";

                try {
                    PreparedStatement ps = c.prepareStatement(deleteSQL);
                    ps.setInt(1,id);
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }

            JOptionPane.showMessageDialog( null, String.valueOf(pilihan));

        });
        cariButton.addActionListener( e -> {
            Connection c = Koneksi.getConnection();
            String keyword = "%" + cariTextField.getText() + "%";
            String searchSQL = "SELECT * fROM kabupaten WHERE nama like ?";

            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString( 1,keyword );
                ResultSet rs = ps.executeQuery();

                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[2];

                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        cariButton.addActionListener( e ->{
            Connection c = Koneksi.getConnection();
            String keyword = "%" +cariTextField.getText() + "$";
            String searchSQL = "SELECT * FROM kabupaten WHERE nama like ?";

            try {
                PreparedStatement ps = c.prepareStatement(searchSQL);
                ps.setString(1,keyword);
                ResultSet rs = ps.executeQuery();

                DefaultTableModel dtm = (DefaultTableModel) viewTable.getModel();
                dtm.setRowCount(0);
                Object[] row = new Object[2];

                while (rs.next()){
                    row[0] = rs.getInt("id");
                    row[1] = rs.getString("nama");
                    dtm.addRow(row);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        tutupButton.addActionListener( e -> {
            dispose();
        });
        batalButton.addActionListener( e -> {
            isiTable();
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                isiTable();
            }
        });
        isiTable();
        init();
    }
    public void init(){
        setContentPane(mainPanel);
        setTitle("Data Kabupaten");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiTable(){
        Connection c = Koneksi.getConnection();
        String selectSQL = "SELECT * FROM kabupaten";
        try {
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(selectSQL);

            String header[] = {"Id", "Nama Kabupaten"};
            DefaultTableModel dtm = new DefaultTableModel (header,0);
            viewTable.setModel(dtm);
            Object[] row = new Object[2];
            while (rs.next()){
                row[0]= rs.getInt("id");
                row[1]= rs.getString( "nama");
                dtm.addRow(row);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
