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
object GetSign {
  def apply(float: Vec[Bool]): Bool = {
    float(31)

  }
}
object GetExponent {
  def apply(float: Vec[Bool]): Seq[Bool] = {
    float.slice(23, 31)
  }
}

object GetMantissa {
  def apply(float: Vec[Bool]): Seq[Bool] = {
    float.slice(0, 23)
  }
}

// for n digit
object IsAllZero {
  def apply(width: Int, in: Seq[Bool]): Bool = {
    NOT(nBitOR(width, in))
  }
}

// all  1
object IsAllOnes {
  def apply(width: Int, in: Seq[Bool]): Bool = {
    nBitAND(width, in)
  }
}
// for 01100.....
object IsNonZero {
  def apply(width: Int, in: Seq[Bool]): Bool = {
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
object IsZero {
  def apply(float: Vec[Bool]): Bool = {
    val exp      = GetExponent(float)
    val mantissa = GetMantissa(float)
    AND(NOT(nBitOR(8, exp)), NOT(nBitOR(23, mantissa)))

  }

}

// -------------------------------------------------- Int to  Float ----------------------------
//turn one 0000010100111    -->   0000010000000     one-hot
object ToOneHot {
  def apply(width: Int, magnitude: Seq[Bool]): Vec[Bool] = {
    var seenHigherOne = false.B
    val res           = Wire(Vec(width, Bool()))

    for (i <- (0 until width).reverse) {
      res(i) := AND(magnitude(i), NOT(seenHigherOne))
      seenHigherOne = OR(seenHigherOne, magnitude(i))
    }
    res
  }
}

//oneHot(i) = 5   --> 5 + 127 = 000010000 -> 11001
object oneHotToExp {
  def apply(width: Int, oneHot: Vec[Bool]): Vec[Bool] = {
    val exp = Wire(Vec(8, Bool()))
    for (b <- 0 until 8) {
      var acc = false.B
      for (i <- 0 until width) {
        val value    = 127 + i
        val bitIsOne = ((value >> b) & 1) == 1

        if (bitIsOne) {
          acc = OR(acc, oneHot(i))
        }
      }
      exp(b) := acc
    }
    exp
  }
}

object oneHotToMan {
  def apply(width: Int, oneHot: Vec[Bool], magnitude: Seq[Bool]): Vec[Bool] = {
    val man = Wire(Vec(23, Bool()))
    for (j <- 0 until 23) {
      var acc = false.B
      for (i <- 0 until width) {
        // oneHot(i)        mantissa(j) =  magnitude ( i-j-1)
        val src = i - 1 - (22 - j)
        if (src >= 0) {
          acc = OR(acc, AND(oneHot(i), magnitude(src)))
        }
      }
      man(j) := acc
    }
    man
  }
}
