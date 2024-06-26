package recyclerviewhelpers

import Jonsthan.Ramirez.crudlab2.R
import Modelo.tbTickets
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    //En el viewHolder mando a llama a los elementos de la Card

    val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
    val imgBorrar: ImageView = view.findViewById(R.id.imgBorrar)


    val txtNumeroCard = view.findViewById<TextView>(R.id.txtNumeroCard)
    val txtNombreCard = view.findViewById<TextView>(R.id.txtNombreCard)
    val txtDescripcionCard = view.findViewById<TextView>(R.id.txtDescripcionCard)
    val txtAutorCard = view.findViewById<TextView>(R.id.txtAutorCard)
    val txtEmailCard = view.findViewById<TextView>(R.id.txtEmailCard)
    val txtEstadoCard = view.findViewById<TextView>(R.id.txtEstadoCard)
    val txtFechaCreacionCard = view.findViewById<TextView>(R.id.txtFechaCreacionCard)
    val txtFechaFinalizacionCard = view.findViewById<TextView>(R.id.txtFechaCreacionCard)



    fun bind(ticket: tbTickets) {
        txtNombreCard.text = ticket.Titulo
        txtNumeroCard.text = ticket.Numero.toString()
        txtAutorCard.text = ticket.Autor
        txtDescripcionCard.text = ticket.Descripcion
        txtEstadoCard.text = ticket.Estado
        txtEmailCard.text = ticket.Email_Autor
        txtFechaCreacionCard.text = ticket.Fecha_Creacion
        txtFechaFinalizacionCard.text = ticket.Fecha_finalizacion
    }




}