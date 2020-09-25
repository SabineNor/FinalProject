import java.io.FileWriter

object Movie_playlists extends App{
// Getting the text from the source
  val srcName = "c:/temp/movieDB.txt"
  val dstName = "c:/temp/DesiredPlaylist.txt"
  def openSource(fName:String) = {
    val filePointer = scala.io.Source.fromFile(srcName)
    val myLines = filePointer.getLines.toSeq
    myLines
  }
  // Filtering information for the playlist
  def processSeq(mySeq:Seq[String])= {
    //TODO filter the playlist
    }

// Saving yielded data
  def saveSeq(destName:String, mySeq:Seq[String]) = {
    println(s"Saving my Sequence to file $destName")
    val fw = new FileWriter(destName)
    mySeq.map(_ + "\n").foreach(fw.write)
    fw.close()
  }

  val mySeq = openSource(srcName)
  val filteredSeq = processSeq(mySeq)
  saveSeq(dstName,filteredSeq)
}


