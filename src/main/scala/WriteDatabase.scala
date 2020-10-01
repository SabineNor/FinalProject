import java.sql.DriverManager

// inserts rows in the database

object WriteDatabase {
  def writeDatabase(Playlist: Seq[MovieClass], dname: String): Unit = {
    val insertSql =
      """
        |INSERT INTO playlistTable (
        |title, genre, lead_studio, audience_score, profitability, rotten_tomatoes_score, worldwide_gross, year)
        |VALUES(?,?,?,?,?,?,?,?)
        |""".stripMargin
    val environmentVars = System.getenv()
    val sqlite_home = environmentVars.get("SQLITE_HOME").replace("\\", "/")
    val dbname = dname
    val url = s"jdbc:sqlite:$sqlite_home/db/$dbname"

    val conn = DriverManager.getConnection(url)


    val preparedStatement = conn.prepareStatement(insertSql)
    for (p <- Playlist) {
      preparedStatement.setString(1, p.title)
      preparedStatement.setString(2, p.genre)
      preparedStatement.setString(3, p.lead_studio)
      preparedStatement.setInt(4, p.audience_score)
      preparedStatement.setString(5, p.profitability)
      preparedStatement.setInt(6, p.rotten_tomatoes_score)
      preparedStatement.setString(7, p.worldwide_gross)
      preparedStatement.setInt(8, p.year)
      preparedStatement.addBatch
    }
    preparedStatement.executeBatch()
    preparedStatement.close()

  }
}