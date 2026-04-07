object Stats {

  def getStats(posts: List[FileIO.Post]): Int = {
    val scores = posts.map(e => e._5)

    val sumScores = scores.foldLeft(0) {(acum, num) =>
      acum + num
    }

    sumScores
  }

  def getFirstFive(posts: List[FileIO.Post]): List[FileIO.Post] = {
    val first = posts.take(5)

    first
  }
}