import java.io.FileWriter
import scala.io.StdIn.readLine

// Data source for movieDB: https://gist.github.com/tiangechen/b68782efa49a16edaf07dc2cdaa855ea

object Movie_playlists extends App{

// Getting the text file from the User Directory

  val userDir = System.getProperty("user.dir")
  val srcName = s"$userDir/movieDB.txt"
  val dstName = s"$userDir/DesiredPlaylist.txt"

  def openSource(fName:String) = {
    val filePointer = scala.io.Source.fromFile(srcName)
    val myLines = filePointer.getLines.toSeq
    myLines
  }

  //Counts lines in the file
  def getLineCount(fileName: String): Int = {
  var count = 0
  val bufferedSource = io.Source.fromFile(fileName)
  for (line <- bufferedSource.getLines) {
    count += 1
  }
    bufferedSource.close
    count
  }

  // Splits objects in line which are separated by comma
 def getParsedLines(fileName:String) = {
   var myListBuf = scala.collection.mutable.ListBuffer[Seq[String]]()
   val bufferedSource = io.Source.fromFile(fileName)
   for (line <- bufferedSource.getLines){
     val splitLine = line.split (",")
     myListBuf += splitLine
   }
     bufferedSource.close
   myListBuf.toSeq
 }

  //Checks how many splits are in one line
  def getLineSplits(fileName:String): Seq[Int] = {
    var myListBuf = scala.collection.mutable.ListBuffer[Int]()
    val bufferedSource = io.Source.fromFile(fileName)
    for (line <- bufferedSource.getLines) {
      val splitLine = line.split(",")
      myListBuf += splitLine.size
    }
    bufferedSource.close
    myListBuf.toSeq
  }

  // To map our movie sequence with the MovieClass
  def getMovieClassSeq(splitLineSeq: Seq[Seq[String]]): Seq[MovieClass] = {
    splitLineSeq.map(t => MovieClass(t.head,t(1),t(2),t(3).toInt,t(4),t(5).toInt,t(6),t(7).toInt))
  }

  //Printing results in IntellJ - for Data validation and User Input
    val lineCount = getLineCount(srcName)
    println(s"We got $lineCount movies in our list.")
    val myLineSplits = getLineSplits(srcName)
    val dataValid = if (myLineSplits.min == myLineSplits.max)
      println(s"We checked that there are no missing data in our movie list. " +
        s"It is ok to proceed with your movie choice!\n")
    else println("Some lines might be missing some data. Please check!")

  //Code for the User's Input
  val userName = readLine("What is your name? ")
  println(s"$userName, what kind of movie genre do you prefer:")
  var userGenre = readLine ("Romance, Comedy, Drama, Animation? or Action? ")

  //Filters to Playlists
  def getTop5byAudScore: Seq[MovieClass] = {
    val rawSplit = getParsedLines(srcName)
    val filteredResults = rawSplit.filter(_.size == 8) // gets lines with split size 8
    val ourMovies = getMovieClassSeq(filteredResults.slice(1, filteredResults.size)) // maps MovieClass with each split element
    val sortByAudScore = ourMovies.sortBy(_.audience_score).reverse.filter(_.genre == userGenre).slice(0, 5).distinct
    sortByAudScore
  }

  def getTop5RotTom: Seq[MovieClass] = {
    val rawSplit = getParsedLines(srcName)
    // gets lines with split size 8
    val filteredResults = rawSplit.filter(_.size == 8)
    // connects MovieClass with each split element
    val ourMovies = getMovieClassSeq(filteredResults.slice(1, filteredResults.size))
    val sortByTomScore = ourMovies.sortBy(_.rotten_tomatoes_score).reverse.filter(_.genre == userGenre).slice(0, 5).distinct
    sortByTomScore
  }

  val top5a = getTop5byAudScore
  val top5t = getTop5RotTom
  val tops = top5a ++ top5t
  val top5Audience = "TOP 5 MOVIES BY AUDIENCE SCORE: \r\n"
  val top5Tomatoes = "TOP 5 MOVIES FROM ROTTEN TOMATOES \r\n"
  //counts how much results are per TOP section
  val destLineCount = (getLineCount(dstName)-2)/2

  // Saving yielded data
  def saveSeq(destName:String, mySeq:Seq[String], mySeq2:Seq[String]) = {
    if (destLineCount <= 1) {
      println(s"Nice choice, but we have only $destLineCount $userGenre movie. Its details are saved: $destName")
    } else if (destLineCount <5) {
      println(s"\r\nNice choice, but we have only $destLineCount $userGenre movies in our list." +
              s"Your TOP$destLineCount  movie list is saved: $destName")
    } else println(s"\r\nNice choice! Your TOP$destLineCount movie list is saved: $destName")
//
    val fw = new FileWriter(destName)
    fw.write(s"$top5Audience")//gets new section/playlist
    mySeq.map(_ + "\r\n").foreach(fw.write)
    fw.write(s"\r\n$top5Tomatoes")
    mySeq2.map(_ + "\r\n").foreach(fw.write)
    fw.close()
  }

// creates a more appealing look
  def prettyPrint(top: MovieClass): String = {
    s"Title: ${top.title} - Genre: ${top.genre} - Studio: ${top.lead_studio} " +
      s"- Audience score: ${top.audience_score} - Rotten score: ${top.rotten_tomatoes_score} - Year: ${top.year}"
  }

  val mySeq = openSource(srcName)
  val top5aClean = top5a.map(prettyPrint(_))
  val top5tClean = top5t.map(prettyPrint(_))
  saveSeq(dstName,top5aClean,top5tClean)

  //Database source material + creates a database + inserts the rows
  val playlistDB = NewDatabase.createNewDatabase()
  val writing = WriteDatabase.writeDatabase(tops, "playlist.db")

}



