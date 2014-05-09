/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package con.aplic.modelo;

/**
 *
 * @author jose
 */

import con.aplic.datos.AccesoDatos;
import java.util.Vector;


public class Asesor {

    private String sNomAsesor="";
    private AccesoDatos oAD=null;


public String getNomAsesor(){
    return sNomAsesor;
}

public void setNomAsesor(String sNomAsesor){
    this.sNomAsesor = sNomAsesor;
}

   public AccesoDatos getAD() {
        return oAD;
    }

    /**
     * @param oAD the oAD to set
     */
    public void setAD(AccesoDatos oAD) {
        this.oAD = oAD;
    }

 public Asesor[] buscarTodos()throws Exception{
    Asesor arrRet[] = null, oAsesor=null;
    Vector rst = null;
    Vector<Asesor> vObj = null;
    String sQuery = "";
    int i=0, nTam = 0;

       sQuery = "SELECT nombre " 
               +"FROM  Asesor";

       System.out.println(sQuery);
        /*Si oAD es nulo, tiene que crearlo y conectarlo, de otro modo
          supone que ya viene conectado*/
        if (getAD() == null){
            setAD(new AccesoDatos());
            getAD().conectar();
            rst = getAD().ejecutarConsulta(sQuery);
            getAD().desconectar();
            setAD(null);
        }
        else{
            rst = getAD().ejecutarConsulta(sQuery);
        }
        if (rst != null) {
            vObj = new Vector<Asesor>();
            for (i = 0; i < rst.size(); i++) {
                oAsesor = new Asesor();
                Vector vTemp = (Vector)rst.elementAt(i);
             //vas a ordenarlos de acuerdo a la base
                oAsesor.setNomAsesor(((String) vTemp.elementAt(0)));

                vObj.add(oAsesor);
            }
            nTam = vObj.size();
            arrRet = new Asesor[nTam];

            for (i=0; i<nTam; i++){
                arrRet[i] = vObj.elementAt(i);
            }
        }
        return arrRet;
    }
}