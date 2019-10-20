import java.io.File

import better.files.{File => BFile}
import org.scalatest.FunSpec
import tools.Hash
class HashTest extends FunSpec {

  describe("hash command :") {

    val string1 = "test"
    val string2 = "test2"
    val hash1= Hash.sha_one(string1)
    val hash2= Hash.sha_one(string1)
    val hash5= Hash.sha_one(string2)
    val hash6= Hash.sha_one(string2)




    it("should always return same output for the same input" ) {


      assert((hash1==hash2) && (hash5 == hash6))

    }
    it("should not return same output for the different input" ) {


      assert(hash1!=hash6)

    }


  }



}