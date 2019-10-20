package tools

import java.io.{PrintWriter, _}

import scala.util.Properties



class FileManager(globalPath:String = ""){
  import FileManager._

def createFile(path:String): File ={
  new File(path)
}
  def getChilds(d: File): List[File] ={
    if (d.exists && d.isDirectory) {
      val these = d.listFiles.toList
      these ++ these.filter(_.isDirectory).flatMap(d=> getChilds(d))
    } else {
      List[File]()
    }
  }
  def delChilds(d: File): Boolean ={
    if (d.exists && d.isDirectory) {
      val these = d.listFiles.toList
      these.map(f=> delChilds(f))
      true
    } else {
      d.delete()
    }
  }


  // With lineSeparator
  def readAllFile(path:String) : Option[String]= {
    try
      {
        val lines = scala.io.Source.fromFile(globalPath+path).mkString
        Some(lines)

      }
    catch
      {
        case _: Throwable => return None
      }
  }

  def readFileByLine(path:String): Option[String] = {
    try
      {
        val br = new BufferedReader(new FileReader(globalPath + path))
        Some(readFileRecByLine(br))
      }
    catch
      {
        case _: Throwable => return None
      }
  }


  def createDir(path:String): Boolean = new File(globalPath + path).mkdirs()

  def addLine(path:String,text : String): Boolean  = {
    try
      {




        val write = new PrintWriter(new FileOutputStream(new File(globalPath+path),true))
        write.write(text + Properties.lineSeparator)

        write.close()




        true
      }
    catch
      {
        case _: Throwable => return false
      }
  }

  def writeFile(path:String, text: String): Boolean = {
    try
      {

        val HEAD = new PrintWriter(new File(globalPath + path))
        HEAD.write(text)
        HEAD.close()
        true
      }
    catch
      {
        case _: Throwable => return false
      }
  }
}

object FileManager {
  private def readFileRec(br:BufferedReader): String = {
    val st = br.readLine()
    if ( st!= null) st  + readFileRec(br)
    else ""
  }
  private def readFileRecByLine(br:BufferedReader): String = {
    val st = br.readLine()
    if ( st!= null) st  + " " +readFileRec(br)
    else ""
  }
  def IOprint(msg : String): Unit = {
    println(msg)
  }
}
