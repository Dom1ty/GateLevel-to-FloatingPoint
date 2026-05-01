package sysarch.circuits.helpers

import sysarch.chisel._
import sysarch.gates._

// when 0 do nth;when 1 invert all the bit into
object ConditionalInvertAll {
  def apply(width: Int, in: Vec[Bool], flag: Bool): Vec[Bool] = {
    val out = Wire(Vec(width, Bool()))

    for (i <- 0 until width) {
      out(i) := XOR(in(i), flag)
    }

    out
  }
}
// when 0 do nth;when 1 invert all the bit into   except for signbit
object ConditionalInvertExceptSign {
  def apply(width: Int, in: Vec[Bool], flag: Bool): Vec[Bool] = {
    val out = Wire(Vec(width, Bool()))

    for (i <- 0 until width - 1) {
      out(i) := XOR(in(i), flag)
    }
    out(width - 1) := in(width - 1)

    out
  }
}

// when 0 do nth;   when 1  plus 1
object ConditionalIncrement {
  def apply(width: Int, in: Vec[Bool], flag: Bool): Vec[Bool] = {
    val one = Wire(Vec(width, Bool()))
    one    := Vec.fill(width)(false.B)
    one(0) := flag

    val (sum, _) = nBitAdderSubtractor(width, in, one, false.B)

    sum

  }
}

// when 0 do nth;   when 1  minus 1
object ConditionalDecrement {
  def apply(width: Int, in: Vec[Bool], flag: Bool): Vec[Bool] = {
    val one = Wire(Vec(width, Bool()))
    one    := Vec.fill(width)(false.B)
    one(0) := flag

    val (sum, _) = nBitAdderSubtractor(width, in, one, true.B)

    sum

  }
}



// ---------------------------------------floatingpoint-------------------------------
object GetSign{
  def apply(float: Vec[Bool]): Bool = {
    float(31)

  }
}
object GetExponent {
  def apply(float: Vec[Bool]): Vec[Bool] = {
    float.slice(23, 31)
  }
}

object GetMantissa {
  def apply(float: Vec[Bool]): Vec[Bool] = {
    float.slice(0, 23)
  }
}

// for n digit
object IsAllZero {
  def apply(width: Int, in: Vec[Bool]): Bool = {
    NOT(nBitOR(width, in))
  }
}

// all  1
object IsAllOnes {
  def apply(width: Int, in: Vec[Bool]): Bool = {
    nBitAND(width, in)
  }
}
// for 01100.....
object IsNonZero {
  def apply(width: Int, in: Vec[Bool]): Bool = {
    nBitOR(width, in)
  }
}

object IsNaN {
  def apply(float: Vec[Bool]): Bool = {
    val exp = GetExponent(float)
    val man = GetMantissa(float)

    AND(
      IsAllOnes(8, exp),
      IsNonZero(23, man)
    )
  }
}


object IsInfinity {
  def apply(float: Vec[Bool]): Bool = {
    val exp = GetExponent(float)
    val man = GetMantissa(float)

    AND(
      IsAllOnes(8, exp),
      IsAllZero(23, man)
    )
  }
}


//0000000 or 100000000 both is zero
//for 32 digit
object IsZero{
    def apply(float: Vec[Bool]): Bool = {
      val exp = GetExponent(float)
      val mantissa = GetMantissa(float)
      AND( Not( nBitOR(8, exp)), NOT( nBitOR(23, mantissa)))
      
    }

}





// ShiftRight
// ShiftLeft
// LeadingOneDetector
