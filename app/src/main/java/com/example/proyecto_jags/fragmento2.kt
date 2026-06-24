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
import androidx.appcompat.app.AlertDialog


class ClienteAdapter(
    private val clientes: List<Cliente>,
    private val onClick: (Cliente) -> Unit
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

        val view = LayoutInflater
            .from(parent.context)
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

        holder.itemView.setOnClickListener {
            onClick(cliente)
        }
    }

    override fun getItemCount(): Int {
        return clientes.size
    }
}






class fragmento2 : Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var btnNuevo: Button
    private lateinit var btnGuardar: Button
    private lateinit var btnEliminar: Button

    private lateinit var txtClave: EditText
    private lateinit var txtNombre: EditText
    private lateinit var txtEdad: EditText
    private lateinit var txtFecha: EditText

    private var clienteExiste = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_fragmento2,
            container,
            false
        )

        recyclerView = view.findViewById(R.id.rvClientes)

        btnNuevo = view.findViewById(R.id.btnNuevo)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnEliminar = view.findViewById(R.id.btnEliminar)

        txtClave = view.findViewById(R.id.txtClave)
        txtNombre = view.findViewById(R.id.txtNombre)
        txtEdad = view.findViewById(R.id.txtEdad)
        txtFecha = view.findViewById(R.id.txtFecha)

        recyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        btnNuevo.setOnClickListener {
            limpiarCampos()
        }

        btnGuardar.setOnClickListener {

            if (
                txtClave.text.isEmpty() ||
                txtNombre.text.isEmpty() ||
                txtEdad.text.isEmpty() ||
                txtFecha.text.isEmpty()
            ) {

                Toast.makeText(
                    requireContext(),
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            val cliente = Cliente(
                clave = txtClave.text.toString(),
                nombre = txtNombre.text.toString(),
                edad = txtEdad.text.toString().toInt(),
                fecha_Nacimiento = txtFecha.text.toString()
            )

            if (clienteExiste)
                actualizarCliente(cliente)
            else
                insertarCliente(cliente)
        }

        btnEliminar.setOnClickListener {

            val clave = txtClave.text.toString()

            if (clave.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Seleccione un cliente",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Eliminar")
                .setMessage("¿Desea eliminar este cliente?")
                .setPositiveButton("Sí") { _, _ ->
                    eliminarCliente(clave)
                }
                .setNegativeButton("No", null)
                .show()
        }

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

                        val lista =
                            response.body() ?: emptyList()

                        recyclerView.adapter =
                            ClienteAdapter(lista) { cliente ->

                                mostrarCliente(cliente)
                            }
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

    private fun insertarCliente(cliente: Cliente) {

        RetrofitClient.clienteService
            .insertarCliente(cliente)
            .enqueue(object : Callback<Any> {

                override fun onResponse(
                    call: Call<Any>,
                    response: Response<Any>
                ) {

                    Toast.makeText(
                        requireContext(),
                        "Cliente agregado",
                        Toast.LENGTH_SHORT
                    ).show()

                    limpiarCampos()
                    cargarClientes()
                }

                override fun onFailure(
                    call: Call<Any>,
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

    private fun actualizarCliente(cliente: Cliente) {

        RetrofitClient.clienteService
            .actualizarCliente(cliente)
            .enqueue(object : Callback<Any> {

                override fun onResponse(
                    call: Call<Any>,
                    response: Response<Any>
                ) {

                    Toast.makeText(
                        requireContext(),
                        "Cliente actualizado",
                        Toast.LENGTH_SHORT
                    ).show()

                    cargarClientes()
                }

                override fun onFailure(
                    call: Call<Any>,
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

    private fun eliminarCliente(clave: String) {

        RetrofitClient.clienteService
            .eliminarCliente(clave)
            .enqueue(object : Callback<Any> {

                override fun onResponse(
                    call: Call<Any>,
                    response: Response<Any>
                ) {

                    Toast.makeText(
                        requireContext(),
                        "Cliente eliminado",
                        Toast.LENGTH_SHORT
                    ).show()

                    limpiarCampos()
                    cargarClientes()
                }

                override fun onFailure(
                    call: Call<Any>,
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

    private fun limpiarCampos() {

        txtClave.setText("")
        txtNombre.setText("")
        txtEdad.setText("")
        txtFecha.setText("")

        clienteExiste = false
    }

    fun mostrarCliente(cliente: Cliente) {

        txtClave.setText(cliente.clave)
        txtNombre.setText(cliente.nombre)
        txtEdad.setText(cliente.edad.toString())
        txtFecha.setText(cliente.fecha_Nacimiento)

        clienteExiste = true
    }
}