import java.sql.DriverManager

object NewDatabase extends App {
    val environmentVars = System.getenv()
    environmentVars.forEach((k, v) => println(k, v))

    val sqlite_home = environmentVars.get("SQLITE_HOME").replace("\\", "/")
    val dbname = "playlist.db"
    val url = s"jdbc:sqlite:$sqlite_home/db/$dbname"
   
    val conn = DriverManager.getConnection(url)
    val sql =
        """
          |CREATE TABLE IF NOT EXISTS Playlist (
          |title TEXT NOT NULL,
          |genre TEXT NOT NULL,
          |lead_studio TEXT NOT NULL,
          |audience_score INTEGER NOT NULL,
          |year INTEGER NOT NULL;
          |""".stripMargin

    val statement = conn.createStatement()
    val resultSet = statement.execute(sql)

    /**
     *
     */
    val insertSql =
        """
          |INSERT INTO Playlist (
          |title, genre, lead_studio, audience_score, year)
          |VALUES(?,?,?,?)
          |""".stripMargin

    val preparedStatement = conn.prepareStatement(insertSql)
   ....foreach(...(preparedStatement, _))//TODO create the writer
    preparedStatement.executeBatch()

    preparedStatement.close()



}


