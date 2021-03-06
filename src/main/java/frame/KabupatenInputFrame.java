package frame;

import helpers.Koneksi;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KabupatenInputFrame extends JFrame {
    private JPanel mainPanel;
    private JTextField idTextField;
    private JTextField namaTextField;
    private JButton simpanButton;
    private JButton batalButton;

    int id;

    public  void  setId(int id) {
        this.id = id;
    }

    public KabupatenInputFrame(){
        simpanButton.addActionListener( e -> {
            String nama = namaTextField.getText();
            Connection c = Koneksi.getConnection();
            PreparedStatement ps;

            try {
            if (id == 0 ){
                String insertSQL = "INSERT INTO kabupaten SET nama = ?";
                    ps = c.prepareStatement(insertSQL);
                    ps.setString( 1,nama);
                    ps.executeUpdate();
                    dispose();

            } else {
                String updateSQL = "UPDATE kabupaten SET nama = ? WHERE ID = ?";
                    ps = c.prepareStatement(updateSQL);
                    ps.setString( 1,nama);
                    ps.setInt( 2,id);
                    ps.executeUpdate();
                    dispose();
                }
            } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
        });

        batalButton.addActionListener( e -> {
            dispose();
        });
        init();
    }
    public void init(){
        setContentPane(mainPanel);
        setTitle("Input Kabupaten");
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void isiKomponen(){
        Connection c = Koneksi.getConnection();
        String findSQL = "SELECT * FROM kabupaten WHERE id = ?";
        PreparedStatement ps;

        try {
            ps = c.prepareStatement(findSQL);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                idTextField.setText(String.valueOf(rs.getInt("id")));
                namaTextField.setText(rs.getString("nama"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
