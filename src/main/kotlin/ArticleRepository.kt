class ArticleRepository {
    private val articles = mutableListOf<Article>()
    private var lastId = 0

    fun deleteArticle(article: Article) {
        articles.remove(article)
    }

    fun getArticleById(id: Int): Article? {
        for (article in articles) {
            if (article.id == id) {
                return article
            }
        }

        return null
    }

    fun addArticle(boardId: Int, memberId: Int, title: String, body: String): Int {
        val id = ++lastId
        val regDate = Util.getNowDateStr()
        val updateDate = Util.getNowDateStr()

        articles.add(Article(id, regDate, updateDate, boardId, memberId, title, body))

        return id
    }

    fun makeTestArticles() {
        for (id in 1..20) {
            addArticle(id % 2 + 1, id % 9 + 1, "제목_$id", "내용_$id")
        }
    }

    fun modifyArticle(id: Int, title: String, body: String) {
        val article = getArticleById(id)!!

        article.title = title
        article.body = body
        article.updateDate = Util.getNowDateStr()
    }

    fun getFilteredArticles(
        boardCode: String,
        searchKeyword: String,
        page: Int,
        itemsCountInAPage: Int
    ): List<Article> {
        val filtered1Articles = getSearchKeywordFilteredArticles(articles, boardCode, searchKeyword)
        val filtered2Articles = getPageFilteredArticles(filtered1Articles, page, itemsCountInAPage)

        return filtered2Articles
    }

    private fun getSearchKeywordFilteredArticles(
        articles: List<Article>,
        boardCode: String,
        searchKeyword: String
    ): List<Article> {
        val filteredArticles = mutableListOf<Article>()

        val boardId = if (boardCode.isEmpty()) {
            0
        } else {
            boardRepository.getBoardByCode(boardCode)!!.id
        }

        for (article in articles) {
            var needToAdd = true

            if (boardId != 0) {
                if (article.boardId != boardId) {
                    needToAdd = false
                }
            }

            if (needToAdd && searchKeyword.isNotEmpty()) {
                if (!article.title.contains(searchKeyword)) {
                    needToAdd = false
                }
            }

            if (needToAdd) {
                filteredArticles.add(article)
            }
        }

        return filteredArticles
    }

    private fun getPageFilteredArticles(articles: List<Article>, page: Int, itemsCountInAPage: Int): List<Article> {
        val filteredArticles = mutableListOf<Article>()

        val offsetCount = (page - 1) * itemsCountInAPage

        val startIndex = articles.lastIndex - offsetCount
        var endIndex = startIndex - (itemsCountInAPage - 1)

        if (endIndex < 0) {
            endIndex = 0
        }

        for (i in startIndex downTo endIndex) {
            filteredArticles.add(articles[i])
        }

        return filteredArticles
    }
}