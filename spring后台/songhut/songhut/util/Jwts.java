package com.songhut.songhut.util;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT模型
 * @author Kun
 */
public class Jwts {
    private String Head;
    private String Claim;
    private String Sign;
    private Base64.Encoder encoder = Base64.getEncoder();
    private Base64.Decoder decoder = Base64.getDecoder();

    private Jwts(String Head,String Claim,String Sign){
        this.Head=Head;
        this.Claim=Claim;
        this.Sign=Sign;
    }

    public static Jwts build(){
        return new Jwts("Head","Claim","Sign");
    }

    public Jwts setHead(String secretKey){
        StringBuffer head = new StringBuffer(secretKey);
        this.Head=encoder.encodeToString(head.toString().getBytes());
        return this;
    }

    public Jwts setClaim(Map<String,Long> map){
        StringBuffer strbuf = new StringBuffer();
        for(Map.Entry<String,Long> entry:map.entrySet()){
            strbuf.append(entry.getKey()+":"+entry.getValue());
            strbuf.append(";");
        }
        String claim = encoder.encodeToString(strbuf.toString().getBytes());
        this.Claim=claim;
        return this;
    }

    public Jwts setSign(Integer sign,String secret){
        try {
            String md5 = Md5.md5(sign.toString(),secret);
            String b64 = encoder.encodeToString(md5.getBytes());
            this.Sign=b64;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println(e.getStackTrace());
            this.Sign="";
        }finally {
            return this;
        }
    }

    public String contact(){
        return Head+"."+Claim+"."+Sign;
    }

    public Jwts unpack(String token){
        try{
            String[] strings = token.split("\\.");
            this.Head= new String(decoder.decode(strings[0]));
            this.Claim=new String(decoder.decode(strings[1]));
            this.Sign=new String(decoder.decode(strings[2]));
            System.out.println(Claim);
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }finally {
            return this;
        }
    }

    public String getHead(){
        return this.Head;
    }
    public Map<String,Long> getClaim(){
        Map<String,Long> map = new HashMap<>();
        String[] strings = Claim.split(";");
        try {
            for (String s : strings) {
                if ("".equals(s)) continue;
                String[] kv = s.split(":");
                map.put(kv[0],Long.valueOf(kv[1]));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return map;
        }
    }
    public String getSign(){
        return Sign;
    }

    @Override
    public String toString() {
        return "Jwts{" +
                "Head='" + Head + '\'' +
                ", Claim='" + Claim + '\'' +
                ", Sign='" + Sign + '\'' +
                ", encoder=" + encoder +
                ", decoder=" + decoder +
                '}';
    }
}
