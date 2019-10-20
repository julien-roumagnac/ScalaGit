import java.io.File

import better.files.{File => BFile}
import commands.Init
import org.scalatest.FunSpec
import tools.SgitManager
class IniTest extends FunSpec {

  describe("Init command :") {
    Init.init()

    val repo = SgitManager.getPathRepo()
    val sgit =  new File(s"${repo}.sgit")
    val objects = new File(s".sgit${File.separator}objects")
    val refs = new File(s".sgit${File.separator}objects")

    it("should create .sgit diretory if doesnt exists") {


      assert((sgit.exists() && sgit.isDirectory) == true)

      }
    it("should have 4 files or dir in .sgit diretory ") {
      assert(sgit.listFiles().size == 4)
    }
    it("should have 2 files or dir in objects diretory ") {
      assert(objects.listFiles().size == 2)
    }
    it("should have 2 files or dir in refs diretory ") {
      assert(refs.listFiles().size == 2)
    }

  }



}