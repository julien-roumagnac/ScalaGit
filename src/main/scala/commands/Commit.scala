package commands

import java.io.File
import java.util.Calendar

import tools.{FileManager, Hash, SgitManager, Stage}

import scala.util.Properties

object Commit{

  def createCommit(repo:String,time:String,index:String,refLastCommit:String): (String,String) ={


    val hash = Hash.sha_one(index)
    val content = refLastCommit +Properties.lineSeparator + time + Properties.lineSeparator + index

    (hash,content)

  }
  def commit(repo:String,lastCommit : String): Unit ={
    val cal = Calendar.getInstance()
    val time = cal.getTime().toString()
    val index = Stage.toString()
    val commit = createCommit(repo,time,index,lastCommit)

    // is hash of commit new ?
     commit._1 == lastCommit match {
      case true => FileManager.IOprint("No modification from last commit")
      case _ =>
        val fm = new  FileManager(repo)
        fm.writeFile(s".sgit${File.separator}objects${File.separator}commit${File.separator}"+commit._1+ ".txt",commit._2)
        val currentbranch = Branch.getCurrent(fm)
        FileManager.IOprint((currentbranch.split(File.separator).reverse.apply(0).reverse).substring(4).reverse)

        Branch.updateLastCommit(fm,currentbranch,commit._1)

    }

  }

}
