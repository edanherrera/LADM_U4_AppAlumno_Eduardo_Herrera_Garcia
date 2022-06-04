package mx.tecnm.tepic.ladm_u4_appalumno

import android.R
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import mx.tecnm.tepic.ladm_u4_appalumno.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    var list = ArrayList<String>()
    val identifi: UUID = UUID.fromString("8e3508b8-e39f-11ec-8fea-0242ac120002")
    var listaID = ArrayList<BluetoothDevice>()
    lateinit var telefono : BluetoothDevice
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);tengo();


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.asistencia.setOnClickListener {
            Toast.makeText(this,"Estás conectado a:${binding.noControl.text.toString()}", Toast.LENGTH_LONG).show()
            Cliente(telefono,this,identifi,binding.noControl.text.toString()).start()
        }

        for (device in BluetoothAdapter.getDefaultAdapter().bondedDevices) {
            listaID.add(device)
            list.add((if (device.name != null) device.name else "Unknown") + "\n" + device.address + "\nPared")
        }
        binding.lista.adapter = ArrayAdapter<String>(this,
            R.layout.simple_list_item_1, list)

        binding.lista.setOnItemClickListener { adapterView, view, posicion, l ->
            AlertDialog.Builder(this)
                .setTitle("ATENCIÓN")
                .setMessage("Deseas conectarte a este telefono${listaID[posicion].name}?")
                .setPositiveButton("Conectar") { d, i ->
                    telefono = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(listaID[posicion].address)
                }
                .setNegativeButton("CANCELAR") { d, i -> }
                .show()
        }

    }
    fun tengo(){

        val permiso = arrayOf(
            android.Manifest.permission.BLUETOOTH_CONNECT,
            android.Manifest.permission.BLUETOOTH_SCAN,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if (!permisos(this, permiso)) {
            ActivityCompat.requestPermissions(this, permiso, 1)
        }
    }
    fun permisos(context: Context, permiso: Array<String>): Boolean = permiso.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }


}