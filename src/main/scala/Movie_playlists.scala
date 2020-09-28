import java.io.FileWriter
import scala.io.StdIn.readLine

object Movie_playlists extends App{

// Getting the text file from the User Directory
  val userDir = System.getProperty("user.dir") // UserDirectory makes the code more universal
  val srcName = s"$userDir/movieDB.txt"
  val dstName = s"$userDir/DesiredPlaylist.txt"

  def openSource(fName:String) = {
    val filePointer = scala.io.Source.fromFile(srcName)
    val myLines = filePointer.getLines.toSeq
    myLines
  }

//TODO to get rid of duplicate (Gnomeo and Juliet)

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
  var userGenre = readLine ("(R)omance, (C)omedy, (D)rama, (A)nimation? or (Ac)tion? ")

  //Filters to Playlists
  def getTop5byAudScore: Seq[MovieClass] = { //FIXME or TODO to create prettyPrint to get rid of the text "MovieClass" before the results?
    val rawSplit = getParsedLines(srcName)
    val filteredResults = rawSplit.filter(_.size == 8) // gets lines with split size 8
    val ourMovies = getMovieClassSeq(filteredResults.slice(1, filteredResults.size)) // maps MovieClass with each split element
    val sortByAudScore = ourMovies.sortBy(_.audience_score).reverse.filter(_.genre.startsWith(userGenre)).slice(0, 5)
    sortByAudScore
  }

  def getTop5RotTom: Seq[MovieClass] = {
    val rawSplit = getParsedLines(srcName)
    val filteredResults = rawSplit.filter(_.size == 8) // gets lines with split size 8
    val ourMovies = getMovieClassSeq(filteredResults.slice(1, filteredResults.size)) // connects MovieClass with each split element
    val sortByTomScore = ourMovies.sortBy(_.rotten_tomatoes_score).reverse.filter(_.genre.startsWith(userGenre)).slice(0, 5)
    sortByTomScore
  }

  val top5Audience = "TOP 5 MOVIES BY AUDIENCE SCORE: \r\n"
  val top5Tomatoes = "TOP 5 MOVIES FROM ROTTEN TOMATOES \r\n"

  // Saving yielded data
  def saveSeq(destName:String, mySeq:Seq[MovieClass], mySeq2:Seq[MovieClass]) = {
    if (userGenre == "Ac") {
      println(s"Sorry we have only one action movie. Its details are saved: $destName")
    } else
      println(s"Nice choice! Your TOP5 movie list is saved: $destName")
    val fw = new FileWriter(destName)
    fw.write(s"$top5Audience")//gets new section/playlist
    mySeq.map(_ + "\r\n").foreach(fw.write)
    fw.write(s"\r\n $top5Tomatoes")
    mySeq2.map(_ + "\r\n").foreach(fw.write)
    fw.close()
  }

  val mySeq = openSource(srcName)
  saveSeq(dstName,getTop5byAudScore,getTop5RotTom)

  //TODO how to get rid of the text "MovieClass" before the results?
  //TODO How to print only Title, Genre, Studio, Both Scores and Year? Otherwise the results look kinda ugly.
  //This actually might be already integrated in the MovieClass PrettyPrint

}
//TODO create db and save the results there

