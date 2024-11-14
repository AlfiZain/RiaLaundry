package tampilan;

import java.sql.*;
import javax.swing.JFrame;
import javax.swing.ButtonModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;

/**
 *
 * @author muham_000
 */
public class MenuLayanan extends javax.swing.JFrame {

    Connection conn = new Koneksi().connect();
    protected int choice;
    protected String query;
    
    //deklarasi posisi mouse
    private int xMouse, yMouse;
    
    // create the table data
    Object[] row = {"ID", "Nama", "Deskripsi", "Harga", "Satuan"};
    DefaultTableModel tabmode = new DefaultTableModel(null, row);

    // to show data table
    protected void muatTabelLayanan() {
        clearTable();
        tabelLayanan.setModel(tabmode);
        query = "select * from layanan";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("id_layanan");
                String b = rs.getString("nama_layanan");
                String c = rs.getString("deskripsi_layanan");
                String d = "Rp. ";
                d += rs.getString("harga");
                String e = rs.getString("satuan");
                String[] data = {a, b, c, d, e};
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
        taDeskripsi.setText("");
        tHarga.setText("");
        bgSatuan.clearSelection();
    }
    
    // to save the form
    protected void save() {
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Menyimpan Data Tersebut ?", "Simpan", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            query = "insert into layanan (id_layanan,nama_layanan,deskripsi_layanan,harga,satuan) values (?,?,?,?,?)";
        try {
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, tID.getText());
            pst.setString(2, tNama.getText());
            pst.setString(3, taDeskripsi.getText());
            pst.setInt(4, Integer.parseInt(tHarga.getText()));
            if (rKilogram.isSelected()) {
                pst.setString(5, rKilogram.getText());
            } else if (rBuah.isSelected()) {
                pst.setString(5, rBuah.getText());
            }

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Telah Berhasil Disimpan");
            muatTabelLayanan();
        } catch (Exception e) {
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
            query = "update layanan set id_layanan=?, nama_layanan=?, deskripsi_layanan=?,"
                    + " harga=?, satuan=? where id_layanan=?";
            PreparedStatement pst = conn.prepareStatement(query);

            pst.setString(1, tID.getText());
            pst.setString(2, tNama.getText());
            pst.setString(3, taDeskripsi.getText());
            pst.setInt(4, Integer.parseInt(tHarga.getText()));
            if (rKilogram.isSelected()) {
                pst.setString(5, rKilogram.getText());
            } else if (rBuah.isSelected()) {
                pst.setString(5, rBuah.getText());
            }
            pst.setString(6, tID.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Telah Berhasil Diperbarui");
            muatTabelLayanan();
        } catch (Exception e) {
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
                String query = "delete from layanan where id_layanan=" + tID.getText();
                Statement stat = conn.createStatement();
                stat.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Data Telah Berhasil Dihapus");
            } catch (Exception e) {
                System.out.println(e);
            }
            muatTabelLayanan();
        }
    }

    // to search data in database and show them in table and textfield
    protected void search() {
        String chose = "id_layanan";
        clearTable();
        clear();
        tabelLayanan.setModel(tabmode);
        try {
            String query = "select * from layanan where ";
            // kondisional if untuk mengecek pilihan yang akan digunakan untuk melakukan pencarian
            if (cCari.getSelectedItem().equals("ID")) {
                chose = "id_layanan";
            } else if (cCari.getSelectedItem().equals("Nama")) {
                chose = "nama_layanan";
            } else if (cCari.getSelectedItem().equals("Deskripsi")) {
                chose = "deskripsi_layanan";
            } else if (cCari.getSelectedItem().equals("Harga")) {
                chose = "harga";
            } else if (cCari.getSelectedItem().equals("Satuan")) {
                chose = "satuan";
            }
            // penggabungan string query dengan string chose
            query += chose + " = ?";

            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, tCari.getText());

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String a = rs.getString("id_layanan");
                String b = rs.getString("nama_layanan");
                String c = rs.getString("deskripsi_layanan");
                String d = "Rp. ";
                d += rs.getString("harga");
                String e = rs.getString("satuan");
                String[] data = {a, b, c, d, e};
                tabmode.addRow(data);
                tID.setText(a);
                tNama.setText(b);
                taDeskripsi.setText(c);
                tHarga.setText(d);
                if (e.equalsIgnoreCase("kilogram")) {
                    rKilogram.setSelected(true);
                } else {
                    rBuah.setSelected(true);
                }
            }
        } catch (Exception e) {
        }
    }

    // to select the table item and show them in textfield
    protected void selectTableItem() {
        try {
            int row = tabelLayanan.getSelectedRow();
            tID.setText((String) tabelLayanan.getModel().getValueAt(row, 0));
            tNama.setText((String) tabelLayanan.getModel().getValueAt(row, 1));
            taDeskripsi.setText((String) tabelLayanan.getModel().getValueAt(row, 2));
            String harga = (String) tabelLayanan.getModel().getValueAt(row, 3);
            harga = harga.substring(4); // Menghapus 3 karakter pertama (Rp.)
            tHarga.setText(harga);
            if (tabelLayanan.getModel().getValueAt(row, 4).equals("Kilogram")) {
                bgSatuan.setSelected(rKilogram.getModel(), true);
            } else {
                bgSatuan.setSelected(rBuah.getModel(), true);
            }
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
    public MenuLayanan() {
        initComponents();
        muatTabelLayanan();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bgSatuan = new javax.swing.ButtonGroup();
        Body = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelLayanan = new javax.swing.JTable();
        bMuatTabelLayanan = new javax.swing.JButton();
        lID = new javax.swing.JLabel();
        lNama = new javax.swing.JLabel();
        lDeskripsi = new javax.swing.JLabel();
        lHarga = new javax.swing.JLabel();
        lCariBerdasarkan = new javax.swing.JLabel();
        tID = new javax.swing.JTextField();
        tNama = new javax.swing.JTextField();
        tHarga = new javax.swing.JTextField();
        tCari = new javax.swing.JTextField();
        bSimpan = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bCari = new javax.swing.JButton();
        cCari = new javax.swing.JComboBox<>();
        bKembali = new javax.swing.JButton();
        lSatuan = new javax.swing.JLabel();
        rKilogram = new javax.swing.JRadioButton();
        rBuah = new javax.swing.JRadioButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDeskripsi = new javax.swing.JTextArea();
        Header = new javax.swing.JPanel();
        lJudul = new javax.swing.JLabel();
        lMinimize = new javax.swing.JLabel();
        lExit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        Body.setBackground(new java.awt.Color(131, 187, 230));
        Body.setPreferredSize(new java.awt.Dimension(900, 600));

        jScrollPane2.setEnabled(false);

        tabelLayanan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tabelLayanan.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelLayanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelLayananMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelLayanan);

        bMuatTabelLayanan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bMuatTabelLayanan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_cached_black_24dp.png"))); // NOI18N
        bMuatTabelLayanan.setText("Muat Tabel Layanan");
        bMuatTabelLayanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMuatTabelLayananActionPerformed(evt);
            }
        });

        lID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lID.setText("ID Layanan");

        lNama.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lNama.setText("Nama Layanan");

        lDeskripsi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lDeskripsi.setText("Deskripsi");

        lHarga.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lHarga.setText("Harga");

        lCariBerdasarkan.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lCariBerdasarkan.setText("Cari Berdasarkan");

        tID.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tNama.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tHarga.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

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

        cCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nama", "Deskripsi", "Harga", "Satuan" }));

        bKembali.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bKembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_arrow_back_black_24dp.png"))); // NOI18N
        bKembali.setText("Kembali");
        bKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bKembaliActionPerformed(evt);
            }
        });

        lSatuan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lSatuan.setText("Satuan");

        bgSatuan.add(rKilogram);
        rKilogram.setText("Kilogram");

        bgSatuan.add(rBuah);
        rBuah.setText("Buah");

        taDeskripsi.setColumns(20);
        taDeskripsi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        taDeskripsi.setRows(5);
        jScrollPane1.setViewportView(taDeskripsi);

        javax.swing.GroupLayout BodyLayout = new javax.swing.GroupLayout(Body);
        Body.setLayout(BodyLayout);
        BodyLayout.setHorizontalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bKembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bUbah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lHarga)
                                    .addComponent(lSatuan))
                                .addGap(18, 18, 18)
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tHarga)
                                    .addComponent(rBuah)
                                    .addComponent(rKilogram, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(tID, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(lNama)
                                .addGap(18, 18, 18)
                                .addComponent(tNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(lDeskripsi)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(48, 48, 48)
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(BodyLayout.createSequentialGroup()
                            .addComponent(cCari, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(tCari)
                            .addGap(18, 18, 18)
                            .addComponent(bCari, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                        .addComponent(bMuatTabelLayanan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(lCariBerdasarkan))
                .addContainerGap())
        );

        BodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lDeskripsi, lHarga, lID, lNama, lSatuan});

        BodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane1, rBuah, rKilogram, tHarga, tID, tNama});

        BodyLayout.setVerticalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bMuatTabelLayanan)
                        .addGap(18, 18, 18)
                        .addComponent(lCariBerdasarkan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bCari)
                            .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(cCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lID)
                            .addComponent(tID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lNama)
                            .addComponent(tNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lDeskripsi)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(lHarga)
                            .addComponent(tHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lSatuan)
                            .addComponent(rKilogram))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rBuah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bSimpan)
                        .addGap(18, 18, 18)
                        .addComponent(bUbah)
                        .addGap(18, 18, 18)
                        .addComponent(bHapus)
                        .addGap(18, 18, 18)
                        .addComponent(bKembali)))
                .addContainerGap(49, Short.MAX_VALUE))
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

        lJudul.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lJudul.setForeground(new java.awt.Color(255, 255, 255));
        lJudul.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/1455554374_line-24_icon-icons.com_53306.png"))); // NOI18N
        lJudul.setText("MENU LAYANAN");

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
                .addContainerGap(302, Short.MAX_VALUE)
                .addComponent(lJudul)
                .addGap(162, 162, 162)
                .addComponent(lMinimize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lExit)
                .addContainerGap())
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, HeaderLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(lJudul, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
            .addComponent(Body, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Body, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bMuatTabelLayananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMuatTabelLayananActionPerformed
        //override initComponents().EmployeeData
        muatTabelLayanan();
    }//GEN-LAST:event_bMuatTabelLayananActionPerformed

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

    private void tabelLayananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelLayananMouseClicked
        selectTableItem();
    }//GEN-LAST:event_tabelLayananMouseClicked

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
            java.util.logging.Logger.getLogger(MenuLayanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuLayanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuLayanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuLayanan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuLayanan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Body;
    private javax.swing.JPanel Header;
    private javax.swing.JButton bCari;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bKembali;
    private javax.swing.JButton bMuatTabelLayanan;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bUbah;
    private javax.swing.ButtonGroup bgSatuan;
    private javax.swing.JComboBox<String> cCari;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lCariBerdasarkan;
    private javax.swing.JLabel lDeskripsi;
    private javax.swing.JLabel lExit;
    private javax.swing.JLabel lHarga;
    private javax.swing.JLabel lID;
    private javax.swing.JLabel lJudul;
    private javax.swing.JLabel lMinimize;
    private javax.swing.JLabel lNama;
    private javax.swing.JLabel lSatuan;
    private javax.swing.JRadioButton rBuah;
    private javax.swing.JRadioButton rKilogram;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tHarga;
    private javax.swing.JTextField tID;
    private javax.swing.JTextField tNama;
    private javax.swing.JTextArea taDeskripsi;
    private javax.swing.JTable tabelLayanan;
    // End of variables declaration//GEN-END:variables
}
