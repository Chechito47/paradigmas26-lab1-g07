object CountWords {
  type Subscription = (String, String) // (subredditName, url) 
  type Post = (String, String, String, String, Int, String) // (subreddit, title, selftext, date, score, permalink)

  val stopwords = List(    "the", "about", "above", "after", "again", "against", "all", "am", "an",
    "and", "any", "are", "aren't", "as", "at", "be", "because", "been",
    "before", "being", "below", "between", "both", "but", "by", "can't",
    "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't",
    "doing", "don't", "down", "during", "each", "few", "for", "from", "further",
    "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "he'd",
    "he'll", "he's", "her", "here", "here's", "hers", "herself", "him",
    "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if",
    "in", "into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me",
    "more", "most", "mustn't", "my", "myself", "no", "nor", "not", "of", "off",
    "on", "once", "only", "or", "other", "ought", "our", "ours", "ourselves",
    "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's",
    "should", "shouldn't", "so", "some", "such", "than", "that", "that's",
    "the", "their", "theirs", "them", "themselves", "then", "there", "there's",
    "these", "they", "they'd", "they'll", "re", "they've", "this", "those",
    "through", "to", "too", "under", "until", "up", "very", "was", "wasn't",
    "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what",
    "what's", "when", "when's", "where", "where's", "which", "while", "who",
    "who's", "whom", "why", "why's", "with", "won't", "would",
    "wouldn't", "you", "you'd", "you'll", "you're", "you've", "your", "yours",
    "yourself", "yourselves", 

    // Spanish (puse estas stopwords pero no hace falta en si ya que los subreddits estan en ingles
    "el", "la", "los", "las", "un", "una", "unos", "unas", "y", "e", "o", "u",
    "pero", "para", "por", "si", "no", "con", "de", "del", "al", "lo"
  )

  def countw(posts: List[Post]): Map[String, Int] = {
    //Paso 1: Identifico las palabras
    //Paso 1.a: Obtengo el texto (selftext)
    val textos = posts.map(e => e._3)
    //Paso 1.b: Separo en palabras ignorando espacios
    val words = textos.flatMap(t => t.replaceAll("[^a-zA-Z]", " ").split(" "))

    //Paso 2: Filtro que palabras no sean vacias, empiecen con mayuscula y no sean stopwords
    val wordsFiltered = words.filter(p => p.nonEmpty && p.head.isUpper && !stopwords.contains(p.toLowerCase))

    //Paso 3: Cuento el numero de ocurrencias de cada palabra
    //Paso 3.a: Agrupo las palabras iguales
    val wordsGrouped = wordsFiltered.groupBy(p => p)
    //Paso 3.b: Cuento las palabras agrupadas
    val wordsCount = wordsGrouped.map {case (p, o) => (p, o.length)}

    wordsCount
  }
}