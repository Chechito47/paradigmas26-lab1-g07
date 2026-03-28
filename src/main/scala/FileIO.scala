import scala.io.Source
import scala.util.Using
import org.json4s._
import org.json4s.jackson.JsonMethods._

object FileIO {

  type Subscription = (String, String) // (subredditName, url) 

  // Pure function to read subscriptions from a JSON file
  def readSubscriptions(): List[Subscription] = {
    Using.resource(Source.fromFile("src/main/scala/subscriptions.json")) { source =>
      val content = source.mkString
      
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
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): String = {
    val source = Source.fromURL(url)
    source.mkString
  }
}
