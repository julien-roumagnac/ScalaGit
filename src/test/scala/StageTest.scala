import org.scalatest.FunSpec
import tools.Stage

class StageTest extends FunSpec {
  describe("Stage should be able to "){
    val stage1 = Array("path a","b")
    val stage2 =Array("path c","b")
    val stage3 = Array()
    val line1 = "path c"

    it("modify an existing path  to the stage "){
      assert(Stage.addLine(line1,Some(stage1)).toList==stage2.toList)
    }

  }

}
