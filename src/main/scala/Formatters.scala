import java.util.Date
import java.text.SimpleDateFormat


object Formatters {
  type Subscription = (String, String) // (subredditName, url) 
  type Post = (String, String, String, String, Int, String) // (subreddit, title, selftext, date, score, permalink)

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


  def imprimirReporte(name: String, totalScore: Int, words: Map[String, Int], primeros5: List[Post]): Unit = {
    //Paso 1: Imprimo nombre y suma total de cada post
    println(s"===$name===")
    println(s"El score es: $totalScore")

    //Paso 2: Imprimo las 10 palabras mas frecuentes con sus ocurrencias
    val wordsSorted = words.toList.sortBy(p => -p._2).take(10)
    wordsSorted.foreach { case (word, count) =>
      println(s"$word: $count")
    }

    //Paso 3: Imprimo los primeros 5 posts con sus url
    primeros5.foreach { case (subreddit, title, selftext, date, score, permalink) =>
      println(s"$title, $date, https://www.reddit.com$permalink")
    }
  }
}
