package commands

import java.io.File

import scala.util.Properties
import tools.{FileManager}
object Log {

  def log(repo : String,hashNewestCommit : String) : Unit ={
    val fm = new FileManager(repo)
    val currentBranch = fm.readAllFile(s".sgit${File.separator}HEAD.txt")  match {
      case None => ""
      case _ => s".sgit${File.separator}"+fm.readAllFile(s".sgit${File.separator}HEAD.txt").get
    }
    //val hashNewestCommit = fm.readAllFile(currentBranch).getOrElse("first")
    printLog(hashNewestCommit,repo)

  }
  def printLog(hash:String,repo: String): Unit = {
    val fm = new FileManager(repo)
    hash match {
      case "first" => FileManager.IOprint("END")
      case _ =>
      {
        val commit = fm.readAllFile(s".sgit${File.separator}objects${File.separator}commit${File.separator}"+hash+".txt").getOrElse("first").split(Properties.lineSeparator)
        val father = commit.apply(0)
        val date = commit.apply(1)
        FileManager.IOprint(Console.MAGENTA + "Commit  : " + hash + Console.RESET)
        FileManager.IOprint(" date   : "+ date )
        FileManager.IOprint( " father : " +father +Properties.lineSeparator)
        printLog(father,repo)
      }
    }

  }

}
