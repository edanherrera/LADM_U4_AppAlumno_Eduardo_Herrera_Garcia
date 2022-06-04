package mx.tecnm.tepic.ladm_u4_appalumno

import android.bluetooth.BluetoothDevice
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.io.IOException
import java.util.*

class Cliente (device: BluetoothDevice, context: MainActivity, uuid: UUID, mensaje:String): Thread() {
    private val mensaje=mensaje
    private val uuid = uuid
    private val socket = device.createInsecureRfcommSocketToServiceRecord(uuid)
    val context = context
    override fun run() {

        try { this.socket.connect()
        }catch(e: IOException){

            context.runOnUiThread {
                AlertDialog.Builder(context)
                    .setTitle("Error en conexión")
                    .setMessage("Tal vez el servidor este apagado\nO no se han emparejado")
                    .show()
            }
        }

        val outputStream = this.socket.outputStream
        val inputStream = this.socket.inputStream
        try {
            outputStream.write(mensaje.toByteArray())
        } catch(e: Exception) {
            AlertDialog.Builder(context)
                .setTitle(e.toString())
                .setMessage(e.toString())
                .show()
        } finally {
            outputStream.close()
            inputStream.close()
            this.socket.close()
        }
    }
}