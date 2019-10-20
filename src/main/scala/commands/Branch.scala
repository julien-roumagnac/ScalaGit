package commands

import java.io.File
import tools.{FileManager, SgitManager}
object Branch {


  def add(repo:String,branchName : String , refLastCommit : String): Unit = {

    val fm = new FileManager(repo)
    val branch = fm.createFile(s"${repo}.sgit${File.separator}refs${File.separator}heads${File.separator}"+branchName+".txt")

    branch.exists() match {
      case true => FileManager.IOprint("branch already existing")
      case _ =>
        FileManager.IOprint(s"${branchName} added with succes on commit : ${refLastCommit}")
        fm.writeFile(s".sgit${File.separator}refs${File.separator}heads${File.separator}"+branchName+".txt",refLastCommit)
    }


  }
  def getCurrent(fm :FileManager): String = {
    fm.readAllFile(s".sgit${File.separator}HEAD.txt")  match {
      case None => ""
      case _ => s".sgit${File.separator}"+fm.readAllFile(s".sgit${File.separator}HEAD.txt").get
    }
  }

  def updateLastCommit(fm:FileManager,currentBranch : String,hash:String): Unit = {
    fm.writeFile(currentBranch,hash)
  }
}
