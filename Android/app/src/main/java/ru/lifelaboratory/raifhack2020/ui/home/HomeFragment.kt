package ru.lifelaboratory.raifhack2020.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.lifelaboratory.raifhack2020.MainActivity
import ru.lifelaboratory.raifhack2020.R
import ru.lifelaboratory.raifhack2020.ui.dashboard.DashboardFragment
import ru.lifelaboratory.raifhack2020.utils.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val listOfOrders: ListView = root.findViewById(R.id.list_orders)

        run {
            ServerAPI.create().getOrders(1).enqueue(object : Callback<List<Order>> {
                override fun onFailure(call: Call<List<Order>>?, t: Throwable?) {
                    Log.e("TEST", t.toString())
                }

                override fun onResponse(
                    call: Call<List<Order>>?,
                    response: Response<List<Order>>?
                ) {
                    val listOfOrder: Array<String?> = arrayOfNulls<String>(response?.body()!!.size)
                    for (i in 0 until listOfOrder.size) {
                        listOfOrder[i] = response?.body()!![i].address
                    }
                    val adapter =
                        ArrayAdapter(context!!, android.R.layout.simple_list_item_1, listOfOrder)
                    listOfOrders.adapter = adapter

                    listOfOrders.setOnItemClickListener { parent, view, position, id ->
                        idClient = response?.body()!![position].number_client
                        qrUrlClient = response?.body()!![position].qr_code
                        addressClient = response?.body()!![position].address
                        Log.e("TEST", response?.body()!![position].toString())
                        when (response?.body()!![position].status_order) {
                            null -> statusOrderClient = "В процессе"
                            true -> statusOrderClient = "Выполнен"
                            false -> statusOrderClient = "Отменен"
                        }
                        fragmentManager!!.beginTransaction().replace(R.id.nav_host_fragment, DashboardFragment()).commit()
                    }
                }
            })
        }
        return root
    }
}