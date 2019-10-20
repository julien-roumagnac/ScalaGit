package commands

import java.io.File
import tools.{FileManager}
object Tag {

  def add(repo:String,tagName : String,refLastCommit:String): Unit = {
    val fm = new FileManager(repo)


    val tag = new File(s"${repo}.sgit${File.separator}refs${File.separator}tags${File.separator}"+tagName+".txt")

    tag.exists() match {
      case true => FileManager.IOprint("tag already existing")
      case _ =>
        FileManager.IOprint(s"${tagName} added with succes on commit : ${refLastCommit}")
        fm.writeFile(s".sgit${File.separator}refs${File.separator}tags${File.separator}"+tagName+".txt",refLastCommit)
    }


  }


}
