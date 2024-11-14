package tampilan;

import java.sql.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 *
 * @author muham_000
 */
public class MenuKaryawanUntukAdmin extends javax.swing.JFrame {

    Connection conn = new Koneksi().connect();
    protected int choice;
    protected String query;
    
    //deklarasi posisi mouse
    private int xMouse, yMouse;
    
    // create the table data
    Object[] row = {"ID", "Nama", "Alamat", "No Telpon", "Password", "Role"};
    DefaultTableModel tabmode = new DefaultTableModel(null, row);

    // to show data table
    protected void muatTabelKaryawan() {
        clearTable();
        tabelKaryawan.setModel(tabmode);
        query = "select * from karyawan";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("id_karyawan");
                String b = rs.getString("nama_karyawan");
                String c = rs.getString("alamat_karyawan");
                String d = rs.getString("no_telpon_karyawan");
                String e = rs.getString("kata_sandi");
                String f = rs.getString("role");
                String[] data = {a, b, c, d, e, f};
                tabmode.addRow(data);
                clear();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // to clear table data
    protected void clearTable() {
        int row = tabmode.getRowCount();
        for (int i = 0; i < row; i++) {
            tabmode.removeRow(0);
        }
    }
    
    // to clear the form
    protected void clear() {
        tID.setText("");
        tNama.setText("");
        tAlamat.setText("");
        tNoTelpon.setText("");
        tKataSandi.setText("");
    }

    // to save the form
    protected void save() {
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Menyimpan Data Tersebut ?", "Simpan", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0){
            query = "insert into karyawan (id_karyawan,nama_karyawan,alamat_karyawan,no_telpon_karyawan,kata_sandi,role) values (?,?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, tID.getText());
            pst.setString(2,tNama.getText());
            pst.setString(3,tAlamat.getText());
            pst.setString(4,tNoTelpon.getText());
            pst.setString(5,tKataSandi.getText());
            pst.setString(6,cRole.getSelectedItem().toString());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Telah Berhasil Disimpan");
            muatTabelKaryawan();
        } catch (SQLException  e) {
            JOptionPane.showMessageDialog(null, "Data Yang Anda Masukan Tidak Sesuai");
                System.out.println(e);
        }
        }
    }

    // to update the data in database
    protected void update() {
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Memperbarui Data Tersebut ?", "Ubah", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0){
            try {
            String query = "update karyawan set id_karyawan=?, nama_karyawan=?, alamat_karyawan=?,"
                            + " no_telpon_karyawan=?, kata_sandi=?, role=? where id_karyawan=?";
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, tID.getText());
            pst.setString(2, tNama.getText());
            pst.setString(3, tAlamat.getText());
            pst.setString(4, tNoTelpon.getText());
            pst.setString(5, tKataSandi.getText());
            pst.setString(6, cRole.getSelectedItem().toString());
            pst.setString(7, tID.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Telah Berhasil Diperbarui");
            muatTabelKaryawan();
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
                String query = "delete from karyawan where id_karyawan=" + tID.getText();
                Statement stat = conn.createStatement();
                stat.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Data Telah Berhasil Dihapus");
            } catch (Exception e) {
            }
            muatTabelKaryawan();
        }
    }

    // to search data in database and show them in table and textfield
    protected void search() {
        String chose = "id_karyawan";
        clearTable();
        clear();
        tabelKaryawan.setModel(tabmode);
        try {
            String query = "select * from karyawan where ";
            // kondisional if untuk mengecek pilihan yang akan digunakan untuk melakukan pencarian
            if (cCari.getSelectedItem().equals("ID")) {
                chose = "id_karyawan";
            } else if (cCari.getSelectedItem().equals("Nama")) {
                chose = "nama_karyawan";
            } else if (cCari.getSelectedItem().equals("Alamat")) {
                chose = "alamat_karyawan";
            } else if (cCari.getSelectedItem().equals("No Telpon")) {
                chose = "no_telpon_karyawan";
            } else if (cCari.getSelectedItem().equals("Kata Sandi")) {
                chose = "kata_sandi";
            } else if (cCari.getSelectedItem().equals("Role")) {
                chose = "role";
            }
            // penggabungan string query dengan string chose
            query += chose + " = ?";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1,tCari.getText());
            
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String a = rs.getString("id_karyawan");
                String b = rs.getString("nama_karyawan");
                String c = rs.getString("alamat_karyawan");
                String d = rs.getString("no_telpon_karyawan");
                String e = rs.getString("kata_sandi");
                String f = rs.getString("role");
                String[] data = {a, b, c, d, e, f};
                tabmode.addRow(data);
                tID.setText(a);
                tNama.setText(b);
                tAlamat.setText(c);
                tNoTelpon.setText(d);
                tKataSandi.setText(e);
                cRole.setSelectedItem(f);
            }
        } catch (Exception e) {
        }
    }

    // to select the table item and show them in textfield
    protected void selectTableItem() {
        try {
            int row = tabelKaryawan.getSelectedRow();
            tID.setText((String) tabelKaryawan.getModel().getValueAt(row, 0));
            tNama.setText((String) tabelKaryawan.getModel().getValueAt(row, 1));
            tAlamat.setText((String) tabelKaryawan.getModel().getValueAt(row, 2));
            tNoTelpon.setText((String) tabelKaryawan.getModel().getValueAt(row, 3));
            tKataSandi.setText((String) tabelKaryawan.getModel().getValueAt(row, 4));
            cRole.setSelectedItem((String) tabelKaryawan.getModel().getValueAt(row, 5));
        } catch (Exception e) {
        }
    }
    
    protected void kembali(){
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
    public MenuKaryawanUntukAdmin() {
        initComponents();
        muatTabelKaryawan();
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
        tabelKaryawan = new javax.swing.JTable();
        bMuatTabelKaryawan = new javax.swing.JButton();
        lID = new javax.swing.JLabel();
        lNama = new javax.swing.JLabel();
        lAlamat = new javax.swing.JLabel();
        lNoTelpon = new javax.swing.JLabel();
        lKataSandi = new javax.swing.JLabel();
        lRole = new javax.swing.JLabel();
        lCariBerdasarkan = new javax.swing.JLabel();
        tID = new javax.swing.JTextField();
        tNama = new javax.swing.JTextField();
        tAlamat = new javax.swing.JTextField();
        tNoTelpon = new javax.swing.JTextField();
        tKataSandi = new javax.swing.JTextField();
        tCari = new javax.swing.JTextField();
        bSimpan = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bCari = new javax.swing.JButton();
        cRole = new javax.swing.JComboBox<>();
        cCari = new javax.swing.JComboBox<>();
        bKembali = new javax.swing.JButton();
        Header = new javax.swing.JPanel();
        lJudul = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lMinimize = new javax.swing.JLabel();
        lExit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(800, 600));
        setResizable(false);

        Body.setBackground(new java.awt.Color(131, 187, 230));
        Body.setPreferredSize(new java.awt.Dimension(800, 600));

        jScrollPane2.setEnabled(false);

        tabelKaryawan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tabelKaryawan.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelKaryawan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelKaryawanMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelKaryawan);

        bMuatTabelKaryawan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bMuatTabelKaryawan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_cached_black_24dp.png"))); // NOI18N
        bMuatTabelKaryawan.setText("Muat Tabel Karyawan");
        bMuatTabelKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMuatTabelKaryawanActionPerformed(evt);
            }
        });

        lID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lID.setText("ID Karyawan");

        lNama.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lNama.setText("Nama Karyawan");

        lAlamat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lAlamat.setText("Alamat");

        lNoTelpon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lNoTelpon.setText("No Telpon");

        lKataSandi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lKataSandi.setText("Kata Sandi");

        lRole.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lRole.setText("Role");

        lCariBerdasarkan.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lCariBerdasarkan.setText("Cari Berdasarkan");

        tID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tNama.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tAlamat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tNoTelpon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tKataSandi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

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

        cRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "User", "Admin" }));
        cRole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cRoleActionPerformed(evt);
            }
        });

        cCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nama", "Alamat", "No Telpon", "Kata Sandi", "Role" }));

        bKembali.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bKembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_arrow_back_black_24dp.png"))); // NOI18N
        bKembali.setText("Kembali");
        bKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bKembaliActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BodyLayout = new javax.swing.GroupLayout(Body);
        Body.setLayout(BodyLayout);
        BodyLayout.setHorizontalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                        .addComponent(bSimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bMuatTabelKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 454, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lNoTelpon)
                                    .addComponent(lRole)
                                    .addComponent(lKataSandi))
                                .addGap(18, 18, 18)
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tNoTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tKataSandi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(lNama)
                                .addGap(18, 18, 18)
                                .addComponent(tNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(lAlamat)
                                .addGap(18, 18, 18)
                                .addComponent(tAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bKembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(cCari, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tCari)
                                .addGap(18, 18, 18)
                                .addComponent(bCari, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lCariBerdasarkan))))
                .addContainerGap())
        );

        BodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lAlamat, lID, lKataSandi, lNama, lNoTelpon, lRole});

        BodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bCari, cCari, cRole, tAlamat, tID, tKataSandi, tNama, tNoTelpon});

        BodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {bHapus, bSimpan, bUbah});

        BodyLayout.setVerticalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lID)
                            .addComponent(tID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lNama)
                            .addComponent(tNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lAlamat)
                            .addComponent(tAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lNoTelpon)
                            .addComponent(tNoTelpon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lKataSandi)
                            .addComponent(tKataSandi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(cRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lRole)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bMuatTabelKaryawan)
                    .addComponent(bSimpan))
                .addGap(18, 18, 18)
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addComponent(bUbah)
                        .addGap(18, 18, 18)
                        .addComponent(bHapus))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addComponent(lCariBerdasarkan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bCari)
                            .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addComponent(bKembali)
                .addContainerGap(85, Short.MAX_VALUE))
        );

        BodyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cRole, lRole});

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

        lJudul.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lJudul.setForeground(new java.awt.Color(255, 255, 255));
        lJudul.setText("MENU KARYAWAN");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/female_woman_avatar_people_person_white_tone_icon_159370.png"))); // NOI18N

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
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGap(226, 226, 226)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lJudul)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lMinimize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lExit)
                .addContainerGap())
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HeaderLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(lJudul, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HeaderLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addComponent(lMinimize)
                    .addComponent(lExit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Body, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bMuatTabelKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMuatTabelKaryawanActionPerformed
        //override initComponents().EmployeeData
        muatTabelKaryawan();
    }//GEN-LAST:event_bMuatTabelKaryawanActionPerformed

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        save();
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        update();
    }//GEN-LAST:event_bUbahActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        delete();
    }//GEN-LAST:event_bHapusActionPerformed

    private void cRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cRoleActionPerformed

    }//GEN-LAST:event_cRoleActionPerformed

    private void bCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCariActionPerformed
        search();
    }//GEN-LAST:event_bCariActionPerformed

    private void tabelKaryawanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelKaryawanMouseClicked
        selectTableItem();
    }//GEN-LAST:event_tabelKaryawanMouseClicked

    private void bKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKembaliActionPerformed
        kembali();
    }//GEN-LAST:event_bKembaliActionPerformed

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
            java.util.logging.Logger.getLogger(MenuKaryawanUntukAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuKaryawanUntukAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuKaryawanUntukAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuKaryawanUntukAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new MenuKaryawanUntukAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Body;
    private javax.swing.JPanel Header;
    private javax.swing.JButton bCari;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bKembali;
    private javax.swing.JButton bMuatTabelKaryawan;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bUbah;
    private javax.swing.JComboBox<String> cCari;
    private javax.swing.JComboBox<String> cRole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lAlamat;
    private javax.swing.JLabel lCariBerdasarkan;
    private javax.swing.JLabel lExit;
    private javax.swing.JLabel lID;
    private javax.swing.JLabel lJudul;
    private javax.swing.JLabel lKataSandi;
    private javax.swing.JLabel lMinimize;
    private javax.swing.JLabel lNama;
    private javax.swing.JLabel lNoTelpon;
    private javax.swing.JLabel lRole;
    private javax.swing.JTextField tAlamat;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tID;
    private javax.swing.JTextField tKataSandi;
    private javax.swing.JTextField tNama;
    private javax.swing.JTextField tNoTelpon;
    private javax.swing.JTable tabelKaryawan;
    // End of variables declaration//GEN-END:variables
}
