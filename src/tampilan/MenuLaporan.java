package tampilan;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.Koneksi;
import koneksi.Sesi;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author muham_000
 */
public class MenuLaporan extends javax.swing.JFrame {

    Connection conn = new Koneksi().connect();
    protected int id_karyawan = Sesi.getIDLogin();
    protected int choice;
    Date tgl1, tgl2;

    //deklarasi posisi mouse
    private int xMouse, yMouse;

    // Panel Karyawan
    // create the table data Karyawan
    Object[] rowKaryawan = {"ID", "Nama", "Alamat", "No Telpon", "Role"};
    DefaultTableModel tabmodeKaryawan = new DefaultTableModel(null, rowKaryawan);

    // to show data table Karyawan
    protected void muatTabelKaryawan() {
        tabelKaryawan.setModel(tabmodeKaryawan);
        String query = "select * from karyawan";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("id_karyawan");
                String b = rs.getString("nama_karyawan");
                String c = rs.getString("alamat_karyawan");
                String d = rs.getString("no_telpon_karyawan");
                String e = rs.getString("role");
                String[] data = {a, b, c, d, e};
                tabmodeKaryawan.addRow(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Panel Layanan
    // create the table data Layanan
    Object[] rowLayanan = {"ID", "Nama", "Deskripsi", "Harga", "Satuan"};
    DefaultTableModel tabmodeLayanan = new DefaultTableModel(null, rowLayanan);

    // to show data table Layanan
    protected void muatTabelLayanan() {
        tabelLayanan.setModel(tabmodeLayanan);
        String query = "select * from layanan";
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
                tabmodeLayanan.addRow(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Panel Pelanggan
    // create the table data Pelanggan
    Object[] rowPelanggan = {"ID", "Nama", "Alamat", "No Telpon"};
    DefaultTableModel tabmodePelanggan = new DefaultTableModel(null, rowPelanggan);

    // to show data table Pelanggan
    protected void muatTabelPelanggan() {
        tabelPelanggan.setModel(tabmodePelanggan);
        String query = "select * from pelanggan";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("id_pelanggan");
                String b = rs.getString("nama_pelanggan");
                String c = rs.getString("alamat_pelanggan");
                String d = rs.getString("no_telpon_pelanggan");
                String[] data = {a, b, c, d};
                tabmodePelanggan.addRow(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Panel Transaksi
    // create the table data transaksi
    Object[] rowTransaksi = {"ID Transaksi", "Nama Pelanggan", "Total Harga", "Status Pembayaran",
        "Tanggal Transaksi", "Nama Karyawan"};
    DefaultTableModel tabmodeTransaksi = new DefaultTableModel(null, rowTransaksi);

    // untuk memuat data table transaksi
    protected void muatTabelTransaksi() {
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

    // Panel Pengambilan
    // create the table data pengambilan
    Object[] rowPengambilan = {"ID Pengambilan", "ID Transaksi", "Nama Pelanggan", "Status Pembayaran", "Nama Pengambil", "Tanggal Pengambilan"};
    DefaultTableModel tabmodePengambilan = new DefaultTableModel(null, rowPengambilan);

    // to show data table pengambilan
    protected void muatTabelPengambilan() {
        tabelPengambilan.setModel(tabmodePengambilan);
        String query = "SELECT pa.id_pengambilan, pa.id_transaksi, pa.nama_pengambil, pa.tanggal_pengambilan, p.nama_pelanggan, "
                + "t.status_pembayaran FROM pengambilan pa "
                + "INNER JOIN transaksi t ON pa.id_transaksi = t.id_transaksi "
                + "INNER JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                + "ORDER BY pa.id_pengambilan ASC";

        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("pa.id_pengambilan");
                String b = rs.getString("pa.id_transaksi");
                String c = rs.getString("p.nama_pelanggan");
                String d = rs.getString("t.status_pembayaran");
                String e = rs.getString("pa.nama_pengambil");
                String f = rs.getString("pa.tanggal_pengambilan");
                String[] data = {a, b, c, d, e, f};
                tabmodePengambilan.addRow(data);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // Panel Komplain
    // create the table data pengambilan
    Object[] rowKomplain = {"ID Komplain", "ID Transaksi", "Nama Pelanggan", "Detail Komplain", "Tanggal Komplain"};
    DefaultTableModel tabmodeKomplain = new DefaultTableModel(null, rowKomplain);

    // to show data table pengambilan
    protected void muatTabelKomplain() {
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

    //to print the karyawan report
    public void printKaryawan() {
        int choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Mencetak Data Tersebut?", "Cetak", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            try {
                String currentDir = System.getProperty("user.dir");
                String reportPath = currentDir + "/src/report/LaporanDataKaryawan.jrxml";

                JasperDesign jDesign = JRXmlLoader.load(reportPath);
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, conn);
                JasperViewer.viewReport(jPrint, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }

//to print the layanan report
    public void printLayanan() {
        int choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Mencetak Data Tersebut?", "Cetak", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            try {
                String currentDir = System.getProperty("user.dir");
                String reportPath = currentDir + "/src/report/LaporanDataLayanan.jrxml";

                JasperDesign jDesign = JRXmlLoader.load(reportPath);
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, conn);
                JasperViewer.viewReport(jPrint, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }

//to print the pelanggan report
    public void printPelanggan() {
        int choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Mencetak Data Tersebut?", "Cetak", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            try {
                String currentDir = System.getProperty("user.dir");
                String reportPath = currentDir + "/src/report/LaporanDataPelanggan.jrxml";

                JasperDesign jDesign = JRXmlLoader.load(reportPath);
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, null, conn);
                JasperViewer.viewReport(jPrint, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }

//to print the transaksi report
    public void printTransaksi() {
        int choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Mencetak Data Tersebut?", "Cetak", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            HashMap<String, Object> param = new HashMap<>();
            param.put("tgl1", tgl1);
            param.put("tgl2", tgl2);
            try {
                String currentDir = System.getProperty("user.dir");
                String reportPath = currentDir + "/src/report/LaporanDataTransaksi.jrxml";

                JasperDesign jDesign = JRXmlLoader.load(reportPath);
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, param, conn);
                JasperViewer.viewReport(jPrint, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }

//to print the pengambilan report
    public void printPengambilan() {
        int choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Mencetak Data Tersebut?", "Cetak", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            HashMap<String, Object> param = new HashMap<>();
            param.put("tgl1", tgl1);
            param.put("tgl2", tgl2);
            try {
                String currentDir = System.getProperty("user.dir");
                String reportPath = currentDir + "/src/report/LaporanDataPengambilan.jrxml";

                JasperDesign jDesign = JRXmlLoader.load(reportPath);
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, param, conn);
                JasperViewer.viewReport(jPrint, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }

//to print the komplain report
    public void printKomplain() {
        int choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Mencetak Data Tersebut?", "Cetak", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            HashMap<String, Object> param = new HashMap<>();
            param.put("tgl1", tgl1);
            param.put("tgl2", tgl2);
            try {
                String currentDir = System.getProperty("user.dir");
                String reportPath = currentDir + "/src/report/LaporanDataKomplain.jrxml";

                JasperDesign jDesign = JRXmlLoader.load(reportPath);
                JasperReport jReport = JasperCompileManager.compileReport(jDesign);
                JasperPrint jPrint = JasperFillManager.fillReport(jReport, param, conn);
                JasperViewer.viewReport(jPrint, false);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                System.out.println(e.getMessage());
            }
        }
    }

    protected void kembali() {
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Kembali Ke Menu Utama ?", "Kembali", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            MenuUtama menuUtama = new MenuUtama();
            menuUtama.setVisible(true);
            dispose();
        }
    }

    /**
     * Creates new form EmployeeInfo
     */
    public MenuLaporan() {
        initComponents();
        muatTabelKaryawan();
        muatTabelLayanan();
        muatTabelPelanggan();
        muatTabelTransaksi();
        muatTabelPengambilan();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        pKaryawan = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelKaryawan = new javax.swing.JTable();
        bPrintKaryawan = new javax.swing.JButton();
        pLayanan = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabelLayanan = new javax.swing.JTable();
        bPrintLayanan = new javax.swing.JButton();
        pPelanggan = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabelPelanggan = new javax.swing.JTable();
        bPrintPelanggan = new javax.swing.JButton();
        pTransaksi = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabelTransaksi = new javax.swing.JTable();
        bPrintTransaksi = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dTransaksi1 = new com.toedter.calendar.JDateChooser();
        dTransaksi2 = new com.toedter.calendar.JDateChooser();
        pPengambilan = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabelPengambilan = new javax.swing.JTable();
        bPrintPengambilan = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        dPengambilan1 = new com.toedter.calendar.JDateChooser();
        jLabel5 = new javax.swing.JLabel();
        dPengambilan2 = new com.toedter.calendar.JDateChooser();
        pKomplain = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tabelKomplain = new javax.swing.JTable();
        bPrintKomplain = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        dKomplain1 = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        dKomplain2 = new com.toedter.calendar.JDateChooser();
        bKembali = new javax.swing.JButton();
        Header = new javax.swing.JPanel();
        lJudul = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        lMinimize = new javax.swing.JLabel();
        lExit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        Body.setBackground(new java.awt.Color(131, 187, 230));
        Body.setPreferredSize(new java.awt.Dimension(800, 600));

        pKaryawan.setBackground(new java.awt.Color(131, 187, 230));
        pKaryawan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

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
        jScrollPane2.setViewportView(tabelKaryawan);

        bPrintKaryawan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_print_black_24dp.png"))); // NOI18N
        bPrintKaryawan.setText("Cetak");
        bPrintKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrintKaryawanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pKaryawanLayout = new javax.swing.GroupLayout(pKaryawan);
        pKaryawan.setLayout(pKaryawanLayout);
        pKaryawanLayout.setHorizontalGroup(
            pKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pKaryawanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bPrintKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(607, Short.MAX_VALUE))
            .addGroup(pKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pKaryawanLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pKaryawanLayout.setVerticalGroup(
            pKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pKaryawanLayout.createSequentialGroup()
                .addContainerGap(425, Short.MAX_VALUE)
                .addComponent(bPrintKaryawan)
                .addContainerGap())
            .addGroup(pKaryawanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pKaryawanLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(58, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Karyawan", pKaryawan);

        pLayanan.setBackground(new java.awt.Color(131, 187, 230));
        pLayanan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jScrollPane3.setEnabled(false);

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
        jScrollPane3.setViewportView(tabelLayanan);

        bPrintLayanan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_print_black_24dp.png"))); // NOI18N
        bPrintLayanan.setText("Cetak");
        bPrintLayanan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrintLayananActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pLayananLayout = new javax.swing.GroupLayout(pLayanan);
        pLayanan.setLayout(pLayananLayout);
        pLayananLayout.setHorizontalGroup(
            pLayananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pLayananLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bPrintLayanan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(607, Short.MAX_VALUE))
            .addGroup(pLayananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pLayananLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pLayananLayout.setVerticalGroup(
            pLayananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pLayananLayout.createSequentialGroup()
                .addContainerGap(425, Short.MAX_VALUE)
                .addComponent(bPrintLayanan)
                .addContainerGap())
            .addGroup(pLayananLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pLayananLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(58, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Layanan", pLayanan);

        pPelanggan.setBackground(new java.awt.Color(131, 187, 230));
        pPelanggan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jScrollPane4.setEnabled(false);

        tabelPelanggan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tabelPelanggan.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(tabelPelanggan);

        bPrintPelanggan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_print_black_24dp.png"))); // NOI18N
        bPrintPelanggan.setText("Cetak");
        bPrintPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrintPelangganActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pPelangganLayout = new javax.swing.GroupLayout(pPelanggan);
        pPelanggan.setLayout(pPelangganLayout);
        pPelangganLayout.setHorizontalGroup(
            pPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPelangganLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bPrintPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(607, Short.MAX_VALUE))
            .addGroup(pPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pPelangganLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pPelangganLayout.setVerticalGroup(
            pPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pPelangganLayout.createSequentialGroup()
                .addContainerGap(425, Short.MAX_VALUE)
                .addComponent(bPrintPelanggan)
                .addContainerGap())
            .addGroup(pPelangganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pPelangganLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(58, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Pelanggan", pPelanggan);

        pTransaksi.setBackground(new java.awt.Color(131, 187, 230));
        pTransaksi.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jScrollPane5.setEnabled(false);

        tabelTransaksi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
        jScrollPane5.setViewportView(tabelTransaksi);

        bPrintTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_print_black_24dp.png"))); // NOI18N
        bPrintTransaksi.setText("Cetak");
        bPrintTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrintTransaksiActionPerformed(evt);
            }
        });

        jLabel2.setText("Periode :");

        jLabel3.setText("s/d");

        dTransaksi1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dTransaksi1PropertyChange(evt);
            }
        });

        dTransaksi2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dTransaksi2PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout pTransaksiLayout = new javax.swing.GroupLayout(pTransaksi);
        pTransaksi.setLayout(pTransaksiLayout);
        pTransaksiLayout.setHorizontalGroup(
            pTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bPrintTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pTransaksiLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dTransaksi1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(dTransaksi2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(346, Short.MAX_VALUE))
            .addGroup(pTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pTransaksiLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pTransaksiLayout.setVerticalGroup(
            pTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pTransaksiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addComponent(dTransaksi1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dTransaksi2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                .addComponent(bPrintTransaksi)
                .addContainerGap())
            .addGroup(pTransaksiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pTransaksiLayout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(58, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Transaksi", pTransaksi);

        pPengambilan.setBackground(new java.awt.Color(131, 187, 230));
        pPengambilan.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jScrollPane6.setEnabled(false);

        tabelPengambilan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tabelPengambilan.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane6.setViewportView(tabelPengambilan);

        bPrintPengambilan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_print_black_24dp.png"))); // NOI18N
        bPrintPengambilan.setText("Cetak");
        bPrintPengambilan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrintPengambilanActionPerformed(evt);
            }
        });

        jLabel4.setText("Periode :");

        dPengambilan1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dPengambilan1PropertyChange(evt);
            }
        });

        jLabel5.setText("s/d");

        dPengambilan2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dPengambilan2PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout pPengambilanLayout = new javax.swing.GroupLayout(pPengambilan);
        pPengambilan.setLayout(pPengambilanLayout);
        pPengambilanLayout.setHorizontalGroup(
            pPengambilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPengambilanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pPengambilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bPrintPengambilan, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pPengambilanLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dPengambilan1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(dPengambilan2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(346, Short.MAX_VALUE))
            .addGroup(pPengambilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pPengambilanLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pPengambilanLayout.setVerticalGroup(
            pPengambilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pPengambilanLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pPengambilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pPengambilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(dPengambilan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dPengambilan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                .addComponent(bPrintPengambilan)
                .addContainerGap())
            .addGroup(pPengambilanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pPengambilanLayout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(58, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Pengambilan", pPengambilan);

        pKomplain.setBackground(new java.awt.Color(131, 187, 230));
        pKomplain.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

        jScrollPane7.setEnabled(false);

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
        jScrollPane7.setViewportView(tabelKomplain);

        bPrintKomplain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_print_black_24dp.png"))); // NOI18N
        bPrintKomplain.setText("Cetak");
        bPrintKomplain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrintKomplainActionPerformed(evt);
            }
        });

        jLabel6.setText("Periode :");

        dKomplain1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dKomplain1PropertyChange(evt);
            }
        });

        jLabel7.setText("s/d");

        dKomplain2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dKomplain2PropertyChange(evt);
            }
        });

        javax.swing.GroupLayout pKomplainLayout = new javax.swing.GroupLayout(pKomplain);
        pKomplain.setLayout(pKomplainLayout);
        pKomplainLayout.setHorizontalGroup(
            pKomplainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pKomplainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pKomplainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bPrintKomplain, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pKomplainLayout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dKomplain1, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(dKomplain2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(346, Short.MAX_VALUE))
            .addGroup(pKomplainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pKomplainLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 745, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pKomplainLayout.setVerticalGroup(
            pKomplainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pKomplainLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pKomplainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pKomplainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(dKomplain1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dKomplain2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 390, Short.MAX_VALUE)
                .addComponent(bPrintKomplain)
                .addContainerGap())
            .addGroup(pKomplainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pKomplainLayout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(58, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Komplain", pKomplain);

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
                    .addComponent(jTabbedPane1)
                    .addComponent(bKembali, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        BodyLayout.setVerticalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bKembali)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
        lJudul.setText("MENU LAPORAN");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/analytics_118088.png"))); // NOI18N

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
                        .addGap(13, 13, 13)
                        .addComponent(jLabel1))
                    .addComponent(lMinimize)
                    .addComponent(lExit, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(HeaderLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(lJudul)))
                .addContainerGap())
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Body, javax.swing.GroupLayout.PREFERRED_SIZE, 566, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lMinimizeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lMinimizeMouseClicked
        this.setExtendedState(JFrame.ICONIFIED);
    }//GEN-LAST:event_lMinimizeMouseClicked

    private void lExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lExitMouseClicked
        System.exit(0);
    }//GEN-LAST:event_lExitMouseClicked

    private void HeaderMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HeaderMousePressed
        xMouse = evt.getX();
        yMouse = evt.getY();
    }//GEN-LAST:event_HeaderMousePressed

    private void HeaderMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_HeaderMouseDragged
        int x = evt.getXOnScreen();
        int y = evt.getYOnScreen();
        setLocation(x - xMouse, y - yMouse);
    }//GEN-LAST:event_HeaderMouseDragged

    private void bKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKembaliActionPerformed
        kembali();
    }//GEN-LAST:event_bKembaliActionPerformed

    private void bPrintKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrintKaryawanActionPerformed
        printKaryawan();
    }//GEN-LAST:event_bPrintKaryawanActionPerformed

    private void bPrintLayananActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrintLayananActionPerformed
        printLayanan();
    }//GEN-LAST:event_bPrintLayananActionPerformed

    private void bPrintPelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrintPelangganActionPerformed
        printPelanggan();
    }//GEN-LAST:event_bPrintPelangganActionPerformed

    private void bPrintTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrintTransaksiActionPerformed
        printTransaksi();
    }//GEN-LAST:event_bPrintTransaksiActionPerformed

    private void dTransaksi1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dTransaksi1PropertyChange
        if (dTransaksi1.getDate() != null) {
            tgl1 = dTransaksi1.getDate();
        }
    }//GEN-LAST:event_dTransaksi1PropertyChange

    private void dTransaksi2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dTransaksi2PropertyChange
        if (dTransaksi2.getDate() != null) {
            tgl2 = dTransaksi2.getDate();
        }
    }//GEN-LAST:event_dTransaksi2PropertyChange

    private void dPengambilan1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dPengambilan1PropertyChange
        if (dPengambilan1.getDate() != null) {
            tgl1 = dPengambilan1.getDate();
        }
    }//GEN-LAST:event_dPengambilan1PropertyChange

    private void dPengambilan2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dPengambilan2PropertyChange
        if (dPengambilan2.getDate() != null) {
            tgl2 = dPengambilan2.getDate();
        }
    }//GEN-LAST:event_dPengambilan2PropertyChange

    private void bPrintPengambilanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrintPengambilanActionPerformed
        printPengambilan();
    }//GEN-LAST:event_bPrintPengambilanActionPerformed

    private void dKomplain1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dKomplain1PropertyChange
        if (dKomplain1.getDate() != null) {
            tgl1 = dKomplain1.getDate();
        }
    }//GEN-LAST:event_dKomplain1PropertyChange

    private void dKomplain2PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dKomplain2PropertyChange
        if (dKomplain2.getDate() != null) {
            tgl2 = dKomplain2.getDate();
        }
    }//GEN-LAST:event_dKomplain2PropertyChange

    private void bPrintKomplainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrintKomplainActionPerformed
        printKomplain();
    }//GEN-LAST:event_bPrintKomplainActionPerformed

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
            java.util.logging.Logger.getLogger(MenuLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new MenuLaporan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Body;
    private javax.swing.JPanel Header;
    private javax.swing.JButton bKembali;
    private javax.swing.JButton bPrintKaryawan;
    private javax.swing.JButton bPrintKomplain;
    private javax.swing.JButton bPrintLayanan;
    private javax.swing.JButton bPrintPelanggan;
    private javax.swing.JButton bPrintPengambilan;
    private javax.swing.JButton bPrintTransaksi;
    private com.toedter.calendar.JDateChooser dKomplain1;
    private com.toedter.calendar.JDateChooser dKomplain2;
    private com.toedter.calendar.JDateChooser dPengambilan1;
    private com.toedter.calendar.JDateChooser dPengambilan2;
    private com.toedter.calendar.JDateChooser dTransaksi1;
    private com.toedter.calendar.JDateChooser dTransaksi2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lExit;
    private javax.swing.JLabel lJudul;
    private javax.swing.JLabel lMinimize;
    private javax.swing.JPanel pKaryawan;
    private javax.swing.JPanel pKomplain;
    private javax.swing.JPanel pLayanan;
    private javax.swing.JPanel pPelanggan;
    private javax.swing.JPanel pPengambilan;
    private javax.swing.JPanel pTransaksi;
    private javax.swing.JTable tabelKaryawan;
    private javax.swing.JTable tabelKomplain;
    private javax.swing.JTable tabelLayanan;
    private javax.swing.JTable tabelPelanggan;
    private javax.swing.JTable tabelPengambilan;
    private javax.swing.JTable tabelTransaksi;
    // End of variables declaration//GEN-END:variables
}
