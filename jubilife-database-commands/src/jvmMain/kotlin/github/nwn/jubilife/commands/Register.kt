package github.nwn.jubilife.commands

import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.KeyFactory
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.SecretKeySpec


fun Transaction.registerUser(email: String, password: String, salt: String, pepper: String, key: ByteArray) {

    val encrypted = Cipher.getInstance("AES").run {
        this.init(Cipher.ENCRYPT_MODE,SecretKeySpec(key,"AES"))
        doFinal("$password$salt$pepper".encodeToByteArray())
    }

}