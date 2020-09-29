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
          |TITLE TEXT NOT NULL,
          |GENRE TEXT NOT NULL,
          |STUDIO TEXT NOT NULL,
          |RATING INTEGER NOT NULL,
          |YEAR INTEGER NOT NULL;
          |""".stripMargin

    val statement = conn.createStatement()
    val resultSet = statement.execute(sql)

    /**
     *
     */
    val insertSql =
        """
          |INSERT INTO contacts (
          |first_name, last_name, email, phone)
          |VALUES(?,?,?,?)
          |""".stripMargin

    val preparedStatement = conn.prepareStatement(insertSql)



}


