package koneksi;

/**
 *
 * @author alfi_zain
 */
public class Sesi {
    private static int id_loggin;
    private static String role;
    
    public static int getIDLogin(){
        return id_loggin;
    }
    
    public static String getRole(){
        return role;
    }
    
    public static void setIDLogin(int id_loggin){
        Sesi.id_loggin = id_loggin;
    }
    
    public static void setRole(String role){
        Sesi.role = role;
    }
}
