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

    /*val output = allPosts
      .map { case (url, posts) => Formatters.formatSubscription(url, posts) }
      .mkString("\n")

    println(output)*/
  }
}
