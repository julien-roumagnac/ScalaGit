import org.scalatest.FunSpec
import tools.SgitManager

class SGITManagerTest extends FunSpec {

  describe("Sgit manager should be able to "){
    val repo = "/home/scala/"
    val absolutepath = "/home/scala/test/filename.txt"
    val absolutepath2 = "/home/scala/filename.txt"
    val res1 ="test/filename.txt"
    val res2 = "filename.txt"
    it("get the relative path of a file in  the repo "){

        assert(res1 == SgitManager.relativize(absolutepath,repo))
    }
    it("get the relative path of a file in a dir in the repo "){

      assert(res2 == SgitManager.relativize(absolutepath2,repo))

    }
  }

}
