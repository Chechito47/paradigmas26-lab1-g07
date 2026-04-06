object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: Option[List[FileIO.Subscription]] = FileIO.readSubscriptions()

    val allPosts: Option[List[FileIO.Post]] = subscriptions.map { subs =>
      subs.flatMap { case (name, url) =>
        val rawJsonOpt = FileIO.downloadFeed(url)
        rawJsonOpt.toList.flatMap(rawJson => FileIO.parsePosts(name, rawJson).getOrElse(Nil))
      }
    }

    val allValidPosts: List[FileIO.Post] = allPosts.getOrElse(Nil).filter(TextProcessing.isValidPost)

    // Agrupamos los posts por subreddit para poder reportar por cada uno
    val postsPorSubreddit = allValidPosts.groupBy { case (subreddit, _, _, _, _, _) => subreddit }

    // Iteramos por cada subreddit
    postsPorSubreddit.foreach { case (name, postsDeReddit) =>
      val countWords = CountWords.countw(postsDeReddit)
      val stats = Stats.getStats(postsDeReddit)
      val primeros5 = Stats.getPrimeros5(postsDeReddit)
      Formatters.imprimirReporte(name, stats, countWords, primeros5)
    }


    /*val output = allPosts
      .map { case (url, posts) => Formatters.formatSubscription(url, posts) }
      .mkString("\n")

    println(output)*/
  }
}
