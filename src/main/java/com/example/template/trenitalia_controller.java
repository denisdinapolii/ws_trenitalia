package com.example.template;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin(origins = "*")
@RequestMapping("/trenitalia")
@RestController()
public class trenitalia_controller {
    sqlQuery query;

    public trenitalia_controller() {
        query= new sqlQuery();
    }

    //http://localhost:8080/trenitalia/registrati?mail=monicapanucci@libero.it&pass=monica
    @RequestMapping(value = "/registrati", method = RequestMethod.GET)
    public Result register(@RequestParam(name = "mail", required = true)String mail, @RequestParam(name = "pass", required = true)String pass) throws SQLException, NoSuchAlgorithmException{
        return query.insertUtente(mail, pass);
    }

    //http://localhost:8080/trenitalia/getToken?mail=monicapanucci@libero.it&pass=monica
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public Result getToken(@RequestParam(name = "mail", required = true)String mail, @RequestParam(name = "pass", required = true)String pass) throws SQLException, NoSuchAlgorithmException{
        return query.getToken(mail, pass);
    }

    //http://localhost:8080/trenitalia/getViaggi/9af5b1f88642ea112e6b8a1ca92ae30f
    @RequestMapping(value = {"/getViaggi/{token}", "/getViaggi"}, method = RequestMethod.GET)
    public Result getViaggi(@PathVariable(required = false) String token) throws NoSuchAlgorithmException, SQLException{
        if(token!=null)
            return query.viaggiToken(token);
        else
            return query.viaggiNoToken();
    }

    //http://localhost:8080/trenitalia/prenota/9af5b1f88642ea112e6b8a1ca92ae30f?passeggeri=2&id=3
    @RequestMapping(value = {"/prenota/{token}"},method = RequestMethod.GET)
    public Result prenotaViaggio(@PathVariable(name = "token", required = true)String token,@RequestParam(name = "passeggeri", required = true) int passeggeri,@RequestParam(name = "id",required = true ) int id) throws SQLException{
        return query.prenota(token, passeggeri, id );
    }
    //http://localhost:8080/trenitalia/cambiaPasseggeri/9af5b1f88642ea112e6b8a1ca92ae30f?passeggeri=5&id=3
    @RequestMapping(value = {"/cambiaPasseggeri/{token}"},method = RequestMethod.GET)
    public Result cambiaNPasseggeri(@PathVariable(name = "token", required = true)String token,@RequestParam(name = "passeggeri", required = true) int passeggeri,@RequestParam(name = "id",required = true ) int id) throws SQLException{
        return query.updatePass(token, passeggeri,id);
    }

    //http://localhost:8080/trenitalia/eliminaPrenotazione/9af5b1f88642ea112e6b8a1ca92ae30f/3
    @RequestMapping(value = {"/eliminaPrenotazione/{token}/{id}", "/eliminaPrenotazione", "/eliminaPrenotazione/{token}"},method = RequestMethod.GET)
    public Result cambiaNPasseggeri(@PathVariable(name = "token", required = false)String token,@PathVariable(name = "id", required = false) int id) throws SQLException{
        return query.deleteViaggio(token, id);
    }



}
