import java.sql.DriverManager

// creates a database out of the Top5 choices

object NewDatabase {
    def createNewDatabase() = {
        val environmentVars = System.getenv()
        val sqlite_home = environmentVars.get("SQLITE_HOME").replace("\\", "/")
        val dbname = "playlist.db"
        val url = s"jdbc:sqlite:$sqlite_home/db/$dbname"
   
        val conn = DriverManager.getConnection(url)
        val sql =
            """
            |CREATE TABLE IF NOT EXISTS playlistTable (
            |title TEXT NOT NULL,
            |genre TEXT NOT NULL,
            |lead_studio TEXT NOT NULL,
            |audience_score INTEGER NOT NULL,
            |profitability TEXt NOT NULL,
            |rotten_tomatoes_score INTEGER NOT NULL,
            |worldwide_gross TEXT NOT NULL,
            |year INTEGER NOT NULL);
            |""".stripMargin

            val statement = conn.createStatement()
            val resultSet = statement.execute(sql)

}
}

