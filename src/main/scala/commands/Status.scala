package commands

import scala.util.Properties
import tools.{FileManager}
case class Status(){



}
object Status {

  def status(WD:List[String],stage : List[String], lastCommit : List[String]): Unit ={

    val status = getStatusGroups(WD,stage,lastCommit)

    printStatus(status)

  }
  def getStatusGroups(WD:List[String],stage : List[String], lastCommit : List[String]): (List[String],List[String],List[String],List[String]) ={
    val WDpaths = WD.map(f => f.split(" ").apply(0))
    val stagePaths = stage.map(f => f.split(" ").apply(0))
    val commitPaths = lastCommit.map(f => f.split(" ").apply(0))
    val intersectionStageWD = WD.filter(f=> stage.contains(f)).map(f=> f.split(" ").apply(0))
    val intersectionStageCommit = stage.filter(f=> lastCommit.contains(f)).map(f=> f.split(" ").apply(0))

    val untrackedPaths = WDpaths.filter(s=> !stagePaths.contains(s))
    val changesNotAdded = ((WDpaths.diff(untrackedPaths)).diff(intersectionStageWD))
    val newFiles = (stagePaths.filter(s => !commitPaths.contains(s)))
    val changesAdded = ((stagePaths.diff(newFiles)).diff(intersectionStageCommit))

    (untrackedPaths.map(s=> "untracked : "+ s),changesNotAdded.map(s=>"changed : "+s),changesAdded.map(s=>"changed : "+s),newFiles.map(s=>"new : "+s))

  }
  def  printStatus(status: (List[String],List[String],List[String],List[String])): Unit ={
    val(untracked,changesNotAdded,changesAdded,newFiles) = status
    FileManager.IOprint("Changes that will be validated")
    FileManager.IOprint(Console.GREEN +  (newFiles++changesAdded).mkString(Properties.lineSeparator) + Console.RESET)

    FileManager.IOprint("Changes that will not be validated ")
    FileManager.IOprint(Console.RED +  changesNotAdded.mkString(Properties.lineSeparator) + Console.RESET)

    FileManager.IOprint("Untracked files")
    FileManager.IOprint(Console.RED +  untracked.mkString(Properties.lineSeparator) + Console.RESET)

  }
}