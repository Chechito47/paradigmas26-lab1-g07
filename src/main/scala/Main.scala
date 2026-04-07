object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"
    println(header)

    val subscriptions: Option[List[FileIO.Subscription]] = FileIO.readSubscriptions()

    val allPosts: Option[List[FileIO.Post]] = subscriptions.map { subs =>
      // flatMap aplana List[List[Post]] -> List[Post] para que no haya error de tipo
      subs.flatMap { case (name, url) =>
        val rawJsonOpt = FileIO.downloadFeed(url)
        rawJsonOpt match {
          case Some(rawJson) =>
            FileIO.parsePosts(name, rawJson).getOrElse(Nil)
          case None =>
            Nil
        }
      }
    }

    val allValidPosts: List[FileIO.Post] = allPosts match {
      case Some(posts) =>
        posts.filter(TextProcessing.isValidPost)
      case None =>
        Nil
    }

    val postsBySubreddit: Map[String, List[FileIO.Post]] = allValidPosts.groupBy { 
      case (subreddit, _, _, _, _, _) => subreddit }

    postsBySubreddit.foreach { case (name, postsReddit) =>
      val countWords = CountWords.countW(postsReddit)
      val stats = Stats.getStats(postsReddit)
      val firstFive = Stats.getFirstFive(postsReddit)
      Formatters.printReport(name, stats, countWords, firstFive)
    }

    val subs: List[FileIO.Subscription] = subscriptions.getOrElse(Nil)
    InteractiveMenu.run(postsBySubreddit, subs)
  }
}
