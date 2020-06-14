//package org.jzl.lang.util;
//
//import java.util.Base64;
//
//public class Base64Helper {
//
//    private Base64.Decoder decoder;
//    private Base64.Encoder encoder;
//
//    private Base64Helper() {
//        decoder = Base64.getDecoder();
//        encoder = Base64.getEncoder();
//    }
//
//
//    public byte[] decode(byte[] bytes) {
//        return this.decoder.decode(bytes);
//    }
//
//    public String encode(byte[] bytes){
//        return this.encoder.encodeToString(bytes);
//    }
//
//    public static Base64Helper getInstance() {
//        return Holder.SIN;
//    }
//
//    static class Holder {
//        static final Base64Helper SIN = new Base64Helper();
//    }
//
//}
