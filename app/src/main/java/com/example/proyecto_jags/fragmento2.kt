package com.example.proyecto_jags

import android.widget.TextView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager




class ClienteAdapter(
    private val clientes: List<Cliente>
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val txtClave: TextView =
            itemView.findViewById(R.id.txtClaveItem)

        val txtNombre: TextView =
            itemView.findViewById(R.id.txtNombreItem)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClienteViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_cliente,
                parent,
                false
            )

        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ClienteViewHolder,
        position: Int
    ) {

        val cliente = clientes[position]

        holder.txtClave.text = cliente.clave
        holder.txtNombre.text = cliente.nombre
    }

    override fun getItemCount(): Int {
        return clientes.size
    }
}


class fragmento2 : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnNuevo: Button

    private lateinit var txtClave: EditText
    private lateinit var txtNombre: EditText
    private lateinit var txtEdad: EditText
    private lateinit var txtFecha: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_fragmento2, container, false)

        // 🔹 Inicializar vistas
        recyclerView = view.findViewById(R.id.rvClientes)
        btnNuevo = view.findViewById(R.id.btnNuevo)

        txtClave = view.findViewById(R.id.txtClave)
        txtNombre = view.findViewById(R.id.txtNombre)
        txtEdad = view.findViewById(R.id.txtEdad)
        txtFecha = view.findViewById(R.id.txtFecha)

        // 🔹 Click botón limpiar
        btnNuevo.setOnClickListener {
            txtClave.setText("")
            txtNombre.setText("")
            txtEdad.setText("")
            txtFecha.setText("")
        }

        // 🔹 Cargar datos
        cargarClientes()

        return view
    }

    private fun cargarClientes() {

        RetrofitClient.clienteService
            .obtenerClientes()
            .enqueue(object : Callback<List<Cliente>> {

                override fun onResponse(
                    call: Call<List<Cliente>>,
                    response: Response<List<Cliente>>
                ) {

                    if (response.isSuccessful) {

                        val lista = response.body() ?: emptyList()

                        recyclerView.layoutManager =
                            LinearLayoutManager(requireContext())

                        recyclerView.adapter = ClienteAdapter(lista)
                    }
                }

                override fun onFailure(
                    call: Call<List<Cliente>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        requireContext(),
                        t.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    // 🔹 función para llenar campos cuando seleccionas un cliente
    fun mostrarCliente(cliente: Cliente) {

        txtClave.setText(cliente.clave)
        txtNombre.setText(cliente.nombre)
        txtEdad.setText(cliente.edad.toString())
        txtFecha.setText(cliente.fecha_Nacimiento)
    }
}