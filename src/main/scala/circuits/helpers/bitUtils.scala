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
//0000000 or 100000000 both is zero

object IsZero{
    def apply(float: Vec[Bool]): Bool = {
      val exp = GetExponent(float)
      val mantissa = GetMantissa(float)
      AND( Not( nBitOR(8, exp)), NOT( nBitOR(23, mantissa)))
      
    }

}

object IsExpAllOnes{
    def apply(float: Vec[Bool]): Bool = {
      val exp = GetExponent(float)
      nBitAND(8, exp)
      
    }

}

object IsMantissaZero{
    def apply(float: Vec[Bool]): Bool = {
      val mantissa = GetMantissa(float)
      NOT(nBitOR(23, mantissa))
    }
}

object IsNaN{
    def apply(float: Vec[Bool]): Bool = {
      
      AND(IsExpAllOnes(float), NOT(IsMantissaZero(float)))
    }
}



object IsInfinity{
    def apply(float: Vec[Bool]): Bool = {
    
      AND(IsExpAllOnes(float), IsMantissaZero(float))
      
    }

}







// ShiftRight
// ShiftLeft
// LeadingOneDetector
