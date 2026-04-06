import org.json4s._
import org.json4s.jackson.JsonMethods._
import scala.io.Source
import scala.util.Using

object FileIO {

  type Subscription = (String, String) // (subredditName, url) 
  type Post = (String, String, String, String, Int, String) // (subreddit, title, selftext, date, score, permalink)

  // Pure function to read subscriptions from a JSON file
  def readSubscriptions(): Option[List[Subscription]] = {
    try {
      Some(Using.resource(Source.fromFile("src/main/scala/subscriptions.json")) { source =>
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
      }})
    }
    catch {
      case _: Exception => None
    }
  }

  // Pure function to parse posts from a JSON string
  def parsePosts(subredditName: String, jsonRaw: String): Option[List[Post]] = {
    try {
      implicit val formats = DefaultFormats
      val json = parse(jsonRaw)

      // Navega la estructura data -> children (transforma en lista)
      val children = (json \ "data" \ "children").children

      // Usamos map para transformar cada elemento del JSON en un Post
      Some(children.map { child =>
        val d = child \ "data"
        val title = (d \ "title").extract[String]
        val selftext = (d \ "selftext").extract[String]
        val createdUtc = (d \ "created_utc").extract[Double].toLong
        val date = Formatters.formatDateFromUTC(createdUtc)
        val score = (d \ "score").extract[Int]
        val permalink = (d \ "permalink").extract[String]

        // Return
        (subredditName, title, selftext, date, score, permalink)
      })
    }
    catch {
      case _: Exception => None
    }
  }

  // Pure function to download JSON feed from a URL
  def downloadFeed(url: String): Option[String] = {
    try {
      // Usamos Using para evitar resource leaks en la conexión
      Some( Using.resource(Source.fromURL(url)) { source =>
      source.mkString
      })
    }
    catch {
      case _: Exception => None
    }
  }
}