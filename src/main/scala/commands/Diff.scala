package commands

import java.io.File

import tools.{FileManager, Hash, SgitManager}

import scala.util.Properties

object Diff {


  def doDiff(text1 : List[String] , text2 : List[String] , res : List[(String, String)]): List[(String,String)] = {

    if(text1.isEmpty && text2.isEmpty) {
       res
    }else if(text1.isEmpty ) {
         doDiff(text1,text2.tail,res ++ List(("++",text2.head)))
    }else if(text2.isEmpty ) {
       doDiff(text1.tail,text2,res ++ List(("--",text1.head)))
    }else if(text2.head == text1.head ) {
       doDiff(text1.tail,text2.tail,res ++ List(("  ",text1.head)))
    }else {
      val add =  doDiff(text1,text2.tail,res ++ List(("++",text2.head)))
      val supp = doDiff(text1.tail,text2,res ++ List(("--",text1.head)))
      add.size > supp.size match {
        case true =>  supp
        case _ =>  add
      }

    }




  }
  def printDiff(diff : List[(String,String)]): Unit = {


    diff.map(f => {
      f._1 match {
        case  "++"=> FileManager.IOprint( Console.GREEN + f._1 +  " " + f._2 + Console.RESET )
        case "--" => FileManager.IOprint( Console.RED + f._1 +  " " + f._2 + Console.RESET )
        case _ => FileManager.IOprint ("   " + f._2)
      }

    })

  }

  def diff(repo : String): Unit ={
    val fm = new FileManager(repo)
    val stage = getStageLines(fm.readAllFile(s".sgit${File.separator}stage.txt"))

    if(stage.size > 0){
    stage.map(l => {


      val hash = l.apply(1)
      val filePath = l.apply(0)
      FileManager.IOprint("Diff of "+filePath+" HASH "+hash+" ")
      val f = new File(repo+ filePath)
      f.exists() match {
        case false => printDiff(doDiff(fm.readAllFile(s".sgit${File.separator}objects${File.separator}blob${File.separator}"+hash).get.split(Properties.lineSeparator).toList,List(),List()))
        case true => {
          val oldBlob = Hash.sha_one(filePath+ fm.readAllFile(filePath).get)

          oldBlob == hash match {
            case true => FileManager.IOprint("No change")
            case _ => printDiff(
              doDiff(
                fm.readAllFile(s".sgit${File.separator}objects${File.separator}blob${File.separator}"+hash).get.split(Properties.lineSeparator).toList,
                fm.readAllFile(filePath).get.split(Properties.lineSeparator).toList,
                List()))
          }
        }
      }
    })}
  }
  def getStageLines(stage: Option[String]): List[List[String]] = {
    val lines = SgitManager.getBlobFromStage(stage)

    val separateLines =lines.map(s => s.split(" ").toList)
    separateLines


  }


}
