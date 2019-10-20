package tools

import java.io.File

import commands.Branch
import better.files.{File => BFile}
import scala.annotation.tailrec
import scala.util.Properties

object SgitManager {

  def isSgitInit(): Boolean ={
    @tailrec
    def isInitTail(path: String): Boolean ={

      val f =  BFile(s"$path${File.separator}.sgit").exists

      f match {
        case true => return true
        case _ => {
          BFile(path).parent != null match {
            case true => isInitTail(BFile(path).parent.pathAsString)
            case false => false

          }

        }
      }

    }
    val userdir = System.getProperty("user.dir")
    isInitTail(userdir)
  }

  def getCurrentCommit(): String ={
    val fm = new FileManager(getPathRepo())
    val currentBranch = Branch.getCurrent(fm)
    val refLastCommit = fm.readAllFile(currentBranch).getOrElse("nofather")
    refLastCommit
  }
  def getPathRepo(): String = {

    @tailrec
    def getRepoTail(path: String): String ={

      val f =  BFile(s"$path${File.separator}.sgit").exists

      f match {
        case true => path + File.separator
        case _ => getRepoTail(BFile(path).parent.pathAsString)
      }

    }
    val userdir = System.getProperty("user.dir")
    getRepoTail(userdir)

  }

  def relativize(absolutepath: String,repo:String): String = {

    val repoToList = repo.split(File.separator)
    val pathToList = absolutepath.split(File.separator)

    val relatif = (pathToList.toList.diff(repoToList.toList)).mkString(File.separator)


    relatif
  }
  def getWDToLines(repo : String): List[String] = {
    val fm = new FileManager(repo)
    val ignore = List(".sgit","project","src","target",".git",".idea","sgit","sgit.sh","installation.sh","build.sbt").map(f=> repo+f)
    val f =  new File(repo).listFiles().filter(p => {

      !ignore.contains(p.getAbsolutePath())}).toList


    val f2 = f.flatMap(f3 => f3.isDirectory match {
      case true => fm.getChilds(f3)
      case _ => List(f3)
    }).filter(d=> !d.isDirectory)




    val res = f2.map(f=>{
      val path = SgitManager.relativize(f.getAbsolutePath,repo)

      path + " " + Hash.sha_one(path+fm.readAllFile(path).get)
    })


    res
  }



    def getBlobsFromCommit(hash:String,repo:String): List[String] ={
      val fm = new FileManager(repo)
      val commit = fm.readAllFile(s".sgit${File.separator}objects${File.separator}commit${File.separator}"+hash+".txt").getOrElse("first").split(Properties.lineSeparator)
      commit.size match {
        case 1=> List()
        case _ => commit.tail.tail.toList
      }

    }
  def getBlobFromStage(stage:Option[String]): List[String] ={

    stage match {
      case None =>List()
      case _ =>
      {stage.get match {
        case ""=> List()
        case _ =>   val blobs = stage.get.split(Properties.lineSeparator)
          blobs.toList
      }
      }

    }
  }
}
