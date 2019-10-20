package commands

import java.io.File

import tools.{FileManager, Hash, SgitManager, Stage}

object Add {


  def doAdd(repo: String,path : String): Unit = {
    val fm = new FileManager("")
    val file = fm.createFile(path)

    file.isDirectory match {

      case true => {
        //get all the files in the directory and subdirectory
        val allChilds = fm.getChilds(file)

        //addFile to the stage of all file in directory and subdirectories
        allChilds.map(f => {
          addFile(repo,SgitManager.relativize(f.getAbsolutePath,repo))})
      }
        // Its a file so addFile of this one
      case _ => addFile(repo,SgitManager.relativize(file.getAbsolutePath,repo))
    }

  }
  def addFile(repo:String , path : String): Unit = {

    val fm : FileManager = new FileManager(repo)

    // Content of the file
    val content : Option[String] = fm.readAllFile(path)

    // all lines of the stage

    val stageLines = Stage.toString()


    content match {
       //file dont exists
      case None => FileManager.IOprint("No file")

      //file exists
      case _ => {

        //hash from content + filename

        val blob = Hash.sha_one(path + content.get)


        // "filepath blob" is in the stage ?
        stageLines.contains(path + " " + blob) match {

          case true =>
          // blob not in stage so , two option => new file or modification of a file.
          case _ =>  {


            // is path of the file in the stage ?
            Stage.isBloborPathInStage(path,Stage.toList()) match {

              case true =>
                Stage.updateStage(Stage.addLine(path + " " + blob,Stage.toList()))
                fm.writeFile(s".sgit${File.separator}objects${File.separator}blob${File.separator}" + blob,content.get)

              case _ =>
                FileManager.IOprint(path)
                fm.writeFile(s".sgit${File.separator}objects${File.separator}blob${File.separator}" + blob,content.get)
                fm.addLine(s".sgit${File.separator}stage.txt", path + " " + blob)

            }

          }

        }

      }

    }





  }

}
