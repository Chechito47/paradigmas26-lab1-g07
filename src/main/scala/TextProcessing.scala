object TextProcessing {

  // Función que devuelve true si post con selftext vacío
  def isValidSelftext(post: FileIO.Post): Boolean = { 
    var selftext = post._3
    selftext.trim.nonEmpty
  }

  // Función que filtra posts que tengan solo espacios = isValidSelftext

  // Función que filtra posts que no tenga tittle
  def isValidTitle(post: FileIO.Post) : Boolean = {
    var text = post._2
    text.trim.nonEmpty
  }

  // Función que dice si un post es válido
  def isValidPost(post: FileIO.Post) : Boolean = {
    isValidSelftext(post) && isValidTitle(post)
  }
}