package handler

import java.io.File

import commands._
import scopt.OParser
import tools.{FileManager, SgitManager, Stage}


case class Config(
                   mode: String = "",
                   path: String = ".",
                   files: Seq[File] = Seq(),
                   patch: Boolean = false,
                   stats: Boolean = false,
                   givenName: String = "",
                   displayAll: Boolean = false,
                   verbose: Boolean = false
                 )

object Parser extends App {
  val builder = OParser.builder[Config]
  val parser1 = {
    import builder._
    OParser.sequence(
      programName("sgit"),
      head("sgit", "1.x"),
      help("help")
        .text("usage sgit "),
      cmd("init")
        .action((_, c) => c.copy(mode = "init"))
        .text(
          "creates a new sgit repository if not exists"
        )
        .children(
          arg[String]("<path>")
            .optional()
            .action((x, c) => c.copy(path = x))
        ),

      cmd("add")
        .action((_, c) => c.copy(mode = "add"))
        .text("adds the given files to the stage")
        .children(
          arg[File]("<file>...")
            .unbounded()
            .optional()
            .action((x, c) => c.copy(files = c.files :+ x))
            .text("file to add to stage")
        ),
      cmd("status")
        .action((_, c) => c.copy(mode = "status"))
        .text(
          "display the status of the repo (untracked, modified, staged files)"
        ),
      cmd("commit")
        .action((_, c) => c.copy(mode = "commit"))
        .text("create a new commit with staged files"),
      cmd("diff")
        .action((_, c) => c.copy(mode = "diff"))
        .text("display the delta of modified files with staged files"),
      cmd("log")
        .action((_, c) => c.copy(mode = "log"))
        .text("display commits information in a chronological orger")
        .children(
          opt[Unit]('p', "patch")
            .text("show the patch (diff) of each commited file ")
            .action((_, c) => c.copy(patch = true)),
          opt[Unit]("stats")
            .text(
              "show the stats of insertion and deletion of each commited file"
            )
            .action((_, c) => c.copy(stats = true))
        ),
      cmd("branch")
        .action((_, c) => c.copy(mode = "branch"))
        .text("create a new branch")
        .children(
          arg[String]("name")
            .required()
            .action((x, c) => c.copy(givenName = x))
            .text("name of the branch"),
          opt[Unit]('a', "all")
            .action((_, c) => c.copy(displayAll = true))
            .text("display all branches"),
          opt[Unit]('v', "verbose")
            .action((_, c) => c.copy(verbose = true))
            .text("show hash and commit subject line for each branch's head"),
          checkConfig(
            c =>
              if (c.displayAll && c.givenName != "")
                failure("'all' option does not make sense with a branch name")
              else if (c.verbose && c.givenName != "")
                failure(
                  "'verbose' option does not make sense with a branch name"
                )
              else success
          )
        ),
      cmd("tag")
        .action((_, c) => c.copy(mode = "tag"))
        .text("create a new tag")
        .children(
          arg[String]("name")
            .required()
            .action((x, c) => c.copy(givenName = x))
            .text("name of the tag"),
          opt[Unit]('a', "all")
            .action((_, c) => c.copy(displayAll = true))
            .text("display all branches"),
          opt[Unit]('v', "verbose")
            .action((_, c) => c.copy(verbose = true))
            .text("show hash and commit subject line for each branch's head"),
          checkConfig(
            c =>
              if (c.displayAll && c.givenName != "")
                failure("'all' option does not make sense with a tag name")
              else if (c.verbose && c.givenName != "")
                failure(
                  "'verbose' option does not make sense with a branch name"
                )
              else success
          )
        )

    )

  }

  // OParser.parse returns Option[handler.Config]
  OParser.parse(parser1, args, Config()) match {

    case Some(config) => {

      SgitManager.isSgitInit() match {
        case false =>

          config.mode match {
            case "init" => {
              Init.init()

            }
            case _ => {
              FileManager.IOprint("You have to create the sgit repository before anything else (with : sgit init)")
            }
          }

        case _ =>
          val repo = SgitManager.getPathRepo()

          config.mode match {
            case "init" => {
              FileManager.IOprint("Sgit init :")
              Init.init()

            }
            case "add" => {
              //config.files.foreach(File => Commands.add(File.getAbsolutePath))
              FileManager.IOprint("Adding files to the stage ...")
              config.files.map(File => {

                Add.doAdd(repo,File.getPath)
              })


            }
            case "branch" => {


              Branch.add(repo,args.apply(1),SgitManager.getCurrentCommit())


            }
            case "tag" => {
              FileManager.IOprint("Sgit tag : ")
              Tag.add(repo,args.apply(1),SgitManager.getCurrentCommit())


            }
            case "commit" => {
              FileManager.IOprint("Sgit commit :")
              Commit.commit(repo,SgitManager.getCurrentCommit())


            }
            case "diff" => {
              FileManager.IOprint("Sgit diff :")
              Diff.diff(repo)


            }
            case "log" => {
              FileManager.IOprint("Sgit Logs : ")
              Log.log(repo,SgitManager.getCurrentCommit())


            }
            case "status" => {

              Status.status(
                SgitManager.getWDToLines(repo)
                , Stage.toList().get.toList
                , SgitManager.getBlobsFromCommit(SgitManager.getCurrentCommit(),repo))


            }

          }


        case _ =>

      }
    }
    case _ => FileManager.IOprint("This command does not exist or implemented")

  }


}