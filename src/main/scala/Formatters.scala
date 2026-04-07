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

  def printReport(name: String, totalScore: Int, words: Map[String, Int], firstFive: List[FileIO.Post]): Unit = {
    println(s"===$name===")
    println(s"The score is: $totalScore")

    val wordsSorted = words.toList.sortBy(p => -p._2).take(10)
    wordsSorted.foreach { case (word, count) =>
      println(s"$word: $count")
    }

    firstFive.foreach { case (subreddit, title, selftext, date, score, permalink) =>
      println(s"$title, $date, https://www.reddit.com$permalink")
    }
  }
}
