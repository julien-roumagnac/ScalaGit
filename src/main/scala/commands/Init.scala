package commands

import java.io.File
import tools.{FileManager}
object Init {


  def init(): Unit ={




    val f = new File(".sgit")
    val exists = f.exists() && f.isDirectory




    exists match {
      case false => {
        val fm:FileManager = new FileManager("")


        fm.createDir(s".sgit${File.separator}objects${File.separator}blob")
        fm.createDir(s".sgit${File.separator}objects${File.separator}commit")
        fm.writeFile(s".sgit${File.separator}stage.txt","")
        fm.createDir(s".sgit${File.separator}refs${File.separator}heads")
        fm.createDir(s".sgit${File.separator}refs${File.separator}tags")
        fm.writeFile(s".sgit${File.separator}HEAD.txt", s"refs${File.separator}heads${File.separator}master.txt")
        fm.writeFile(s".sgit${File.separator}refs${File.separator}heads${File.separator}master.txt", "first")
        fm.writeFile(s".sgit${File.separator}objects${File.separator}commit${File.separator}first.txt" , "first")
        FileManager.IOprint(" A new sgit repository has been initialized with success.")
      }
      case _ => FileManager.IOprint("An sgit repository has already been initialized here.")
    }


  }

}
