package tampilan;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;
import koneksi.Sesi;

/**
 *
 * @author muham_000
 */
public class MenuKomplain extends javax.swing.JFrame {

    Connection conn = new Koneksi().connect();
    protected int id_karyawan = Sesi.getIDLogin();
    protected int choice;
    
    //deklarasi posisi mouse
    private int xMouse, yMouse;
    
    // create the table data transaksi
    Object[] rowTransaksi = {"ID Transaksi", "Nama Pelanggan", "Total Harga", "Status Pembayaran",
                    "Tanggal Transaksi", "Nama Karyawan"};
    DefaultTableModel tabmodeTransaksi = new DefaultTableModel(null, rowTransaksi);
    
    // create the table data pengambilan
    Object[] rowKomplain = {"ID Komplain", "ID Transaksi", "Nama Pelanggan", "Detail Komplain", "Tanggal Komplain"};
    DefaultTableModel tabmodeKomplain = new DefaultTableModel(null, rowKomplain);
    
    // untuk memuat data table transaksi
    protected void muatTabelTransaksi() {
        clearTableTransaksi();
        tabelTransaksi.setModel(tabmodeTransaksi);
        String query = "SELECT t.id_transaksi, p.nama_pelanggan, t.total_harga,"
                + "t.status_pembayaran, t.tanggal_transaksi, k.nama_karyawan FROM transaksi t "
                + "INNER JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                + "INNER JOIN karyawan k ON t.id_karyawan = k.id_karyawan "
                + "ORDER BY t.id_transaksi ASC";
        
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("t.id_transaksi");
                String b = rs.getString("p.nama_pelanggan");
                String c = "Rp. ";
                c += rs.getString("t.total_harga");
                String d = rs.getString("t.status_pembayaran");
                String e = rs.getString("t.tanggal_transaksi");
                String f = rs.getString("k.nama_karyawan");
                String[] data = {a, b, c, d, e, f};
                tabmodeTransaksi.addRow(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    // to show data table pengambilan
    protected void muatTabelKomplain(){
        clearTableKomplain();
        tabelKomplain.setModel(tabmodeKomplain);
        String query = "SELECT k.id_komplain , k.id_transaksi, k.detail_komplain, k.tanggal_komplain,"
                + "p.nama_pelanggan FROM komplain k "
                + "INNER JOIN transaksi t ON k.id_transaksi = t.id_transaksi "
                + "INNER JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                + "ORDER BY k.id_komplain ASC";

        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("k.id_komplain");
                String b = rs.getString("k.id_transaksi");
                String c = rs.getString("p.nama_pelanggan");
                String d = rs.getString("k.detail_komplain");
                String e = rs.getString("k.tanggal_komplain");
                String[] data = {a, b, c, d, e};
                tabmodeKomplain.addRow(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    // to clear table data transaksi
    protected void clearTableTransaksi() {
        int row = tabmodeTransaksi.getRowCount();
        for (int i = 0; i < row; i++) {
            tabmodeTransaksi.removeRow(0);
        }
    }
    
    // to clear table data komplain
    protected void clearTableKomplain() {
        int row = tabmodeKomplain.getRowCount();
        for (int i = 0; i < row; i++) {
            tabmodeKomplain.removeRow(0);
        }
    }
    
    protected String getTanggal(){
        // Mendapatkan tanggal saat ini
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = dateFormat.format(calendar.getTime());
        return tanggal;
    }

    // to clear the form
    protected void clear() {
        tIDKomplain.setText("");
        tIDTransaksi.setText("");
        tNamaPelanggan.setText("");
        tDetailKomplain.setText("");
    }

    // to save the form
    protected void save() {
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Menyimpan Data Tersebut ?", "Simpan", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            try {
                String query = "INSERT INTO komplain (id_komplain, id_transaksi, detail_komplain, tanggal_komplain) VALUES (?,?,?,?)";

                PreparedStatement pst = conn.prepareStatement(query);

                // Input ke tabel komplain
                pst.setString(1, tIDKomplain.getText());
                pst.setString(2, tIDTransaksi.getText());
                pst.setString(3, tDetailKomplain.getText());
                pst.setString(4, getTanggal());
                pst.executeUpdate();
                
                JOptionPane.showMessageDialog(null, "Data Telah Berhasil Disimpan");
                muatTabelTransaksi();
                muatTabelKomplain();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Yang Anda Masukan Tidak Sesuai");
                System.out.println(e);
            }
        }
    }

    // to update the data in database
    protected void update() {
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Memperbarui Data Tersebut ?", "Ubah", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            try {
                String query = "UPDATE komplain set id_komplain=?, id_transaksi=?, detail_komplain=?, tanggal_komplain=? WHERE id_komplain=?";

                PreparedStatement updateStatement1 = conn.prepareStatement(query);

                updateStatement1.setString(1, tIDKomplain.getText());
                updateStatement1.setString(2, tIDTransaksi.getText());
                updateStatement1.setString(3, tDetailKomplain.getText());
                updateStatement1.setString(4, getTanggal());
                updateStatement1.setString(5, tIDKomplain.getText());
                updateStatement1.executeUpdate();

                JOptionPane.showMessageDialog(null, "Data Telah Berhasil Diperbarui");
                muatTabelTransaksi();
                muatTabelKomplain();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Yang Anda Masukan Tidak Sesuai");
                System.out.println(e);
            }
        }
    }

    // to delete the data in database
    protected void delete() {
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Menghapus Data Tersebut ?", "Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            try {
                String query = "delete from komplain where id_komplain=" + tIDKomplain.getText();
                Statement stat = conn.createStatement();
                stat.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Data Telah Berhasil Dihapus");
            } catch (Exception e) {
                System.out.println(e);
            }
            muatTabelKomplain();
        }
    }

    // to search data in database and show them in table and textfield
    protected void search() {
        String chose = "id_komplain";
        clearTableKomplain();
        clear();
        tabelKomplain.setModel(tabmodeKomplain);
        try {
            String query = "SELECT k.id_komplain , k.id_transaksi, k.detail_komplain, k.tanggal_komplain,"
                + "p.nama_pelanggan FROM komplain k "
                + "INNER JOIN transaksi t ON k.id_transaksi = t.id_transaksi "
                + "INNER JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                + "WHERE ";
            // kondisional if untuk mengecek pilihan yang akan digunakan untuk melakukan pencarian
            if (cCari.getSelectedItem().equals("ID Komplain")) {
                chose = "k.id_komplain";
            } else if (cCari.getSelectedItem().equals("ID Transaksi")) {
                chose = "k.id_transaksi";
            } else if (cCari.getSelectedItem().equals("Nama Pelanggan")) {
                chose = "p.nama_pelanggan";
            }
            // penggabungan string query dengan string chose
            query += chose + " = ?";
            
            PreparedStatement cari1 = conn.prepareStatement(query);
            cari1.setString(1,tCari.getText());
            
            ResultSet rs = cari1.executeQuery();
            while (rs.next()) {
                String a = rs.getString("k.id_komplain");
                String b = rs.getString("k.id_transaksi");
                String c = rs.getString("p.nama_pelanggan");
                String d = rs.getString("k.detail_komplain");
                String e = rs.getString("k.tanggal_komplain");
                String[] data = {a, b, c, d, e};
                tabmodeKomplain.addRow(data);
            }
        } catch (Exception e) {
        }
    }

    // to select the table item transaksi and show them in textfield
    protected void selectTableItemTransaksi() {
        tIDKomplain.setText("");
        try {
            int row = tabelTransaksi.getSelectedRow();
            tIDTransaksi.setText((String) tabelTransaksi.getModel().getValueAt(row, 0));
            tNamaPelanggan.setText((String) tabelTransaksi.getModel().getValueAt(row, 1));
        } catch (Exception e) {
        }
    }
    
    // to select the table item pengambilan and show them in textfield
    protected void selectTableItemPengambilan() {
        try {
            int row = tabelKomplain.getSelectedRow();
            tIDKomplain.setText((String) tabelKomplain.getModel().getValueAt(row, 0));
            tIDTransaksi.setText((String) tabelKomplain.getModel().getValueAt(row, 1));
            tNamaPelanggan.setText((String) tabelKomplain.getModel().getValueAt(row, 2));
            tDetailKomplain.setText((String) tabelKomplain.getModel().getValueAt(row, 3));
        } catch (Exception e) {
        }
    }
    
    protected void kembali() {
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Kembali Ke Menu Utama ?", "Kembali", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0){
        MenuUtama menuUtama = new MenuUtama();
        menuUtama.setVisible(true);
        dispose();  
        }
    }

    /**
     * Creates new form EmployeeInfo
     */
    public MenuKomplain() {
        initComponents();
        muatTabelTransaksi();
        muatTabelKomplain();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Body = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelKomplain = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelTransaksi = new javax.swing.JTable();
        bMuatTabelKomplain = new javax.swing.JButton();
        lIDPengambil = new javax.swing.JLabel();
        lIDTransaksi = new javax.swing.JLabel();
        lNamaPelanggan = new javax.swing.JLabel();
        lCariBerdasarkan = new javax.swing.JLabel();
        tIDKomplain = new javax.swing.JTextField();
        tIDTransaksi = new javax.swing.JTextField();
        tNamaPelanggan = new javax.swing.JTextField();
        tCari = new javax.swing.JTextField();
        bSimpan = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bCari = new javax.swing.JButton();
        cCari = new javax.swing.JComboBox<>();
        bKembali = new javax.swing.JButton();
        lKomplain = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tDetailKomplain = new javax.swing.JTextArea();
        Header = new javax.swing.JPanel();
        lMenuKomplain = new javax.swing.JLabel();
        lMinimize = new javax.swing.JLabel();
        lExit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        Body.setBackground(new java.awt.Color(131, 187, 230));
        Body.setPreferredSize(new java.awt.Dimension(800, 600));

        jScrollPane2.setEnabled(false);

        tabelKomplain.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tabelKomplain.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelKomplain.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelKomplainMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelKomplain);

        tabelTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelTransaksiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelTransaksi);

        bMuatTabelKomplain.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bMuatTabelKomplain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_cached_black_24dp.png"))); // NOI18N
        bMuatTabelKomplain.setText("Muat Tabel Komplain");
        bMuatTabelKomplain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMuatTabelKomplainActionPerformed(evt);
            }
        });

        lIDPengambil.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lIDPengambil.setText("ID Komplain");

        lIDTransaksi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lIDTransaksi.setText("ID Transaksi");

        lNamaPelanggan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lNamaPelanggan.setText("Nama Pelanggan");

        lCariBerdasarkan.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lCariBerdasarkan.setText("Cari Berdasarkan");

        tIDKomplain.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tIDTransaksi.setEditable(false);
        tIDTransaksi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tIDTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tIDTransaksiActionPerformed(evt);
            }
        });

        tNamaPelanggan.setEditable(false);
        tNamaPelanggan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        bSimpan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_done_black_24dp.png"))); // NOI18N
        bSimpan.setText("Simpan");
        bSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bSimpanActionPerformed(evt);
            }
        });

        bUbah.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bUbah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_edit_black_24dp.png"))); // NOI18N
        bUbah.setText("Ubah");
        bUbah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bUbahActionPerformed(evt);
            }
        });

        bHapus.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_delete_black_24dp.png"))); // NOI18N
        bHapus.setText("Hapus");
        bHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHapusActionPerformed(evt);
            }
        });

        bCari.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_search_black_24dp.png"))); // NOI18N
        bCari.setText("Cari");
        bCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCariActionPerformed(evt);
            }
        });

        cCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID Komplain", "ID Transaksi", "Nama Pelanggan" }));

        bKembali.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bKembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_arrow_back_black_24dp.png"))); // NOI18N
        bKembali.setText("Kembali");
        bKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bKembaliActionPerformed(evt);
            }
        });

        lKomplain.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lKomplain.setText("Detail Komplain");

        jLabel1.setText("Pilih Salah Satu Transaksi Berikut :");

        tDetailKomplain.setColumns(20);
        tDetailKomplain.setRows(5);
        jScrollPane3.setViewportView(tDetailKomplain);

        javax.swing.GroupLayout BodyLayout = new javax.swing.GroupLayout(Body);
        Body.setLayout(BodyLayout);
        BodyLayout.setHorizontalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lKomplain)
                            .addComponent(lNamaPelanggan)
                            .addComponent(lIDTransaksi)
                            .addComponent(lIDPengambil, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(61, 61, 61)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(bHapus, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(bSimpan, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(bUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(bKembali, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)))
                                    .addComponent(bMuatTabelKomplain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(cCari, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lCariBerdasarkan))
                                        .addGap(18, 18, 18)
                                        .addComponent(tCari)
                                        .addGap(18, 18, 18)
                                        .addComponent(bCari, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tNamaPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tIDTransaksi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(tIDKomplain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(678, 678, 678))))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(600, 600, 600))))
        );

        BodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lIDPengambil, lIDTransaksi, lKomplain, lNamaPelanggan});

        BodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane3, tIDKomplain, tIDTransaksi, tNamaPelanggan});

        BodyLayout.setVerticalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bMuatTabelKomplain)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lIDPengambil)
                            .addComponent(tIDKomplain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lIDTransaksi)
                            .addComponent(tIDTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lNamaPelanggan)
                            .addComponent(tNamaPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lKomplain)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGap(291, 291, 291)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(bSimpan)
                            .addComponent(bUbah))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(bHapus)
                            .addComponent(bKembali))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(lCariBerdasarkan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bCari)))))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        BodyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bCari, cCari, tCari});

        Header.setBackground(new java.awt.Color(50, 126, 172));
        Header.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                HeaderMouseDragged(evt);
            }
        });
        Header.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                HeaderMousePressed(evt);
            }
        });

        lMenuKomplain.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lMenuKomplain.setForeground(new java.awt.Color(255, 255, 255));
        lMenuKomplain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/Talk-80_icon-icons.com_57266.png"))); // NOI18N
        lMenuKomplain.setText("MENU KOMPLAIN");

        lMinimize.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_minimize_white_24dp.png"))); // NOI18N
        lMinimize.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lMinimizeMouseClicked(evt);
            }
        });

        lExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_close_white_24dp.png"))); // NOI18N
        lExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lExitMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeaderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lMenuKomplain)
                .addGap(228, 228, 228)
                .addComponent(lMinimize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lExit)
                .addContainerGap())
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lMenuKomplain)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lMinimize)
                    .addComponent(lExit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Body, javax.swing.GroupLayout.DEFAULT_SIZE, 1112, Short.MAX_VALUE)
            .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Body, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bMuatTabelKomplainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMuatTabelKomplainActionPerformed
        muatTabelKomplain();
    }//GEN-LAST:event_bMuatTabelKomplainActionPerformed

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        save();
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        update();
    }//GEN-LAST:event_bUbahActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        delete();
    }//GEN-LAST:event_bHapusActionPerformed

    private void bCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCariActionPerformed
        search();
    }//GEN-LAST:event_bCariActionPerformed

    private void tabelKomplainMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelKomplainMouseClicked
        selectTableItemPengambilan();
    }//GEN-LAST:event_tabelKomplainMouseClicked

    private void bKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKembaliActionPerformed
        kembali();
    }//GEN-LAST:event_bKembaliActionPerformed

    private void tIDTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tIDTransaksiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tIDTransaksiActionPerformed

    private void tabelTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelTransaksiMouseClicked
        selectTableItemTransaksi();
    }//GEN-LAST:event_tabelTransaksiMouseClicked

    private void lMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lMinimizeMouseClicked
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_lMinimizeMouseClicked

    private void lExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lExitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lExitMouseClicked

    private void HeaderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HeaderMousePressed
        xMouse=evt.getX();
        yMouse=evt.getY();
    }//GEN-LAST:event_HeaderMousePressed

    private void HeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HeaderMouseDragged
        int x=evt.getXOnScreen();
        int y=evt.getYOnScreen();
        setLocation(x-xMouse, y-yMouse);
    }//GEN-LAST:event_HeaderMouseDragged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuKomplain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuKomplain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuKomplain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuKomplain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuKomplain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Body;
    private javax.swing.JPanel Header;
    private javax.swing.JButton bCari;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bKembali;
    private javax.swing.JButton bMuatTabelKomplain;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bUbah;
    private javax.swing.JComboBox<String> cCari;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lCariBerdasarkan;
    private javax.swing.JLabel lExit;
    private javax.swing.JLabel lIDPengambil;
    private javax.swing.JLabel lIDTransaksi;
    private javax.swing.JLabel lKomplain;
    private javax.swing.JLabel lMenuKomplain;
    private javax.swing.JLabel lMinimize;
    private javax.swing.JLabel lNamaPelanggan;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextArea tDetailKomplain;
    private javax.swing.JTextField tIDKomplain;
    private javax.swing.JTextField tIDTransaksi;
    private javax.swing.JTextField tNamaPelanggan;
    private javax.swing.JTable tabelKomplain;
    private javax.swing.JTable tabelTransaksi;
    // End of variables declaration//GEN-END:variables
}
