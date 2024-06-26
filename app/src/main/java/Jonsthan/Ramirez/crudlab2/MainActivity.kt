package Jonsthan.Ramirez.crudlab2

import Modelo.ClaseConexion
import Modelo.tbTickets
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import recyclerviewhelpers.Adaptador
import java.util.UUID

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

// 1 Mandar a llamar a todos los elementos de la vista

        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val txtFechaCreacion = findViewById<EditText>(R.id.txtFechaCreacion)
        val txtFechaFinalizacion = findViewById<EditText>(R.id.txtFechaFinalizacion)
        val txtNumero = findViewById<EditText>(R.id.txtNumero)
        val txtAutor = findViewById<EditText>(R.id.txtAutor)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtEstado = findViewById<EditText>(R.id.txtEstado)
        val btnInsertar = findViewById<Button>(R.id.btnInsertar)
        val rcvTickets = findViewById<RecyclerView>(R.id.rcvTickets)

        //Agrego un layaout al RecyclerView

        rcvTickets.layoutManager = LinearLayoutManager(this)


        //////////////// TODO :  mostrar datos


        fun obtenerTickets(): List<tbTickets>{

            //1- Crear un onjeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Creo un Statement
            val statement = objConexion?.createStatement()

            val resultSet = statement?.executeQuery("SELECT * FROM tbTickets")!!

            val listaTickets = mutableListOf<tbTickets>()

            while (resultSet.next()){
                val uuid = resultSet.getString("uuid")
                val Numero = resultSet.getInt("Numero")
                val Titulo = resultSet.getString("Titulo")
                val Descripcion = resultSet.getString("Descripcion")
                val Autor = resultSet.getString("Autor")
                val Email_Autor = resultSet.getString("Email_Autor")
                val Fecha_Creacion = resultSet.getString("Fecha_Creacion")
                val Estado = resultSet.getString("Estado")
                val Fecha_finalizacion = resultSet.getString("Fecha_finalizacion")

                val valoresJuntos = tbTickets(uuid, Numero, Titulo, Descripcion, Autor, Email_Autor, Fecha_Creacion, Estado, Fecha_finalizacion)

                listaTickets.add(valoresJuntos)


            }

            return listaTickets
        }

        // Asignarle el adaptador al RecyclerView

        CoroutineScope(Dispatchers.IO).launch {


            val TicketsDB = obtenerTickets()
            withContext(Dispatchers.Main){

                val adapter = Adaptador(TicketsDB)

                rcvTickets.adapter= adapter
            }


        }




        // 2 Programar el boton insertar

        btnInsertar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                //1-Crear un objeto de la clase conexion

                val objConexion = ClaseConexion().cadenaConexion()

                // Crear una variable que contenga un PrepareStatement


                val addTicket = objConexion?.prepareStatement("insert into tbTickets (uuid, Numero, Titulo, Descripcion, Autor, Email_Autor, Fecha_Creacion, Estado, Fecha_finalizacion)  VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)")!!
                addTicket.setString(1, UUID.randomUUID().toString())
                addTicket.setInt(2, txtNumero.text.toString().toInt())
                addTicket.setString(3, txtTitulo.text.toString())
                addTicket.setString(4, txtDescripcion.text.toString())
                addTicket.setString(5, txtAutor.text.toString())
                addTicket.setString(6, txtEmail.text.toString())
                addTicket.setString(7, txtFechaCreacion.text.toString())
                addTicket.setString(8, txtEstado.text.toString())
                addTicket.setString(9, txtFechaFinalizacion.text.toString())
                addTicket.executeUpdate()

            }

        }


    }
}