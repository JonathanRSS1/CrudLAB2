package Modelo


data class tbTickets(
    val uuid: String,
    val Numero : Int,
    val Titulo : String,
    val Descripcion : String,
    val Autor : String,
    val Email_Autor : String,
    val Fecha_Creacion : String,
    val Estado : String,
    val Fecha_finalizacion : String
)
