package com.example.rebu

import android.nfc.cardemulation.HostApduService
import android.os.Bundle

class ClienteHceService : HostApduService() {

    companion object {
        private val SELECT_HEADER = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00)
        private val SUCCESS_SW = byteArrayOf(0x90.toByte(), 0x00)
        private const val ID_CLIENTE = "CLIENTE-00456"
    }

    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        if (commandApdu != null && esComandoSelect(commandApdu)) {
            return ID_CLIENTE.toByteArray(Charsets.UTF_8) + SUCCESS_SW
        }
        return SUCCESS_SW
    }

    override fun onDeactivated(reason: Int) {}

    private fun esComandoSelect(apdu: ByteArray): Boolean =
        apdu.size >= 4 && apdu.copyOfRange(0, 4).contentEquals(SELECT_HEADER)
}