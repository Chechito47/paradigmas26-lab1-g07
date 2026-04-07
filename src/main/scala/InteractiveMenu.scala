object InteractiveMenu {

  type Post         = FileIO.Post
  type Subscription = FileIO.Subscription

  // Genera el texto del menú de suscripciones a partir de una lista
  def renderSubscriptionMenu(subs: List[Subscription]): String = {
    val items = subs.zipWithIndex.map { case ((name, _), idx) =>
      s"  ${idx + 1}. $name"
    }
    ("\n=== Suscripciones disponibles ===" :: items ::: List("  0. Salir"))
      .mkString("\n")
  }

  // Genera el texto del menú de posts de una suscripción
  def renderPostMenu(subreddit: String, posts: List[Post]): String = {
    val items = posts.zipWithIndex.map { case ((_, title, _, date, score, _), idx) =>
      s"  ${idx + 1}. [$score pts] $title  ($date)"
    }
    (s"\n=== Posts de r/$subreddit ===" :: items ::: List("  0. Volver"))
      .mkString("\n")
  }

  // Genera la vista completa de un post individual
  def renderPost(post: Post): String = {
    val (subreddit, title, selftext, date, score, permalink) = post
    s"""
    ${"─" * 70}
    Subreddit : r/$subreddit
    Título    : $title
    Fecha     : $date
    Score     : $score
    URL       : https://www.reddit.com$permalink
    ${"─" * 70}
    $selftext
    ${"─" * 70}"""
  }

  // Devuelve Some(n) si la entrada es un número dentro del rango [0, max], None si no
  def parseChoice(input: String, max: Int): Option[Int] =
    input.trim.toIntOption.filter(n => n >= 0 && n <= max)

  def run(postsBySubreddit: Map[String, List[Post]], subs: List[Subscription]): Unit = {
    // Bucle de selección de suscripción
    def loopSubs(): Unit = {
      println(renderSubscriptionMenu(subs))
      print("Elegí una suscripción: ")
      val raw = scala.io.StdIn.readLine()

      parseChoice(raw, subs.length) match {
        case None =>
          println("Opción inválida, intentá de nuevo.")
          loopSubs()

        case Some(0) =>
          println("Hasta luego")

        case Some(n) =>
          val (name, _) = subs(n - 1)
          val posts = postsBySubreddit.getOrElse(name, Nil)
          loopPosts(name, posts)
          loopSubs()   // vuelve al menú de suscripciones al terminar
      }
    }

    // Bucle de selección de post dentro de una suscripción
    def loopPosts(name: String, posts: List[Post]): Unit = {
      println(renderPostMenu(name, posts))
      print("Elegí un post: ")
      val raw = scala.io.StdIn.readLine()

      parseChoice(raw, posts.length) match {
        case None =>
          println("Opción inválida, intentá de nuevo.")
          loopPosts(name, posts)

        case Some(0) =>
          ()   // vuelve al menú anterior

        case Some(n) =>
          println(renderPost(posts(n - 1)))
          print("\nPresioná Enter para continuar...")
          scala.io.StdIn.readLine()
          loopPosts(name, posts)
      }
    }

    loopSubs()
  }
}