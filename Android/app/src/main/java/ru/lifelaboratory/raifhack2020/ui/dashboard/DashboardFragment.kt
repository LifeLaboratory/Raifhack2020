package ru.lifelaboratory.raifhack2020.ui.dashboard

import android.Manifest.permission.CALL_PHONE
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.lifelaboratory.raifhack2020.R
import ru.lifelaboratory.raifhack2020.utils.*


class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
                ViewModelProvider(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val nameClientView: TextView = root.findViewById(R.id.name_client)
        val addresClientView: TextView = root.findViewById(R.id.address_client)
        val phoneClientView: TextView = root.findViewById(R.id.phone_client)
        val qrClientView: ImageView = root.findViewById(R.id.qr_client)
        val statusOrderClientView: TextView = root.findViewById(R.id.status_order_client)

        run {
            val server = ServerAPI.create()
            server.getClient(idClient).enqueue(object : Callback<Client> {
                override fun onFailure(call: Call<Client>?, t: Throwable?) {
                    Log.e("TEST", t.toString())
                }

                override fun onResponse(call: Call<Client>?, response: Response<Client>?) {
                    Log.e("TEST", response?.body().toString())
                    nameClientView.text = response?.body()!!.name + " " + response?.body()!!.surname
                    addresClientView.text = addressClient
                    phoneClientView.text = response?.body()!!.number.toString()
                    statusOrderClientView.text = statusOrderClient
                    Picasso.with(context)
                        .load(qrUrlClient)
                        .placeholder(R.drawable.logo_life)
                        .error(R.drawable.logo_life)
                        .fit().centerInside()
                        .into(qrClientView)

                    phoneClientView.setOnClickListener(object: View.OnClickListener{
                        override fun onClick(v: View?) {
                            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + response?.body()!!.number.toString()))
                            if (ContextCompat.checkSelfPermission(context!!, CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                startActivity(intent);
                            } else {
                                requestPermissions(arrayOf(CALL_PHONE), 1);
                            }
                        }
                    })

                    addresClientView.setOnClickListener {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=" + response?.body()!!.city + " " + addressClient)
                        )
                        startActivity(intent)
                    }
                }

            })
        }

        return root
    }
}