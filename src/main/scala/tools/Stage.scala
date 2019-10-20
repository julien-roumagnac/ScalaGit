package tools

import java.io.File

import scala.util.Properties

object Stage {

  def getFilesPath(): Option[Array[String]]={
    val fm  = new FileManager(SgitManager.getPathRepo())
    val stage : Option[String] = fm.readFileByLine(s".sgit${File.separator}stage.txt")
    stage match {
      case None =>   None
      case _ => {
        val paths = stage.get.split(" ")
        Some(paths)
      }
    }

  }

  def addLine(line : String,stage : Option[Array[String]] ) : Array[String] = {



    //get file path
    val path = line.split(" ").apply(0)

    // I go through the stage and if a line contains my path it is because it is the one I want to change so I replace if not I return the old one
    val newStage = stage.get.map(f  =>  {
      if(f.split(" ").contains(path)) line
      else f
    }
    )
    newStage
  }
  def updateStage(lines : Array[String]): Unit ={
    val concatLines = lines.mkString(Properties.lineSeparator)
    val fm = new FileManager(SgitManager.getPathRepo())
    fm.writeFile(s".sgit${File.separator}stage.txt", concatLines)
  }

  def isBloborPathInStage(hash : String,stage:Option[Array[String]]): Boolean ={

    stage.get.filter(s => s.contains(hash)).length > 0




  }
  override def toString(): String = {

    val fm  = new FileManager(SgitManager.getPathRepo())
    val stage : Option[String] = fm.readAllFile(s".sgit${File.separator}stage.txt")
    val paths = stage.get
    paths



  }
  def toList( ): Option[Array[String]] ={
    val fm  = new FileManager(SgitManager.getPathRepo())
    val stage : Option[String] = fm.readAllFile(s".sgit${File.separator}stage.txt")
    stage match {
      case None => None
      case _ => { val paths = stage.get.split(Properties.lineSeparator)

         Some(paths)
      }
    }
    }

}
