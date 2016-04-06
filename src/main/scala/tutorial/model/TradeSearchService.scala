package tutorial.model

object TradeSearchService {

  def find(id: String): Seq[Trade] = {
    val s1 = Seq(
      Trade("NEW", tid = "t1", v = 1, id = "t1:1"),
      Trade("AMEND", tid = "t1", v = 2, mid = Option("t1"), id = "t1:2"),
      Trade("AMEND", tid = "t1", v = 3, mid = Option("t1"), id = "t1:3"),
      Trade("AMEND", tid = "t1", v = 4, mid = Option("t1"), id = "t1:4"),
      Trade("AMEND", tid = "t1", v = 5, mid = Option("t1"), id = "t1:5"),
      Trade("AMEND", tid = "t1", v = 6, mid = Option("t1"), id = "t1:6"),
      Trade("AMEND", tid = "t1", v = 7, mid = Option("t1"), id = "t1:7"),
      Trade("NEW", tid = "t2", v = 1, mid = Option("t1"), id = "t2:1"),
      Trade("AMEND", tid = "t2", v = 2, mid = Option("t1"), id = "t2:2"),
      Trade("AMEND", tid = "t2", v = 3, mid = Option("t1"), id = "t2:3"),
      Trade("AMEND", tid = "t2", v = 4, mid = Option("t1"), id = "t2:4"),
      Trade("AMEND", tid = "t2", v = 5, mid = Option("t1"), id = "t2:5"),
      Trade("AMEND", tid = "t2", v = 6, mid = Option("t1"), id = "t2:6"),
      Trade("AMEND", tid = "t2", v = 7, mid = Option("t1"), id = "t2:7"),
      Trade("AMEND", tid = "t2", v = 8, mid = Option("t1"), id = "t2:8",
        status = "Processing"),
      Trade(transactionType = "AMEND", tid = "t2", v = 9, mid = Option("t1"), id = "t2:9",
        status = "Captured"),
      Trade(transactionType = "NEW", tid = "t3", v = 1, mid = Option("t1"), id = "t3:1"),
      Trade(transactionType = "AMEND", tid = "t3", v = 2, mid = Option("t1"), id = "t3:2"),
      Trade(transactionType = "AMEND", tid = "t3", v = 3, mid = Option("t1"), id = "t3:3"),
      Trade(transactionType = "AMEND", tid = "t3", v = 4, mid = Option("t1"), id = "t3:4"),
      Trade(transactionType = "AMEND", tid = "t3", v = 5, mid = Option("t1"), id = "t3:5"),
      Trade(transactionType = "AMEND", tid = "t3", v = 6, mid = Option("t1"), id = "t3:6"),
      Trade(transactionType = "AMEND", tid = "t3", v = 7, mid = Option("t1"), id = "t3:7"),
      Trade(transactionType = "AMEND", tid = "t3", v = 8, mid = Option("t1"), id = "t3:8",
        status = "Discarded"),
      Trade(transactionType = "AMEND", tid = "t3", v = 9, mid = Option("t1"), id = "t3:9",
        status = "Failed")
    )

    val s2 = Seq(
      Trade(transactionType = "NEW", tid = "t1", v = 1, mid = Option("t1"), id = "t1:1"),
      Trade(transactionType = "AMEND", tid = "t1", v = 2, mid = Option("t1"), id = "t1:2"),
      Trade(transactionType = "NEW", tid = "t2", v = 1, mid = Option("t1"), id = "t2:1"),
      Trade(transactionType = "AMEND", tid = "t2", v = 2, mid = Option("t1"), id = "t2:2"),
      Trade(transactionType = "NEW", tid = "t3", v = 1, mid = None, id = "t3:1"),
      Trade(transactionType = "AMEND", tid = "t3", v = 2, mid = Option("t3"), id = "t3:2"),
      Trade(transactionType = "AMEND", tid = "t3", v = 3, mid = Option("t3"), id = "t3:3"),
      Trade(transactionType = "AMEND", tid = "t3", v = 4, mid = Option("t3"), id = "t3:4"),
      Trade(transactionType = "CANCEL", tid = "t3", v = 5, mid = Option("t3"), id = "t3:5"),
      Trade(transactionType = "NEW", tid = "t4", v = 1, mid = Option("t3"), id = "t4:1"),
      Trade(transactionType = "AMEND", tid = "t4", v = 2, mid = Option("t3"), id = "t4:2"),
      Trade(transactionType = "CANCEL", tid = "t4", v = 3, mid = Option("t3"), id = "t4:3"),
      Trade(transactionType = "NEW", tid = "t5", v = 1, mid = Option("t3"), id = "t5:1"),
      Trade(transactionType = "AMEND", tid = "t5", v = 2, mid = Option("t3"), id = "t5:2"),
      Trade(transactionType = "CANCEL", tid = "t5", v = 3, mid = Option("t3"), id = "t5:3"),
      Trade(transactionType = "NEW", tid = "t6", v = 1, mid = Option("t3"), id = "t6:1"),
      Trade(transactionType = "CANCEL", tid = "t6", v = 2, mid = Option("t3"), id = "t6:2"),
      Trade(transactionType = "NEW", tid = "t7", v = 1, mid = Option("t1"), id = "t7:1"),
      Trade(transactionType = "NEW", tid = "t8", v = 1, mid = Option("t1"), id = "t8:1"),
      Trade(transactionType = "NEW", tid = "t9", v = 1, mid = Option("t1"), id = "t9:1")
    )
    s2
  }

}
