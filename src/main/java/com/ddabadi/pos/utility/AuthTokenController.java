package com.ddabadi.pos.utility;


import io.jsonwebtoken.*;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class AuthTokenController {

    private class TokenResultDto{
        protected String key;
        protected String token;
    }
    private Logger logger = Logger.getLogger(AuthTokenController.class);

    private TokenResultDto generateToken(String nama){
        logger.info("generate Token");
        //Key key = MacProvider.generateKey();

        SignatureAlgorithm signatureAlgorithm=SignatureAlgorithm.HS256;

        // 2 jam
        Long dateL = new Date().getTime()+(1000 * 60*60*2);

        String compactJws = Jwts.builder()
                .setSubject(nama)
                .setIssuer("posSystem")
                .setIssuedAt(new Date())
                .setExpiration(new Date(dateL))
                .signWith(signatureAlgorithm, "saltUntukKeyRahasia")
                .compact();
        logger.info("success generate token");
        TokenResultDto token = new TokenResultDto();
        //token.key = key;
        token.token = compactJws;

        return  token;
    }

    @RequestMapping(value ="token",
                    method = RequestMethod.GET,
                    produces = "application/json")
    public TokenResultDto generate(){
        return generateToken("deddy");
    }

    @RequestMapping(value ="cekToken",
            method = RequestMethod.GET,
            produces = "application/json")
    public String cekToken() {
        TokenResultDto token = generateToken("deddy");

        try {
            //Jwts.parser().setSigningKey(token.key).parseClaimsJws(token.token);
            Jws<Claims> claims = Jwts.parser()
                    .requireSubject("deddy")
                    .requireIssuer("posSystem")
                    .setSigningKey("saltUntukKeyRahasia")
                    .parseClaimsJws(token.token);

            Date tgl = claims.getBody().getExpiration();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:MM:ss");
            sdf.format(tgl);
            System.out.println("tanggal expired " + sdf.getCalendar().getTime());
            System.out.println("tanggal sekarang" + new Date());

            //Waktu token lebih kecil dari sekarang
            if(tgl.before(new Date()) ){
                return "expired token";
            }else{
                //return claims.getBody().getIssuer();
                return sdf.getCalendar().getTime().toString();
            }


        } catch (SignatureException e) {
            return  "Failed token";
        } catch (MissingClaimException e) {
            logger.error("MissingClaimException ="+e.getMessage());
            // we get here if the required claim is not present
            return  "Claim ada yg hilang";
        } catch (IncorrectClaimException e) {
            logger.info("IncorrectClaimException ="+e.getMessage());
            // we get here if the required claim has the wrong value
            return  "Claim salam tipe";
        }

    }


}
