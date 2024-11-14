package tampilan;

import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.view.JasperViewer;
import koneksi.Koneksi;
import koneksi.Sesi;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 *
 * @author alfi_zain
 */
public class MenuTransaksi extends javax.swing.JFrame {

    Connection conn = new Koneksi().connect();
    protected int id_karyawan = Sesi.getIDLogin();
    protected int choice;

    //deklarasi posisi mouse
    private int xMouse, yMouse;

    // create the table data transaksi
    Object[] rowTransaksi = {"ID Transaksi", "Nama Pelanggan", "Total Harga", "Status Pembayaran",
        "Tanggal Transaksi", "Nama Karyawan"};
    DefaultTableModel tabmodeTransaksi = new DefaultTableModel(null, rowTransaksi);

    // create the table data detail transaksi
    Object[] rowDetailTransaksi = {"ID Transaksi", "Nama Layanan", "Jumlah(Kg/Satuan)", "Total"};
    DefaultTableModel tabmodeDetailTransaksi = new DefaultTableModel(null, rowDetailTransaksi);

    // untuk memuat data table detail transaksi
    protected void muatTabelDetailTransaksi() {
        clearTableDetailTransaksi();
        tabelDetailTransaksi.setModel(tabmodeDetailTransaksi);
        String query = "SELECT dt.id_transaksi, l.nama_layanan, dt.jumlah, dt.total FROM detail_transaksi dt "
                + "LEFT JOIN transaksi t ON dt.id_transaksi = t.id_transaksi "
                + "LEFT JOIN layanan l ON dt.id_layanan = l.id_layanan "
                + "ORDER BY dt.id_transaksi ASC";
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("dt.id_transaksi");
                String b = rs.getString("l.nama_layanan");
                String c = rs.getString("dt.jumlah");
                String d = "Rp. ";
                d += rs.getString("dt.total");
                String[] data = {a, b, c, d};
                tabmodeDetailTransaksi.addRow(data);
            }
        } catch (Exception e) {
        }
    }

    // untuk memuat satu data transaksi tabel detail transaksi
    protected void muatTabelDetailSatuTransaksi() {
        clearTableDetailTransaksi();
        tabelDetailTransaksi.setModel(tabmodeDetailTransaksi);
        String query = "SELECT dt.id_transaksi, l.nama_layanan, dt.jumlah, dt.total FROM detail_transaksi dt "
                + "LEFT JOIN transaksi t ON dt.id_transaksi = t.id_transaksi "
                + "LEFT JOIN layanan l ON dt.id_layanan = l.id_layanan "
                + "WHERE dt.id_transaksi = " + tIDTransaksi.getText();
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("dt.id_transaksi");
                String b = rs.getString("l.nama_layanan");
                String c = rs.getString("dt.jumlah");
                String d = "Rp. ";
                d += rs.getString("dt.total");
                String[] data = {a, b, c, d};
                tabmodeDetailTransaksi.addRow(data);
            }
        } catch (Exception e) {
        }
    }

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

    // to clear table data
    protected void clearTableTransaksi() {
        int row = tabmodeTransaksi.getRowCount();
        for (int i = 0; i < row; i++) {
            tabmodeTransaksi.removeRow(0);
        }
    }

    // to clear table data
    protected void clearTableDetailTransaksi() {
        int row = tabmodeDetailTransaksi.getRowCount();
        for (int i = 0; i < row; i++) {
            tabmodeDetailTransaksi.removeRow(0);
        }
    }

    // to fill the pelanggan combobox
    protected void mengisiCPelanggan() throws SQLException {
        String query = "select id_pelanggan, nama_pelanggan, alamat_pelanggan from pelanggan";
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(query);
        while (rs.next()) {
            String id = rs.getString("id_pelanggan");
            String nama = rs.getString("nama_pelanggan");
            String alamat = rs.getString("alamat_pelanggan");
            cNama.addItem(id + " - " + nama + " - " + alamat);
        }
    }

    // to fill the layanan combobox
    protected void mengisiCLayanan() throws SQLException {
        String query = "select id_layanan, nama_layanan, harga from layanan";
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(query);
        while (rs.next()) {
            String id = rs.getString("id_layanan");
            String nama = rs.getString("nama_layanan");
            String harga = rs.getString("harga");
            cLayanan.addItem(id + " - " + nama + " - " + harga);
        }
    }

    // to sum the total 
    protected int total() throws SQLException {
        int jumlah = Integer.parseInt(tJumlah.getText());
        int id_layanan = getIDLayanan();
        int harga = 0;
        int total = 0;
        String query = "select harga from layanan where id_layanan = " + id_layanan;
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(query);
        while (rs.next()) {
            harga = rs.getInt("harga");
        }
        return total = harga * jumlah;
    }

    // to autofill the total textfield and disable it from editing
    protected void mengisiTotal() throws SQLException {
        tTotal.setText(String.valueOf(total()));
        tTotal.enableInputMethods(false);
    }

    // to get the id pelanggan
    protected int getIDPelanggan() {
        // Mengambil ID dari pelanggan dengan method split
        String nPelanggan = cNama.getSelectedItem().toString();
        String[] nPelangganParts = nPelanggan.split(" - ");
        String id_pelanggan = nPelangganParts[0];
        return Integer.parseInt(id_pelanggan);
    }

    // to get the id layanan
    protected int getIDLayanan() {
        // Mengambil ID dari layanan dengan method split
        String jLayanan = cLayanan.getSelectedItem().toString();
        String[] jLayananParts = jLayanan.split(" - ");
        String id_layanan = jLayananParts[0];
        return Integer.parseInt(id_layanan);
    }

    // to get the tanggal
    protected String getTanggal() {
        // Mendapatkan tanggal saat ini
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = dateFormat.format(calendar.getTime());
        return tanggal;
    }

    // Metode untuk menghitung total harga
    public int totalHarga() throws SQLException {
        int totalHarga = 0;

        String query = "SELECT SUM(total) AS total_harga FROM detail_transaksi WHERE id_transaksi = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, tIDTransaksi.getText());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                totalHarga = rs.getInt("total_harga");
            }
        }

        return totalHarga;
    }

    // to save the form
    protected void save() throws SQLException {
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Menyimpan Data Tersebut ?", "Simpan", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            int id_pelanggan = getIDPelanggan();
            String tanggal = getTanggal();

            String query = "insert into transaksi (id_transaksi, id_pelanggan, total_harga, status_pembayaran,"
                    + " tanggal_transaksi, id_karyawan) values (?,?,?,?,?,?)";
            try {
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, tIDTransaksi.getText());
                pst.setInt(2, id_pelanggan);
                pst.setInt(3, totalHarga());
                pst.setString(4, cStatus.getSelectedItem().toString());
                pst.setString(5, tanggal);
                pst.setInt(6, id_karyawan);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
                muatTabelTransaksi();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Yang Anda Masukan Tidak Sesuai");
                System.out.println(e);
            }
        }
    }

    // to update the data in database
    protected void update() throws SQLException {
        int id_pelanggan = getIDPelanggan();
        String tanggal = getTanggal();

        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Memperbarui Data Tersebut ?", "Ubah", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            try {
                String query = "update transaksi set id_transaksi=?, id_pelanggan=?, total_harga=?, status_pembayaran=?, tanggal_transaksi=?,"
                        + " id_karyawan=? where id_transaksi=?";
                PreparedStatement pst = conn.prepareStatement(query);

                pst.setString(1, tIDTransaksi.getText());
                pst.setInt(2, id_pelanggan);
                pst.setInt(3, totalHarga());
                pst.setString(4, cStatus.getSelectedItem().toString());
                pst.setString(5, tanggal);
                pst.setInt(6, id_karyawan);
                pst.setString(7, tIDTransaksi.getText());

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data Telah Berhasil Diperbarui");
                muatTabelTransaksi();
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
                String query = "delete from transaksi where id_transaksi=" + tIDTransaksi.getText();
                Statement stat = conn.createStatement();
                stat.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Data Telah Berhasil Dihapus");
            } catch (Exception e) {
            }
            muatTabelTransaksi();
        }
    }

    //to print the transaction note
    public void print() {
        String id = tIDTransaksi.getText();
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Mencetak Data Tersebut ?", "Cetak", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            try {
                HashMap param = new HashMap();
                param.put("id", id);
                String currentDir = System.getProperty("user.dir");
                String reportPath = currentDir + "/src/report/NotaTransaksi.jrxml";

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

    // to add the data in database
    protected void tambah() {
        int id_layanan = getIDLayanan();
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Menambahkan Data Tersebut ?", "Tambah", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            String query = "insert into detail_transaksi (id_transaksi, id_layanan, jumlah, total) values (?,?,?,?)";
            try {
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, tIDTransaksi.getText());
                pst.setInt(2, id_layanan);
                pst.setString(3, tJumlah.getText());
                pst.setString(4, tTotal.getText());

                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Data Yang Anda Masukan Tidak Sesuai");
                System.out.println(e);
            }
            muatTabelDetailSatuTransaksi();
        }
    }

    // to remove the data in database
    protected void kurang() {
        int id_layanan = getIDLayanan();
        choice = JOptionPane.showConfirmDialog(null, "Apakah Anda Ingin Mengurangi Data Tersebut ?", "Kurang", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (choice == 0) {
            try {
                String query = "delete from detail_transaksi where id_transaksi = ? AND id_layanan = ? AND jumlah = ? AND total = ?";
                PreparedStatement pst = conn.prepareStatement(query);
                pst.setString(1, tIDTransaksi.getText());
                pst.setInt(2, id_layanan);
                pst.setString(3, tJumlah.getText());
                pst.setString(4, tTotal.getText());
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
            } catch (Exception e) {
            }
            muatTabelDetailSatuTransaksi();
        }
    }

    // to searchTransaksi data in database and show them in table and textfield
    protected void searchTransaksi() {
        String chose = "id_transaksi";
        clearTableTransaksi();
        clear();
        tabelTransaksi.setModel(tabmodeTransaksi);
        try {
            String query = "SELECT t.id_transaksi, p.nama_pelanggan, t.total_harga,"
                    + "t.status_pembayaran, t.tanggal_transaksi, k.nama_karyawan FROM transaksi t "
                    + "INNER JOIN pelanggan p ON t.id_pelanggan = p.id_pelanggan "
                    + "INNER JOIN karyawan k ON t.id_karyawan = k.id_karyawan "
                    + "WHERE ";
            // kondisional if untuk mengecek pilihan yang akan digunakan untuk melakukan pencarian
            if (cCari.getSelectedItem().equals("ID")) {
                chose = "t.id_transaksi";
            } else if (cCari.getSelectedItem().equals("Nama")) {
                chose = "p.nama_pelanggan";
            } else if (cCari.getSelectedItem().equals("Tanggal")) {
                chose = "t.tanggal_transaksi";
            } else if (cCari.getSelectedItem().equals("Status")) {
                chose = "t.status_pembayaran";
            }
            // penggabungan string query dengan string chose
            query += chose + " = ?";

            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, tCari.getText());

            ResultSet rs = pst.executeQuery();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // to searchDetailTransaksi data in database and show them in table and textfield
    protected void searchDetailTransaksi() {
        clearTableDetailTransaksi();
        tabelDetailTransaksi.setModel(tabmodeDetailTransaksi);
        String query = "SELECT dt.id_transaksi, l.nama_layanan, dt.jumlah, dt.total FROM detail_transaksi dt "
                + "LEFT JOIN transaksi t ON dt.id_transaksi = t.id_transaksi "
                + "LEFT JOIN layanan l ON dt.id_layanan = l.id_layanan "
                + "WHERE dt.id_transaksi = " + tCariDetailTransaksi.getText();
        try {
            java.sql.Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String a = rs.getString("dt.id_transaksi");
                String b = rs.getString("l.nama_layanan");
                String c = rs.getString("dt.jumlah");
                String d = "Rp. ";
                d += rs.getString("dt.total");
                String[] data = {a, b, c, d};
                tabmodeDetailTransaksi.addRow(data);
            }
        } catch (Exception e) {
        }
    }

    // to clear the form
    protected void clear() {
        tIDTransaksi.setText("");
        tJumlah.setText("");
        tTotal.setText("");
        cNama.setSelectedIndex(0);
        cLayanan.setSelectedIndex(0);
        cStatus.setSelectedIndex(0);
    }

    // to select the table item and show them in textfield
    protected void selectTableDetailItemTransaksi() {
        try {
            int row = tabelDetailTransaksi.getSelectedRow();
            tIDTransaksi.setText((String) tabelDetailTransaksi.getModel().getValueAt(row, 0));
            tJumlah.setText((String) tabelDetailTransaksi.getModel().getValueAt(row, 2));
            String total = (String) tabelDetailTransaksi.getModel().getValueAt(row, 3);
            total = total.substring(4); // Menghapus 3 karakter pertama (Rp.)
            tTotal.setText(total);

            String pilihLayanan = (String) tabelDetailTransaksi.getModel().getValueAt(row, 1);

            // Memilih indeks pada JComboBox cLayanan berdasarkan nilai yang ada di tabel
            for (int i = 0; i < cLayanan.getItemCount(); i++) {
                String item = (String) cLayanan.getItemAt(i);
                if (item.contains(pilihLayanan)) {
                    cLayanan.setSelectedIndex(i);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    // to select the table item and show them in textfield
    protected void selectTableItemTransaksi() {
        try {
            int row = tabelTransaksi.getSelectedRow();
            tIDTransaksi.setText((String) tabelTransaksi.getModel().getValueAt(row, 0));

            String pilihNama = (String) tabelTransaksi.getModel().getValueAt(row, 1);
            String pilihStatus = (String) tabelTransaksi.getModel().getValueAt(row, 3);

            // Memilih indeks pada JComboBox cNama berdasarkan nilai yang ada di tabel
            for (int i = 0; i < cNama.getItemCount(); i++) {
                String item = (String) cNama.getItemAt(i);
                if (item.contains(pilihNama)) {
                    cNama.setSelectedIndex(i);
                    break;
                }
            }

            // Memilih indeks pada JComboBox cStatus berdasarkan nilai yang ada di tabel
            for (int i = 0; i < cStatus.getItemCount(); i++) {
                String item = (String) cStatus.getItemAt(i);
                if (item.equals(pilihStatus)) {
                    cStatus.setSelectedIndex(i);
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    // to go back to main menu
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
    public MenuTransaksi() {
        initComponents();
        muatTabelTransaksi();
        muatTabelDetailTransaksi();
        try {
            mengisiCPelanggan();
            mengisiCLayanan();
        } catch (SQLException ex) {
            Logger.getLogger(MenuTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        tabelTransaksi = new javax.swing.JTable();
        bMuatTabelTransaksi = new javax.swing.JButton();
        lID = new javax.swing.JLabel();
        lNama = new javax.swing.JLabel();
        lLayanan = new javax.swing.JLabel();
        lJumlah = new javax.swing.JLabel();
        lTotal = new javax.swing.JLabel();
        lStatus = new javax.swing.JLabel();
        tIDTransaksi = new javax.swing.JTextField();
        cNama = new javax.swing.JComboBox<>();
        cLayanan = new javax.swing.JComboBox<>();
        tJumlah = new javax.swing.JTextField();
        tTotal = new javax.swing.JTextField();
        cStatus = new javax.swing.JComboBox<>();
        lCariBerdasarkan = new javax.swing.JLabel();
        cCari = new javax.swing.JComboBox<>();
        tCari = new javax.swing.JTextField();
        bSimpan = new javax.swing.JButton();
        bUbah = new javax.swing.JButton();
        bHapus = new javax.swing.JButton();
        bCari = new javax.swing.JButton();
        bKembali = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelDetailTransaksi = new javax.swing.JTable();
        bTambah = new javax.swing.JButton();
        bKurang = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        tCariDetailTransaksi = new javax.swing.JTextField();
        bCariDetailTransaksi = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        bMuatTabelDetailTransaksi = new javax.swing.JButton();
        bPrintKaryawan = new javax.swing.JButton();
        Header = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lMinimize = new javax.swing.JLabel();
        lExit = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        Body.setBackground(new java.awt.Color(131, 187, 230));
        Body.setPreferredSize(new java.awt.Dimension(1000, 600));

        jScrollPane2.setEnabled(false);

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
        tabelTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelTransaksiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelTransaksi);

        bMuatTabelTransaksi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bMuatTabelTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_cached_black_24dp.png"))); // NOI18N
        bMuatTabelTransaksi.setText("Muat Tabel Transaksi");
        bMuatTabelTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMuatTabelTransaksiActionPerformed(evt);
            }
        });

        lID.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lID.setText("ID Transaksi");

        lNama.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lNama.setText("Nama Pelanggan");

        lLayanan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lLayanan.setText("Jenis Layanan");

        lJumlah.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lJumlah.setText("Jumlah");

        lTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lTotal.setText("Total");

        lStatus.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lStatus.setText("Status");

        tIDTransaksi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        tJumlah.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tJumlahKeyReleased(evt);
            }
        });

        tTotal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        cStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Lunas", "Belum Lunas" }));

        lCariBerdasarkan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        lCariBerdasarkan.setText("Cari Transaksi Berdasarkan : ");

        cCari.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ID", "Nama", "Tanggal", "Status" }));
        cCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cCariActionPerformed(evt);
            }
        });

        tCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tCariActionPerformed(evt);
            }
        });

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

        bKembali.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bKembali.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_arrow_back_black_24dp.png"))); // NOI18N
        bKembali.setText("Kembali");
        bKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bKembaliActionPerformed(evt);
            }
        });

        tabelDetailTransaksi.setModel(new javax.swing.table.DefaultTableModel(
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
        tabelDetailTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelDetailTransaksiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelDetailTransaksi);

        bTambah.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bTambah.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_add_shopping_cart_black_24dp.png"))); // NOI18N
        bTambah.setText("Tambah");
        bTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bTambahActionPerformed(evt);
            }
        });

        bKurang.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bKurang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_remove_shopping_cart_black_24dp.png"))); // NOI18N
        bKurang.setText("Kurang");
        bKurang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bKurangActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("ID Transaksi : ");

        bCariDetailTransaksi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bCariDetailTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_search_black_24dp.png"))); // NOI18N
        bCariDetailTransaksi.setText("Cari");
        bCariDetailTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCariDetailTransaksiActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Detail Transaksi");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel4.setText("Tabel Transaksi");

        bMuatTabelDetailTransaksi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bMuatTabelDetailTransaksi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/outline_cached_black_24dp.png"))); // NOI18N
        bMuatTabelDetailTransaksi.setText("Muat Tabel Detail Transaksi");
        bMuatTabelDetailTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMuatTabelDetailTransaksiActionPerformed(evt);
            }
        });

        bPrintKaryawan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/baseline_print_black_24dp.png"))); // NOI18N
        bPrintKaryawan.setText("Cetak");
        bPrintKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrintKaryawanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BodyLayout = new javax.swing.GroupLayout(Body);
        Body.setLayout(BodyLayout);
        BodyLayout.setHorizontalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BodyLayout.createSequentialGroup()
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                                            .addComponent(lID)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(tIDTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(BodyLayout.createSequentialGroup()
                                            .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(bSimpan, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                                .addComponent(bHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(bUbah, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(bPrintKaryawan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(BodyLayout.createSequentialGroup()
                                            .addComponent(lStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(cStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(bKembali, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(BodyLayout.createSequentialGroup()
                                        .addComponent(lNama)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cNama, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(18, 18, 18)
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(BodyLayout.createSequentialGroup()
                                        .addComponent(bTambah, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(bKurang, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(BodyLayout.createSequentialGroup()
                                        .addComponent(lTotal)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tTotal))
                                    .addGroup(BodyLayout.createSequentialGroup()
                                        .addComponent(lJumlah)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tJumlah))
                                    .addGroup(BodyLayout.createSequentialGroup()
                                        .addComponent(lLayanan)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(cLayanan, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(18, 18, 18)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(cCari, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(bCari, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(BodyLayout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(24, 24, 24)
                                    .addComponent(tCariDetailTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(bCariDetailTransaksi, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)))
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addGap(177, 177, 177)
                                .addComponent(jLabel3))
                            .addComponent(bMuatTabelTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lCariBerdasarkan)
                            .addComponent(bMuatTabelDetailTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGap(320, 320, 320)
                        .addComponent(jLabel4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        BodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {lID, lJumlah, lLayanan, lNama, lStatus, lTotal});

        BodyLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cNama, cStatus, tIDTransaksi});

        BodyLayout.setVerticalGroup(
            BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BodyLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lLayanan)
                            .addComponent(cLayanan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lJumlah)
                            .addComponent(tJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lTotal)
                            .addComponent(tTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(bTambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(bKurang, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel2)
                                .addComponent(tCariDetailTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(bCariDetailTransaksi)))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lID)
                            .addComponent(tIDTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lNama)
                            .addComponent(cNama, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lStatus)
                            .addComponent(cStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(BodyLayout.createSequentialGroup()
                                .addComponent(bSimpan)
                                .addGap(15, 15, 15)
                                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(bHapus)
                                    .addComponent(bPrintKaryawan)))
                            .addComponent(bUbah))
                        .addGap(14, 14, 14)
                        .addComponent(bKembali)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addComponent(bMuatTabelDetailTransaksi)
                        .addGap(62, 62, 62)
                        .addComponent(lCariBerdasarkan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BodyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bCari)
                            .addComponent(cCari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bMuatTabelTransaksi)
                        .addGap(77, 77, 77))
                    .addGroup(BodyLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        BodyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cLayanan, cStatus, lID, lJumlah, lLayanan, lNama, lStatus, lTotal, tIDTransaksi, tJumlah, tTotal});

        BodyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bCari, bUbah, cCari, lCariBerdasarkan, tCari});

        BodyLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {bCariDetailTransaksi, jLabel2, tCariDetailTransaksi});

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

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/pay_cash_payment_money_dollar_bill_icon_143267.png"))); // NOI18N
        jLabel1.setText("MENU TRANSAKSI");

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
                .addGap(435, 435, 435)
                .addComponent(jLabel1)
                .addGap(342, 342, 342)
                .addComponent(lMinimize)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lExit)
                .addContainerGap())
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1))
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
            .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(Body, javax.swing.GroupLayout.DEFAULT_SIZE, 1260, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(Body, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void bMuatTabelTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMuatTabelTransaksiActionPerformed
        clear();
        muatTabelTransaksi();
    }//GEN-LAST:event_bMuatTabelTransaksiActionPerformed

    private void bSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bSimpanActionPerformed
        try {
            save();
        } catch (SQLException ex) {
            Logger.getLogger(MenuTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bSimpanActionPerformed

    private void bUbahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bUbahActionPerformed
        try {
            update();
        } catch (SQLException ex) {
            Logger.getLogger(MenuTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bUbahActionPerformed

    private void bHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHapusActionPerformed
        delete();
    }//GEN-LAST:event_bHapusActionPerformed

    private void bCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCariActionPerformed
        searchTransaksi();
    }//GEN-LAST:event_bCariActionPerformed

    private void tabelTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelTransaksiMouseClicked
        selectTableItemTransaksi();
    }//GEN-LAST:event_tabelTransaksiMouseClicked

    private void bKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKembaliActionPerformed
        kembali();
    }//GEN-LAST:event_bKembaliActionPerformed

    private void tCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tCariActionPerformed

    private void cCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cCariActionPerformed

    private void tJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tJumlahKeyReleased
        try {
            mengisiTotal();
        } catch (SQLException ex) {
            Logger.getLogger(MenuTransaksi.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_tJumlahKeyReleased

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

    private void bCariDetailTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCariDetailTransaksiActionPerformed
        searchDetailTransaksi();
    }//GEN-LAST:event_bCariDetailTransaksiActionPerformed

    private void bTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bTambahActionPerformed
        tambah();
    }//GEN-LAST:event_bTambahActionPerformed

    private void tabelDetailTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelDetailTransaksiMouseClicked
        selectTableDetailItemTransaksi();
    }//GEN-LAST:event_tabelDetailTransaksiMouseClicked

    private void bKurangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bKurangActionPerformed
        kurang();
    }//GEN-LAST:event_bKurangActionPerformed

    private void bMuatTabelDetailTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMuatTabelDetailTransaksiActionPerformed
        muatTabelDetailTransaksi();
        clear();
    }//GEN-LAST:event_bMuatTabelDetailTransaksiActionPerformed

    private void bPrintKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrintKaryawanActionPerformed
        print();

    }//GEN-LAST:event_bPrintKaryawanActionPerformed

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
            java.util.logging.Logger.getLogger(MenuTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuTransaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new MenuTransaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Body;
    private javax.swing.JPanel Header;
    private javax.swing.JButton bCari;
    private javax.swing.JButton bCariDetailTransaksi;
    private javax.swing.JButton bHapus;
    private javax.swing.JButton bKembali;
    private javax.swing.JButton bKurang;
    private javax.swing.JButton bMuatTabelDetailTransaksi;
    private javax.swing.JButton bMuatTabelTransaksi;
    private javax.swing.JButton bPrintKaryawan;
    private javax.swing.JButton bSimpan;
    private javax.swing.JButton bTambah;
    private javax.swing.JButton bUbah;
    private javax.swing.JComboBox<String> cCari;
    private javax.swing.JComboBox<String> cLayanan;
    private javax.swing.JComboBox<String> cNama;
    private javax.swing.JComboBox<String> cStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lCariBerdasarkan;
    private javax.swing.JLabel lExit;
    private javax.swing.JLabel lID;
    private javax.swing.JLabel lJumlah;
    private javax.swing.JLabel lLayanan;
    private javax.swing.JLabel lMinimize;
    private javax.swing.JLabel lNama;
    private javax.swing.JLabel lStatus;
    private javax.swing.JLabel lTotal;
    private javax.swing.JTextField tCari;
    private javax.swing.JTextField tCariDetailTransaksi;
    private javax.swing.JTextField tIDTransaksi;
    private javax.swing.JTextField tJumlah;
    private javax.swing.JTextField tTotal;
    private javax.swing.JTable tabelDetailTransaksi;
    private javax.swing.JTable tabelTransaksi;
    // End of variables declaration//GEN-END:variables
}
