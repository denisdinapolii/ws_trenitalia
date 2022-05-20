package com.example.template;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.LinkedHashMap;


public class sqlQuery {
    composeMex mex;
    sqlQuery(){
        mex=new composeMex();
        composeMex.creaConnessione();
    }
    

    public Result insertUtente(String mail, String pass) throws SQLException, NoSuchAlgorithmException{
        if(!utilities.checkMail(mail))
            return mex.error("E-Mail non valida");

        String sql="SELECT * FROM utenti where mail='"+mail+"'";    
        if(mex.query(sql,"id", "").isResult())  
            return mex.error("Questa e-mail è già stata utilizzata");

        sql="INSERT INTO utenti(mail, password,token)VALUES('"+mail+"', '"+utilities.MD5(pass)+"', '"+utilities.generaToken(mail)+"')";
        return mex.update(sql, "Utente registrato", "Utente non registrato");
    }


    public Result getToken(String mail, String pass) throws SQLException, NoSuchAlgorithmException{
        if(!utilities.checkMail(mail))
            return mex.error("E-Mail non valida");

        String sql="SELECT * FROM utenti where mail='"+mail+"'and password='"+utilities.MD5(pass)+"'";
        return mex.query(sql,"token", "Attenzione, le credenziali sono errate");
    }


    public Result viaggiNoToken()throws SQLException, NoSuchAlgorithmException{
        String sql="SELECT * FROM viaggio";
        return mex.getViaggi(sql,"Attenzione non ci sono viaggi!", 0);
    }

    public Result viaggiToken(String token)throws SQLException, NoSuchAlgorithmException{
        String sql="SELECT * FROM prenota join utenti as u on u.id = prenota.idUtente join viaggio as v on v.id =prenota.idViaggio where u.token='"+token+"'";
        return mex.getViaggi(sql,"Attenzione non ci sono viaggi!", 1);
    }


    public Result prenota(String token, int passeggeri, int id) throws SQLException {
        int idUtente=0;
        try {
            idUtente=checkUserFromToken(token);
        } catch (Exception e) {
            return mex.error("token errato");
        }
        String sql="INSERT INTO prenota(idUtente, idViaggio,nPasseggeri)VALUES('"+idUtente+"', '"+id+"', '"+passeggeri+"')";
        return mex.update(sql, "Prenotazione effettuata per: "+passeggeri+" passeggeri", "Prenotazione non effettuata");
                
    }


    
    public Result updatePass(String token, int passeggeri, int id) throws SQLException {
        int idUtente=0;
        try {
            idUtente=checkUserFromToken(token);
        } catch (Exception e) {
            return mex.error("token errato");
        }
        String sql="UPDATE prenota set nPasseggeri = '"+passeggeri+"' where idUtente= '"+idUtente+"' and idViaggio = '"+id+"'";
        return mex.update(sql, "Prenotazione modificata per: "+passeggeri+" passeggeri", "Attenzione, Modifica non effettuata!");
    }


    public Result deleteViaggio(String token, int id) throws SQLException {
        int idUtente=0;
        try {
            idUtente=checkUserFromToken(token);
        } catch (Exception e) {
            return mex.error("token errato");
        } 
        String sql="DELETE from prenota where idUtente='"+idUtente+"' and idViaggio='"+id+"'";
        return mex.update(sql, "Prenotazio eliminata", "Eliminazione non effettuata");
    }

    
    

    

    private int checkUserFromToken(String token) throws SQLException{
        String sql="SELECT id from utenti where token='"+token+"'";
        return (int)((LinkedHashMap<String, Object>) mex.query(sql, "id", "").getMessage()).get("message");
    }



    

    
}
