import commands.Status
import org.scalatest.FunSpec

class StatusTest extends FunSpec {

  describe("commands.Status algo ") {

    val wd1 = List("file1 hash1","file2 hash1","file3 hash2")
    val stage1 = List("file1 hash2","file2 hash1" , "file4 hash1")
    val commit1 = List("file1 hash2","file3 hash1" , "file4 hash1")
    val commit2 = List("file5 hash2","file3 hash1" , "file4 hash1")
    val res1 = (List("untracked : file3"),List("changed : file1"),List(),List("new : file2"))
    val res2 = (List("untracked : file1","untracked : file2","untracked : file3"),List(),List(),List())
    val res3 = (List(),List(),List(),List("new : file1","new : file2","new : file3"))
    val res4 = (List(),List(),List(),List())
    val res5 = (List("untracked : file3"),List("changed : file1"),List(),List("new : file1","new : file2"))


    it("should return only untracked files if stage and commits are empty ") {

      assert(Status.getStatusGroups(wd1,List(),List()) == res2)
    }
    it("should return only new if all changes have been staged and commit is empty ") {

      assert(Status.getStatusGroups(wd1,wd1,List()) == res3)
    }
    it("should return nothing  if wd == stage == commit ") {

      assert(Status.getStatusGroups(wd1,wd1,wd1) == res4)
    }
    it("should manage all this case in same time and think to intersection of the parts") {

      assert(Status.getStatusGroups(wd1,stage1,commit1) == res1)
    }
    it("should manage complex cases") {

      assert(Status.getStatusGroups(wd1,stage1,commit2) == res5)
    }






  }
}