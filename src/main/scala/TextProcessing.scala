object TextProcessing {
  def formatDateFromUTC(timestamp: Long): Option[String] = {
    try {
      val date = new java.util.Date(timestamp * 1000)
      Some(date.toString)
    }
    catch {
      case _: Exception => None
    }
  }

  //funcion que devuelve true si post con selftext vacío
  def isValidSelftext (Post: (String, String, String, String)): Boolean = { 
    var selftext = Post._3
    selftext.trim.nonEmpty // true si no es vacio
  }

  //función que filtra posts que tengan solo espacios = isValidSelftext

  //funcion que filtra posts que no tenga titulo
  def isValidTitle (Post: (String, String, String, String)) : Boolean = {
    var text = Post._2
    text.trim.nonEmpty
  }

  //funcion que dice si un post es valido
  def isValidPost (Post: (String, String, String, String)) : Boolean = {
    isValidSelftext(Post) && isValidTitle(Post)
  }
}