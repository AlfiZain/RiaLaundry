package koneksi;
import java.sql.*;

/**
 *
 * @author alfi_zain
 */
public class Koneksi {
    private Connection Koneksi;
    public Connection connect(){
    try{
        Class.forName("com.mysql.cj.jdbc.Driver");
    } catch(ClassNotFoundException ex){
        System.out.println("Gagal terkoneksi dengan driver " + ex);
    }
    //lokasi database yang akan dikoneksi/nama database
    String url = "jdbc:mysql://localhost/ria_laundry";
    try{
        //lokasi,username,password dari database
        Koneksi = DriverManager.getConnection(url,"root","");
        System.out.println("Berhasil terkoneksi dengan database");
    } catch(SQLException ex){
        System.out.println("Gagal terkoneksi dengan database " + ex);
    }
    return Koneksi;
    }
}