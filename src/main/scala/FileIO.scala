import scala.io.Source
import org.json4s._
import org.json4s.jackson.JsonMethods._

object FileIO {

  type Subscription = (String, String) // (subredditName, url) 

  // Pure function to read subscriptions from a JSON file
  def readSubscriptions(): List[Subscription] = {
    val source = Source.fromFile("src/main/scala/subscriptions.json")
    val content = source.mkString
    source.close()
    
    implicit val formats = DefaultFormats

    parse(content) match {
      case JArray(items) => items.map { x =>
        (
          (x \ "name").extract[String],
          (x \ "url").extract[String]
        )
      }
      case _ => List.empty
    }
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): String = {
    val source = Source.fromURL(url)
    source.mkString
  }
}
