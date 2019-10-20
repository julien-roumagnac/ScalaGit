import commands.Diff
import org.scalatest.FunSpec

class DiffTest extends FunSpec {

  describe("Diff command") {

    val vide = List()
    val a = List("a")
    val b = List("b")
    val abc = List("a","b","c")
    val ebcdf = List("e","b","d","c","f")

    it("should return each line of text1 preceded by \"--\" if text2 is empty ") {


      assert(Diff.doDiff(a, vide, List()) == List(("--", "a")))
    }
    it("should return empty list if text1 and text2 is empty ") {


      assert(Diff.doDiff(vide, vide, List()) == List())
    }
    it("should return each line of text2 preceded by \"++\" if text1 is empty ") {

      assert(Diff.doDiff(vide, b, List()) == List(("++", "b")))
    }
    it("should return each line of text1  if text2 is equal ") {
      assert(Diff.doDiff(a, a, List()) == List(("  ", "a")))
    }
    it("should return all the lines from text 1 and 2 preceded by \"--\" or \"++\" or \"  \" depending on how they have changed ") {
      assert( Diff.doDiff(abc,ebcdf,List()) == List(("++","e"),("--","a"),("  ","b"),("++","d"),("  ","c"),("++","f")) )


    }


  }
}