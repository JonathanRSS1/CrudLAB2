package recyclerviewhelpers

import Jonsthan.Ramirez.crudlab2.R
import Modelo.ClaseConexion
import Modelo.tbTickets
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.SQLException

class Adaptador(var Datos: List<tbTickets>): RecyclerView.Adapter<ViewHolder>() {


    fun actualizarLista(nuevaLista: List<tbTickets>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
    }






    /////////////////// TODO: Eliminar datos
    fun eliminarDatos(posicion: Int) {
        // 1. Obtener el ticket a eliminar
        val ticketEliminado = Datos[posicion]
        // 2. Eliminar el ticket de la lista
        val listaMutable = Datos.toMutableList()
        listaMutable.removeAt(posicion)
        Datos = listaMutable.toList()

        // 3. Notificar al adaptador del cambio
        notifyItemRemoved(posicion)

        // 4. Eliminar el ticket de la base de datos
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                val deleteStatement = objConexion?.prepareStatement("DELETE FROM tbTickets WHERE uuid = ?")
                deleteStatement?.setString(1, ticketEliminado.uuid)
                deleteStatement?.executeUpdate()
                objConexion?.commit()
            } catch (e: SQLException) {
                Log.e("EliminarTicket", "Error al eliminar ticket", e)
                // Manejar el error de forma adecuada

            }


        }
    }



    fun actualizarDato(ticketActualizado: tbTickets,recyclerView: RecyclerView) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                val updateTicket = objConexion?.prepareStatement(
                    "UPDATE tbTickets SET Numero = ?, Titulo = ?, Descripcion = ?, Autor = ?, " +
                            "Email_Autor = ?, Fecha_Creacion = ?, Estado = ?, Fecha_finalizacion = ? WHERE uuid = ?"
                )
                updateTicket?.setInt(1, ticketActualizado.Numero)
                updateTicket?.setString(2, ticketActualizado.Titulo)
                updateTicket?.setString(3, ticketActualizado.Descripcion)
                updateTicket?.setString(4, ticketActualizado.Autor) // Agrega esta línea
                updateTicket?.setString(5, ticketActualizado.Email_Autor)
                updateTicket?.setString(6, ticketActualizado.Fecha_Creacion)
                updateTicket?.setString(7, ticketActualizado.Estado)
                updateTicket?.setString(8, ticketActualizado.Fecha_finalizacion)
                updateTicket?.setString(9, ticketActualizado.uuid)
                updateTicket?.executeUpdate()

                withContext(Dispatchers.Main) {
                    val indice = Datos.indexOfFirst { it.uuid == ticketActualizado.uuid }
                    if (indice != -1) {
                        val listaMutable = Datos.toMutableList()
                        listaMutable[indice] = ticketActualizado
                        Datos = listaMutable.toList()
                        notifyItemChanged(indice)
                        // Obtiene el ViewHoldery actualiza la vista
                        val viewHolder =
                            (recyclerView.findViewHolderForAdapterPosition(indice) as? ViewHolder)
                        viewHolder?.bind(ticketActualizado)

                    }
                }
            }catch (e: SQLException) {
                Log.e("ActualizarDato", "Error al actualizar ticket", e)
                // Manejar el error de forma adecuada
            }
        }
    }








    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       // Unir el RecyclerView con la card

        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         // Controlar a la card

        val item1 = Datos[position]
        holder.txtNombreCard.text = item1.Titulo

        val item2 = Datos[position]
        holder.txtNumeroCard.text = item2.Numero.toString()

        val item3 = Datos[position]
        holder.txtAutorCard.text = item3.Autor

        val item4 = Datos[position]
        holder.txtDescripcionCard.text = item4.Descripcion

        val item5 = Datos[position]
        holder.txtEstadoCard.text = item5.Estado

        val item6 = Datos[position]
        holder.txtEmailCard.text = item6.Email_Autor

        val item7 = Datos[position]
        holder.txtFechaCreacionCard.text = item7.Fecha_Creacion

        val item8 = Datos[position]
        holder.txtFechaFinalizacionCard.text = item8.Fecha_finalizacion



        //todo: clic al icono de eliminar
        holder.imgBorrar.setOnClickListener {

            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar la mascota?")

            //Botones
            builder.setPositiveButton("Si") { dialog, which ->
                eliminarDatos(position)
            }

            builder.setNegativeButton("No"){dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

        holder.imgEditar.setOnClickListener {
            val context = holder.itemView.context
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Actualizar Ticket")

            val view = LayoutInflater.from(context).inflate(R.layout.dialogo_editar_ticket, null) // Crea un layout para el diálogo
            builder.setView(view)

            val txtNumero = view.findViewById<EditText>(R.id.txtNumero)
            val txtTitulo = view.findViewById<EditText>(R.id.txtTituloEditar)
            val txtDescripcion = view.findViewById<EditText>(R.id.txtDescripcion)
            val txtAutor = view.findViewById<EditText>(R.id.txtAutor)
            val txtEmail = view.findViewById<EditText>(R.id.txtEmail)
            val txtFechaCreacion = view.findViewById<EditText>(R.id.txtFechaCreacion)
            val txtEstado = view.findViewById<EditText>(R.id.txtEstado)
            val txtFechaFinalizacion = view.findViewById<EditText>(R.id.txtFechaFinalizacion)

            // Llena los cuadros de texto con los datos actuales del ticket
            txtNumero.setText(item1.Numero.toString())
            txtTitulo.setText(item1.Titulo)
            txtDescripcion.setText(item1.Descripcion)
            txtAutor.setText(item1.Autor)
            txtEmail.setText(item1.Email_Autor)
            txtFechaCreacion.setText(item1.Fecha_Creacion)
            txtEstado.setText(item1.Estado)
            txtFechaFinalizacion.setText(item1.Fecha_finalizacion)

            builder.setPositiveButton("Actualizar") { dialog, which ->
                val ticketActualizado = tbTickets(
                    item1.uuid, txtNumero.text.toString().toIntOrNull() ?: item1.Numero, // Manejo de errores si no es un número
                    txtTitulo.text.toString(),
                    txtDescripcion.text.toString(),
                    txtAutor.text.toString(),
                    txtEmail.text.toString(),
                    txtFechaCreacion.text.toString(),
                    txtEstado.text.toString(),
                    txtFechaFinalizacion.text.toString()
                )
                actualizarDato(ticketActualizado, holder.itemView.parent as  RecyclerView)
            }

            builder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

    }
}