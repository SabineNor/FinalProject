import java.io.FileWriter

object Movie_playlists extends App{

// Getting the text file from the User Directory
  val userDir = System.getProperty("user.dir") // I used UserDirectory, so that the code is more universal
  val srcName = s"$userDir/movieDB.txt" // TODO please check if this works for you, if ok, please delete the commented lines in this section
//  val srcName = "c:/temp/movieDB.txt"
//  val dstName = "c:/temp/DesiredPlaylist.txt"
  val dstName = s"$userDir/DesiredPlaylist.txt"

  def openSource(fName:String) = {
    val filePointer = scala.io.Source.fromFile(srcName)
    val myLines = filePointer.getLines.toSeq
    myLines
  }

//FIXME to get rid of duplicates (Gnomeo and Juliet)
  //FIXME to get rid of "$" sign and change worldwide gross type to Double
  //FIXME to get rid of showing "MovieClass" before the results

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

  //Printing results in IntellJ - Data validation
  val lineCount = getLineCount(srcName)
  println(s"We got $lineCount lines in our file.")
  val myLineSplits = getLineSplits(srcName)
  val dataValid = if (myLineSplits.min == myLineSplits.max)
    println("There are no missing data in lines. It is ok to proceed!")
    else println("Some lines might be missing some data. Please check!")


  //Filters to Playlists
  def getTop5byAudScore: Seq[MovieClass] = { //FIXME how to get rid of the text "MovieClass" before the results?
    val rawSplit = getParsedLines(srcName)
    val filteredResults = rawSplit.filter(_.size == 8) // gets lines with split size 8
    val ourMovies = getMovieClassSeq(filteredResults.slice(1, filteredResults.size)) // connects MovieClass with each split element
    val sortByAudScore = ourMovies.sortBy(_.audience_score).reverse.slice(0, 5)
    sortByAudScore
  }

  def getTop5RotTom: Seq[MovieClass] = { //FIXME how to get rid of the text "MovieClass" before the results?
    val rawSplit = getParsedLines(srcName)
    val filteredResults = rawSplit.filter(_.size == 8) // gets lines with split size 8
    val ourMovies = getMovieClassSeq(filteredResults.slice(1, filteredResults.size)) // connects MovieClass with each split element
    val sortByTomScore = ourMovies.sortBy(_.rotten_tomatoes_score).reverse.slice(0, 5)
    sortByTomScore
  }

  def getTop5RotTomComedy: Seq[MovieClass] = {
    val rawSplit = getParsedLines(srcName)
    val filteredResults = rawSplit.filter(_.size == 8) // gets lines with split size 8
    val ourMovies = getMovieClassSeq(filteredResults.slice(1, filteredResults.size)) // connects MovieClass with each split element
    val sortByTomScoreComedy = ourMovies.sortBy(_.rotten_tomatoes_score).reverse.filter(_.genre=="Comedy")slice (0, 5)
    sortByTomScoreComedy
  }

  val top5Audience = "TOP 5 MOVIES BY AUDIENCE: \r\n"
  val top5Tomatoes = "TOP 5 MOVIES FROM ROTTEN TOMATOES \r\n"
  val top5TomComedies = "TOP 5 COMEDIES FROM ROTTEN TOMATOES \r\n"


  // Saving yielded data
  def saveSeq(destName:String, mySeq:Seq[MovieClass], mySeq2:Seq[MovieClass],mySeq3:Seq[MovieClass]) = {
    println(s"Saving my Sequence to file $destName")
    val fw = new FileWriter(destName)
    fw.write(s"$top5Audience")//gets new section/playlist
    mySeq.map(_ + "\r\n").foreach(fw.write)
    fw.write(s"\r\n $top5Tomatoes")
    mySeq2.map(_ + "\r\n").foreach(fw.write)
    fw.write(s"\r\n $top5TomComedies")
    mySeq3.map(_ + "\r\n").foreach(fw.write)
    fw.close()
  }

  val mySeq = openSource(srcName)
  saveSeq(dstName,getTop5byAudScore,getTop5RotTom,getTop5RotTomComedy)
  //FIXME How to print only the titles and the criteria? Otherwise the results look kinda ugly
}
//TODO create db and save the results there

