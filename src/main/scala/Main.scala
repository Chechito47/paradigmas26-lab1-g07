object Main {
  def main(args: Array[String]): Unit = {
    val header = s"Reddit Post Parser\n${"=" * 40}"

    val subscriptions: List[(String,String)] = FileIO.readSubscriptions()

    val allPosts: List[FileIO.Post] = subscriptions.flatMap { case (name, url) =>
      println(s"Fetching posts from : r/$name...")
    
      val rawJson = FileIO.downloadFeed(url)

      FileIO.parsePosts(name, rawJson)
    }

    /*val allPosts: List[(String, String)] = subscriptions.map { url =>
      println(s"Fetching posts from: $url")
      val posts = FileIO.downloadFeed(url)
      (url, posts)
    }

    val output = allPosts
      .map { case (url, posts) => Formatters.formatSubscription(url, posts) }
      .mkString("\n")

    println(output)*/
  }
}
