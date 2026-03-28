import java.util.Date
import java.text.SimpleDateFormat


object Formatters {
  def formatDateFromUTC(utc: Long): String = {
    // Reddit da el tiempo en segundos, Java Date pide milisegundos (por eso * 1000)
    val date = new Date(utc * 1000L) 
    
    // Formato estándar (Año-Mes-Día Hora:Minutos)
    val format = new SimpleDateFormat("yyyy-MM-dd HH:mm")
    
    format.format(date)
  }


  // Pure function to format posts from a subscription
  def formatSubscription(url: String, posts: String): String = {
    val header = s"\n${"=" * 80}\nPosts from: $url \n${"=" * 80}"
    val formattedPosts = posts.take(80)
    header + "\n" + formattedPosts
  }
}
