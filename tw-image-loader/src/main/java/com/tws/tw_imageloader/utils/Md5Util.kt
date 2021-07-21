package com.tws.tw_imageloader.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

fun String.hashKeyForDisk(): String {
    return try {
        val digest: MessageDigest = MessageDigest.getInstance("MD5")
        digest.update(toByteArray())
        digest.digest().bytesToHexString()
    } catch (e: NoSuchAlgorithmException) {
        hashCode().toString()
    }
}

private fun ByteArray.bytesToHexString(): String {
    val sb = StringBuilder()
    for (i in indices) {
        val hex = Integer.toHexString(0xFF and this[i].toInt())
        if (hex.length == 1) {
            sb.append('0')
        }
        sb.append(hex)
    }
    return sb.toString()
}
