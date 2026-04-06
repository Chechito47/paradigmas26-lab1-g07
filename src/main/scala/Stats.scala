object Stats {
  type Subscription = (String, String) // (subredditName, url) 
  type Post = (String, String, String, String, Int, String) // (subreddit, title, selftext, date, score, permalink)

  def getStats(posts: List[Post]): Int = {
    //Paso 1: Obtengo los scores
    val scores = posts.map(e => e._5)

    //Paso 2: Recorro la lista y voy sumando los scores
    val sumaScores = scores.foldLeft(0) {(acum, num) =>
      acum + num
    }

    sumaScores
  }

  def getPrimeros5(posts: List[Post]): List[Post] = {
    val primeros = posts.take(5)

    primeros
  }
}