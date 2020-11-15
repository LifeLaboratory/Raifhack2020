package ru.lifelaboratory.raifhack2020

import android.Manifest
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.CreateNdefMessageCallback
import android.nfc.cardemulation.HostApduService
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.lifelaboratory.raifhack2020.utils.ServerAPI
import java.io.UnsupportedEncodingException
import java.math.BigInteger
import java.util.*

class MainActivity : AppCompatActivity(), LocationListener {

    private var mNfcAdapter: NfcAdapter? = null
    private lateinit var mTurnNfcDialog: AlertDialog
    private var locationManager : LocationManager? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_dashboard))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.NFC) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            requestPermissions(arrayOf(Manifest.permission.NFC, Manifest.permission.BIND_NFC_SERVICE), 1);
        }

        val nfc = NfcAdapter.getDefaultAdapter(this)
        nfc.setNdefPushMessageCallback(CreateNdefMessageCallback
        {
            val uriRecord: NdefRecord =
                NdefRecord.createUri(Uri.parse("https://qr.nspk.ru/AD75B0C97610446B8B9F2C6ED5717058?type=02&bank=10000001&sum=8290&cur=RUB&crc=C08B"))
            NdefMessage(arrayOf(uriRecord))
        }, this, this)

    }

    override fun onLocationChanged(location: Location) {
        Log.e("TEST", "Latitude: " + location.latitude + " , Longitude: " + location.longitude)
        run {
            ServerAPI.create().sendLocation(1,
                ru.lifelaboratory.raifhack2020.utils.Location(
                    lat = location.latitude,
                    lon = location.longitude
                )
            ).enqueue(object: Callback<Boolean>{
                override fun onFailure(call: Call<Boolean>?, t: Throwable?) {}
                override fun onResponse(call: Call<Boolean>?, response: Response<Boolean>?) {}
            })
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 2) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initNFCFunction() {

        if (checkNFCEnable() && packageManager.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION)) {
            initService()
        } else {
            showTurnOnNfcDialog()
        }
    }

    private fun initService() {
        if (TextUtils.isEmpty("http://yandex.ru")) {
            Toast.makeText(
                this,
                "URA",
                Toast.LENGTH_LONG
            ).show()
        } else {
            val intent = Intent(this, KHostApduService::class.java)
            intent.putExtra("ndefMessage", "https://qr.nspk.ru/AD64BD885C8D4E9E8A1865794D1F5203?type=02&bank=10000001&sum=8290&cur=RUB&crc=C08B")
            startService(intent)
        }
    }

    private fun checkNFCEnable(): Boolean {
        return if (mNfcAdapter == null) {
            false
        } else {
            mNfcAdapter!!.isEnabled
        }
    }

    private fun showTurnOnNfcDialog() {
        mTurnNfcDialog = AlertDialog.Builder(this)
            .setTitle("Test")
            .setMessage("Message")
            .setPositiveButton(
                "OK"
            ) { _, _ ->
                startActivity(Intent(android.provider.Settings.ACTION_NFC_SETTINGS))
            }.setNegativeButton("NO") { _, _ ->
                onBackPressed()
            }
            .create()
        mTurnNfcDialog.show()
    }

    override fun onResume() {
        super.onResume()
//        if (mNfcAdapter!!.isEnabled) {
//            initService()
//        }
    }

}
class KHostApduService : HostApduService() {

    private val TAG = "HostApduService"

    private val APDU_SELECT = byteArrayOf(
        0x00.toByte(), // CLA	- Class - Class of instruction
        0xA4.toByte(), // INS	- Instruction - Instruction code
        0x04.toByte(), // P1	- Parameter 1 - Instruction parameter 1
        0x00.toByte(), // P2	- Parameter 2 - Instruction parameter 2
        0x07.toByte(), // Lc field	- Number of bytes present in the data field of the command
        0xD2.toByte(),
        0x76.toByte(),
        0x00.toByte(),
        0x00.toByte(),
        0x85.toByte(),
        0x01.toByte(),
        0x01.toByte(), // NDEF Tag Application name
        0x00.toByte()  // Le field	- Maximum number of bytes expected in the data field of the response to the command
    )

    private val CAPABILITY_CONTAINER_OK = byteArrayOf(
        0x00.toByte(), // CLA	- Class - Class of instruction
        0xa4.toByte(), // INS	- Instruction - Instruction code
        0x00.toByte(), // P1	- Parameter 1 - Instruction parameter 1
        0x0c.toByte(), // P2	- Parameter 2 - Instruction parameter 2
        0x02.toByte(), // Lc field	- Number of bytes present in the data field of the command
        0xe1.toByte(), 0x03.toByte() // file identifier of the CC file
    )

    private val READ_CAPABILITY_CONTAINER = byteArrayOf(
        0x00.toByte(), // CLA	- Class - Class of instruction
        0xb0.toByte(), // INS	- Instruction - Instruction code
        0x00.toByte(), // P1	- Parameter 1 - Instruction parameter 1
        0x00.toByte(), // P2	- Parameter 2 - Instruction parameter 2
        0x0f.toByte()  // Lc field	- Number of bytes present in the data field of the command
    )

    // In the scenario that we have done a CC read, the same byte[] match
    // for ReadBinary would trigger and we don't want that in succession
    private var READ_CAPABILITY_CONTAINER_CHECK = false

    private val READ_CAPABILITY_CONTAINER_RESPONSE = byteArrayOf(
        0x00.toByte(), 0x11.toByte(), // CCLEN length of the CC file
        0x20.toByte(), // Mapping Version 2.0
        0xFF.toByte(), 0xFF.toByte(), // MLe maximum
        0xFF.toByte(), 0xFF.toByte(), // MLc maximum
        0x04.toByte(), // T field of the NDEF File Control TLV
        0x06.toByte(), // L field of the NDEF File Control TLV
        0xE1.toByte(), 0x04.toByte(), // File Identifier of NDEF file
        0xFF.toByte(), 0xFE.toByte(), // Maximum NDEF file size of 65534 bytes
        0x00.toByte(), // Read access without any security
        0xFF.toByte(), // Write access without any security
        0x90.toByte(), 0x00.toByte() // A_OKAY
    )

    private val NDEF_SELECT_OK = byteArrayOf(
        0x00.toByte(), // CLA	- Class - Class of instruction
        0xa4.toByte(), // Instruction byte (INS) for Select command
        0x00.toByte(), // Parameter byte (P1), select by identifier
        0x0c.toByte(), // Parameter byte (P1), select by identifier
        0x02.toByte(), // Lc field	- Number of bytes present in the data field of the command
        0xE1.toByte(), 0x04.toByte() // file identifier of the NDEF file retrieved from the CC file
    )

    private val NDEF_READ_BINARY = byteArrayOf(
        0x00.toByte(), // Class byte (CLA)
        0xb0.toByte() // Instruction byte (INS) for ReadBinary command
    )

    private val NDEF_READ_BINARY_NLEN = byteArrayOf(
        0x00.toByte(), // Class byte (CLA)
        0xb0.toByte(), // Instruction byte (INS) for ReadBinary command
        0x00.toByte(), 0x00.toByte(), // Parameter byte (P1, P2), offset inside the CC file
        0x02.toByte()  // Le field
    )

    private val A_OKAY = byteArrayOf(
        0x90.toByte(), // SW1	Status byte 1 - Command processing status
        0x00.toByte()   // SW2	Status byte 2 - Command processing qualifier
    )

    private val A_ERROR = byteArrayOf(
        0x6A.toByte(), // SW1	Status byte 1 - Command processing status
        0x82.toByte()   // SW2	Status byte 2 - Command processing qualifier
    )

    private val NDEF_ID = byteArrayOf(0xE1.toByte(), 0x04.toByte())

    private var NDEF_URI = NdefMessage(createTextRecord("en", "Ciao, come va?", NDEF_ID))
    private var NDEF_URI_BYTES = NDEF_URI.toByteArray()
    private var NDEF_URI_LEN = fillByteArrayToFixedDimension(
        BigInteger.valueOf(NDEF_URI_BYTES.size.toLong()).toByteArray(), 2
    )


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (intent.hasExtra("ndefMessage")) {
            NDEF_URI =
                NdefMessage(intent.getStringExtra("ndefMessage")?.let {
                    createTextRecord("en",
                        it, NDEF_ID)
                })

            NDEF_URI_BYTES = NDEF_URI.toByteArray()
            NDEF_URI_LEN = fillByteArrayToFixedDimension(
                BigInteger.valueOf(NDEF_URI_BYTES.size.toLong()).toByteArray(), 2
            )
        }

        Log.i(TAG, "onStartCommand() | NDEF" + NDEF_URI.toString())

        return Service.START_STICKY
    }

    override fun processCommandApdu(commandApdu: ByteArray, extras: Bundle?): ByteArray {

        //
        // The following flow is based on Appendix E "Example of Mapping Version 2.0 Command Flow"
        // in the NFC Forum specification
        //
        Log.i(TAG, "processCommandApdu() | incoming commandApdu: " + commandApdu.toHex())

        //
        // First command: NDEF Tag Application select (Section 5.5.2 in NFC Forum spec)
        //
        if (Arrays.equals(APDU_SELECT, commandApdu)) {
            Log.i(TAG, "APDU_SELECT triggered. Our Response: " + A_OKAY.toHex())
            return A_OKAY
        }

        //
        // Second command: Capability Container select (Section 5.5.3 in NFC Forum spec)
        //
        if (Arrays.equals(CAPABILITY_CONTAINER_OK, commandApdu)) {
            Log.i(TAG, "CAPABILITY_CONTAINER_OK triggered. Our Response: " + A_OKAY.toHex())
            return A_OKAY
        }

        //
        // Third command: ReadBinary data from CC file (Section 5.5.4 in NFC Forum spec)
        //
        if (Arrays.equals(
                READ_CAPABILITY_CONTAINER,
                commandApdu
            ) && !READ_CAPABILITY_CONTAINER_CHECK
        ) {
            Log.i(
                TAG,
                "READ_CAPABILITY_CONTAINER triggered. Our Response: " + READ_CAPABILITY_CONTAINER_RESPONSE.toHex()
            )
            READ_CAPABILITY_CONTAINER_CHECK = true
            return READ_CAPABILITY_CONTAINER_RESPONSE
        }

        //
        // Fourth command: NDEF Select command (Section 5.5.5 in NFC Forum spec)
        //
        if (Arrays.equals(NDEF_SELECT_OK, commandApdu)) {
            Log.i(TAG, "NDEF_SELECT_OK triggered. Our Response: " + A_OKAY.toHex())
            return A_OKAY
        }

        if (Arrays.equals(NDEF_READ_BINARY_NLEN, commandApdu)) {
            // Build our response
            val response = ByteArray(NDEF_URI_LEN.size + A_OKAY.size)
            System.arraycopy(NDEF_URI_LEN, 0, response, 0, NDEF_URI_LEN.size)
            System.arraycopy(A_OKAY, 0, response, NDEF_URI_LEN.size, A_OKAY.size)

            Log.i(TAG, "NDEF_READ_BINARY_NLEN triggered. Our Response: " + response.toHex())

            READ_CAPABILITY_CONTAINER_CHECK = false
            return response
        }

        if (Arrays.equals(commandApdu.sliceArray(0..1), NDEF_READ_BINARY)) {
            val offset = commandApdu.sliceArray(2..3).toHex().toInt(16)
            val length = commandApdu.sliceArray(4..4).toHex().toInt(16)

            val fullResponse = ByteArray(NDEF_URI_LEN.size + NDEF_URI_BYTES.size)
            System.arraycopy(NDEF_URI_LEN, 0, fullResponse, 0, NDEF_URI_LEN.size)
            System.arraycopy(
                NDEF_URI_BYTES,
                0,
                fullResponse,
                NDEF_URI_LEN.size,
                NDEF_URI_BYTES.size
            )

            Log.i(TAG, "NDEF_READ_BINARY triggered. Full data: " + fullResponse.toHex())
            Log.i(TAG, "READ_BINARY - OFFSET: " + offset + " - LEN: " + length)

            val slicedResponse = fullResponse.sliceArray(offset until fullResponse.size)

            // Build our response
            val realLength = if (slicedResponse.size <= length) slicedResponse.size else length
            val response = ByteArray(realLength + A_OKAY.size)

            System.arraycopy(slicedResponse, 0, response, 0, realLength)
            System.arraycopy(A_OKAY, 0, response, realLength, A_OKAY.size)

            Log.i(TAG, "NDEF_READ_BINARY triggered. Our Response: " + response.toHex())

            READ_CAPABILITY_CONTAINER_CHECK = false
            return response
        }

        //
        // We're doing something outside our scope
        //
        Log.wtf(TAG, "processCommandApdu() | I don't know what's going on!!!")
        return A_ERROR
    }

    override fun onDeactivated(reason: Int) {
        Log.i(TAG, "onDeactivated() Fired! Reason: $reason")
    }


    private val HEX_CHARS = "0123456789ABCDEF".toCharArray()

    fun ByteArray.toHex(): String {
        val result = StringBuffer()

        forEach {
            val octet = it.toInt()
            val firstIndex = (octet and 0xF0).ushr(4)
            val secondIndex = octet and 0x0F
            result.append(HEX_CHARS[firstIndex])
            result.append(HEX_CHARS[secondIndex])
        }

        return result.toString()
    }

    fun String.hexStringToByteArray(): ByteArray {

        val result = ByteArray(length / 2)

        for (i in 0 until length step 2) {
            val firstIndex = HEX_CHARS.indexOf(this[i]);
            val secondIndex = HEX_CHARS.indexOf(this[i + 1]);

            val octet = firstIndex.shl(4).or(secondIndex)
            result.set(i.shr(1), octet.toByte())
        }

        return result
    }

    fun createTextRecord(language: String, text: String, id: ByteArray): NdefRecord {
        val languageBytes: ByteArray
        val textBytes: ByteArray
        try {
            languageBytes = language.toByteArray(charset("US-ASCII"))
            textBytes = text.toByteArray(charset("UTF-8"))
        } catch (e: UnsupportedEncodingException) {
            throw AssertionError(e)
        }

        val recordPayload = ByteArray(1 + (languageBytes.size and 0x03F) + textBytes.size)

        recordPayload[0] = (languageBytes.size and 0x03F).toByte()
        System.arraycopy(languageBytes, 0, recordPayload, 1, languageBytes.size and 0x03F)
        System.arraycopy(
            textBytes,
            0,
            recordPayload,
            1 + (languageBytes.size and 0x03F),
            textBytes.size
        )

        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, id, recordPayload)
    }

    fun fillByteArrayToFixedDimension(array: ByteArray, fixedSize: Int): ByteArray {
        if (array.size == fixedSize) {
            return array
        }

        val start = byteArrayOf(0x00.toByte())
        val filledArray = ByteArray(start.size + array.size)
        System.arraycopy(start, 0, filledArray, 0, start.size)
        System.arraycopy(array, 0, filledArray, start.size, array.size)
        return fillByteArrayToFixedDimension(filledArray, fixedSize)
    }
}