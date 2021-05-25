//package com.wip.bool.cmmn.util;
//
//public class EncryptRSA {
//
//    public static String getHexString(byte[] b) throws Exception {
//            String result = "";
//            for (int i=0; i < b.length; i++) {
//                result +=
//                        Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
//            }
//            return result;
//        }
//
//        public static byte[] hexStringToByteArray(String s) {
//            int len = s.length();
//            byte[] data = new byte[len / 2];
//            for (int i = 0; i < len; i += 2) {
//                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
//            }
//            return data;
//        }
//
//
//        public static KeyPair genRSAKeyPair() {
//
//            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//
//            SecureRandom secureRandom = new SecureRandom();
//            KeyPairGenerator gen;
//            KeyPair keyPair = null;
//
//            try {
//                gen = KeyPairGenerator.getInstance("RSA");
//                gen.initialize(2048, secureRandom);
//
//                keyPair = gen.genKeyPair();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return keyPair;
//        }
//
//        public static String encryptStringRSA(String plainText, String publicKeyString) throws Exception{
////	Source: http://www.cs.berkeley.edu/~jonah/bc/org/bouncycastle/crypto/engines/RSAEngine.html
//            String encryptedData = null;
//            try {
//
//                Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
//
//                AsymmetricKeyParameter publicKey =
//                        (AsymmetricKeyParameter) PublicKeyFactory.createKey(Base64.getDecoder().decode((publicKeyString)));
//                AsymmetricBlockCipher e = new RSAEngine();
//                e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
//                e.init(true, publicKey);
//
//                byte[] messageBytes = plainText.getBytes();
//                byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length);
//
//                encryptedData = getHexString(hexEncodedCipher);
//
//            }
//            catch (Exception e) {
//                System.out.println(e);
//            }
//
//            return encryptedData;
//        }
//
//        public static String decryptStringRSA(String encrypted, String privateKeyString){
////	Source: http://www.mysamplecode.com/2011/08/java-rsa-decrypt-string-using-bouncy.html
//
//            String dncryptString = null;
//
//            try {
//                Security.addProvider(new BouncyCastleProvider());
//
//                AsymmetricKeyParameter privateKey;
//                privateKey = (AsymmetricKeyParameter) PrivateKeyFactory.createKey(Base64.getDecoder().decode(privateKeyString));
//                AsymmetricBlockCipher e = new RSAEngine();
//                e = new org.bouncycastle.crypto.encodings.PKCS1Encoding(e);
//                e.init(true, privateKey);
//
//                byte[] messageBytes = hexStringToByteArray(encrypted);
//                byte[] hexEncodedCipher = e.processBlock(messageBytes, 0, messageBytes.length);
//
//                System.out.println("hexEncodedCipher : " + hexEncodedCipher);
//                dncryptString = new String(hexEncodedCipher, "UTF-8");
//
//            }
//            catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return dncryptString;
//        }
//
//    }
