/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package con.aplic.datos;

/**
 *
 * @author jose
 */
import java.sql.*;
import java.util.Vector;

public class AccesoDatos {
    
    private static String sUrl = null;  // sUrl de conexión
    private static String sUsr = null; // Usuario
    private static String sPwd = null;  // Contraseña
    private java.sql.Connection oConexion;  // La conexión
    
   public AccesoDatos() throws Exception {
        sUrl = "jdbc:postgresql://localhost/conafordb";
        sUsr = "userconafor";
        sPwd = "userconafor1";
    }

    /**
     * Realiza la conexión a la base de datos.
     */
    public void conectar() throws Exception {
		
        try 
		{
            Class.forName ("org.postgresql.Driver").newInstance();
            oConexion = DriverManager.getConnection(sUrl, sUsr, sPwd);
        } catch(SQLException e) {
            throw e;
        }
    }

    /**
     * Realiza la desconexión a la base de datos.
     */
    public void desconectar() throws Exception {
		
        oConexion.close();
    }

    /**
     * Código que se ejecuta cuando este objeto es colectado.
     */
    @Override
    public void finalize() throws Exception{
        oConexion.close();
        oConexion = null;
    }
        
    /**
     * Realiza una consulta a la base de datos y retorna un vector de resultados.
     */
    public synchronized Vector ejecutarConsulta(String psQuery) throws Exception {
        
        Statement stmt = null;
        ResultSet rset = null;
        Vector vrset = null;
        ResultSetMetaData rsmd = null;
        int nNumCols = 0;
        try {
            stmt = oConexion.createStatement();
            rset = stmt.executeQuery (psQuery);
            rsmd = rset.getMetaData();
            nNumCols = rsmd.getColumnCount();
            vrset = convierteAVector(rset, rsmd, nNumCols);
        } finally {
        	if(rset != null){
	            rset.close();
	            stmt.close(); 
	         }
            rset = null;
            stmt = null;
        }
        return vrset;
    }
         
    /**
     * Realiza una petición de modificación de datos, retornando
     * un int con el número de registros afectados.
     */
    public synchronized int ejecutarComando(String psStatement) 
            throws Exception {
 		
        int ret = 0;
        Vector vTransaction = new Vector();
        
        vTransaction.addElement(psStatement);
        ret = ejecutarComando(vTransaction);
 
        return ret;     
    }
        
    /**
     * Realiza una serie de peticiones de modificación de datos, retornando
     * un int con el número de registros afectados.
     * Estas peticiones son ejecutadas todas en una transacción.
     */
    public synchronized int ejecutarComando(Vector pvStatement) 
            throws Exception {

        int ret = 0, i=0;
        Statement stmt = null;
        String temp = "";
        
        try {
            oConexion.setAutoCommit(false); 
            stmt = oConexion.createStatement();
            for (i=0; i < pvStatement.size(); i++) {
                temp = (String)pvStatement.elementAt(i);
                ret += stmt.executeUpdate(temp);
            }
            oConexion.commit();
        } catch(SQLException e) {
            oConexion.rollback();
            throw e;
        } finally {
            stmt.close();
            stmt = null;
        }
 
        return ret;
    }
        
    /**
     * Recorre un result set y entrega el vector resultante.
     */
    private synchronized Vector convierteAVector( ResultSet rset, 
                                              ResultSetMetaData rsmd,
                                              int nNumCols ) 
            throws Exception {
        Vector vrset = new Vector();
        Vector vrsettmp = null;
        int i=0;
        
        while (rset.next()) {
            vrsettmp = new Vector();
            for (i = 1; i <= nNumCols; i++) {   

                switch (rsmd.getColumnType(i)) {
                case Types.CHAR:
                case Types.VARCHAR:
                    String varchar = "" + doubleQuote(rset.getString(i));
                    vrsettmp.addElement(varchar);
                    break;
                case Types.INTEGER:
                    vrsettmp.addElement(new Double(rset.getLong(i)));
                    break;
                case Types.SMALLINT:
                    vrsettmp.addElement(new Double(rset.getInt(i)));             
                    break;
                case Types.BIGINT:
                case Types.NUMERIC:
                case Types.DECIMAL:
                case Types.DOUBLE:
                    vrsettmp.addElement(new Double(rset.getDouble(i)));
                    break;
                case Types.DATE:
                case Types.TIME:
                case Types.TIMESTAMP:
                    vrsettmp.addElement((rset.getTimestamp(i)==null?null:new Date(rset.getTimestamp(i).getTime())));
                    break;
               default:
                    String str = "" + rset.getString(i);
                    vrsettmp.addElement(str);
                } //switch  
            }  //for
            vrset.addElement(vrsettmp);
        } //while
        return vrset;
    }
        
    /**
     * Imprime en forma adecuada este objeto.
     * @return String los datos del objeto.
     */
    @Override
    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("Class = DataAccess \n");
        s.append("    static sUrl  = " + sUrl + "\n");
        s.append("    static sUsr = " + sUsr + "\n");
        s.append("    static sPwd  = " + sPwd + "\n");
        s.append("    oConexion = " + oConexion + "\n");
        return s.toString();
    }
    
    /**
     * Si la cadena contiene comillas en la base de datos, convierte a código.
     * @return String cadena sin las comillas internas.
     */
	private String doubleQuote(String psCadena){
		if(psCadena == null){
			psCadena = "";
		}
		String CadenaEntrada="";
		if(psCadena.equals("")){
			return psCadena;
		} else if(psCadena.equals("\"")){
			return "&quot;";
		} else {
			int indice = -2;
			CadenaEntrada=psCadena;
			while((indice = CadenaEntrada.indexOf("\"",indice+2))!=-1)
				CadenaEntrada=CadenaEntrada.substring(0,CadenaEntrada.indexOf("\"",indice))+"&quot;"+CadenaEntrada.substring(CadenaEntrada.indexOf("\"",indice)+1);
		}
		return CadenaEntrada;
	}
    
}
