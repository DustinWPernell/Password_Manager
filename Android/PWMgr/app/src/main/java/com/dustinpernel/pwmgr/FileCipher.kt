package com.dustinpernel.pwmgr

import android.util.Log

import java.nio.charset.Charset
import java.security.NoSuchAlgorithmException
import java.util.Arrays

import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by dustinpernell on 4/20/17.
 */

class FileCipher(password: ByteArray?) {
    private var cipher: Cipher? = null
    private val iv = byteArrayOf(2, 6, 95, 4, 35, 7, 56, 5, 847.toByte(), 1, 86, 3, 465.toByte(), 5, 7, 41)
    private val ivspec: IvParameterSpec
    private val keyspec: SecretKeySpec

    init {
        var password = password
        Log.d("LOAD", "FileCipher")

        ivspec = IvParameterSpec(Arrays.copyOf(iv, iv.size))
        password = padData(password!!)
        keyspec = SecretKeySpec(password, "AES")

        try {
            cipher = Cipher.getInstance("AES/CFB/PKCS5Padding")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchPaddingException) {
            e.printStackTrace()
        }

    }

    @Throws(Exception::class)
    fun cryptography(data: ByteArray, mode: String): ByteArray? {
        Log.d("METHOD", "cryptography")

        val newData: ByteArray?

        if (mode.equals("Decrypt")) {
            newData = decryptCipher(data)
        } else {
            newData = encryptCipher(data)
            //            newData = byteArrayToHexString(newData).getBytes(Charset.forName("UTF-8"));
        }

        return newData
    }


    @Throws(Exception::class)
    fun encryptCipher(clearText: ByteArray?): ByteArray? {
        Log.d("METHOD", "encryptCipher Loaded")
        if (clearText == null || clearText.size == 0)
            throw Exception("Empty string")

        var encrypted: ByteArray? = null
        try {
            cipher!!.init(Cipher.ENCRYPT_MODE, keyspec, ivspec)

            encrypted = cipher!!.doFinal(padData(clearText))
        } catch (e: Exception) {
            throw Exception("[encrypt] " + e.getMessage())
        }

        return encrypted
    }

    @Throws(Exception::class)
    fun decryptCipher(cipherText: ByteArray?): ByteArray? {
        Log.d("METHOD", "decryptCipher Loaded")
        if (cipherText == null || cipherText.size == 0)
            throw Exception("Empty string")

        var decrypted: ByteArray? = null
        try {
            cipher!!.init(Cipher.DECRYPT_MODE, keyspec, ivspec)

            //            decrypted = cipher.doFinal(hexToBytes(cipherText.toString()));

            decrypted = cipher!!.doFinal(padData(cipherText))

        } catch (e: Exception) {
            throw Exception("[decrypt] " + e.getMessage())
        }

        return decrypted
    }

    companion object {

        fun hexToBytes(str: String?): ByteArray? {
            Log.d("METHOD", "hexToByte")

            if (str == null) {
                return null
            } else if (str.length() < 2) {
                return null
            } else {

                val len = str.length() / 2
                val buffer = ByteArray(len)
                for (i in 0 until len) {
                    buffer[i] = Integer.parseInt(
                            str.substring(i * 2, i * 2 + 2), 16) as Byte
                }
                return buffer
            }
        }

        fun byteArrayToHexString(array: ByteArray): String {
            Log.d("METHOD", "byteArraToHexString")

            val hexString = StringBuffer()
            for (b in array) {
                val intVal = b and 0xff
                if (intVal < 0x10)
                    hexString.append("0")
                hexString.append(Integer.toHexString(intVal))
            }
            return hexString.toString()
        }

        private fun padData(source: ByteArray): ByteArray? {
            Log.d("METHOD", "padData")

            val paddingByte: Byte = '%'.toByte()
            val size = 16
            val x = source.size % size
            var newSource: ByteArray? = null
            if (x != 0) {
                val padLength = size - x
                newSource = ByteArray(source.size + padLength)
                var count = 0
                for (b in source) {
                    newSource[count] = b
                    count++
                }
                for (i in 0 until padLength) {
                    newSource[count] = paddingByte
                    count++
                }
            }
            return newSource
        }
    }
}
