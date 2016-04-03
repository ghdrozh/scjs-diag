package tutorial.model

object TradeSearchService {

  def find(id: String): Seq[Trade] = {
    Seq(
      Trade(TransactionType.NEW, TradeStatus.Finished, "id1", "tid1", 1),
      Trade(TransactionType.NEW, TradeStatus.Finished, "id2", "tid2", 1, Some("tid1")),
      Trade(TransactionType.AMEND, TradeStatus.Finished, "id3", "tid2", 2, Some("tid1")),
      Trade(TransactionType.CANCEL, TradeStatus.Finished, "id4", "tid2", 3, Some("tid1")),
      Trade(TransactionType.NEW, TradeStatus.Finished, "id5", "tid3", 1, Some("tid1")),
      Trade(TransactionType.AMEND, TradeStatus.Finished, "id6", "tid3", 2, Some("tid1")),
      Trade(TransactionType.AMEND, TradeStatus.Finished, "id7",  "tid3", 3, Some("tid1")),
      Trade(TransactionType.AMEND, TradeStatus.Finished, "id8", "tid3", 4, Some("tid1"))
    )
  }

}
