import scala.io.Source
import scala.util.Using
import org.json4s._
import org.json4s.jackson.JsonMethods._

object FileIO {

  type Subscription = (String, String) // (subredditName, url) 
  type Post = (String, String, String, String) // (subreddit, title, selftext, date)

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

  // Pure function to parse posts from a JSON string
  def parsePosts(subredditName: String, jsonRaw: String): List[Post] = {
    implicit val formats = DefaultFormats
    val json = parse(jsonRaw)

    // Navega la estructura data -> children (transforma en lista)
    val children = (json \ "data" \ "children").children

    // Usamos map para transformar cada elemento del JSON en un Post
    children.map { child =>
      val d = child \ "data"
      val title = (d \ "title").extract[String]
      val selftext = (d \ "selftext").extract[String]
      val createdUtc = (d \ "created_utc").extract[Double].toLong
      val date = Formatters.formatDateFromUTC(createdUtc)

      // Return
      (subredditName, title, selftext, date)
    }
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): String = {
    // Usamos Using para evitar resource leaks en la conexión
    Using.resource(Source.fromURL(url)) { source =>
      source.mkString
    }
  }
}