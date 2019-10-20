package tools

import java.math.BigInteger
import java.security.MessageDigest

object Hash {


    def sha_one(text : String) : String ={


       String.format("%032x", new BigInteger(1, MessageDigest.getInstance("SHA-1").digest(text.getBytes("UTF-8"))))
    }
}
