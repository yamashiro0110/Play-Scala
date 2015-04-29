package models

import anorm.SqlParser._
import anorm._
import play.api.Play.current
import play.api.db._

case class User(id: Long, name: String, mail: String, password: String) {
  /**
   * userテーブルにデータを登録する
   * インスタンスから呼び出し可能
   */
  def addData {
    DB.withConnection { implicit c =>
      SQL(
        """
        INSERT INTO user (name, mail, password)
        values ({name}, {mail}, {password})
        """)
        .on(
          'name -> name,
          'mail -> mail,
          'password -> password
        )
        .executeUpdate()
    }
  }
}

object User {
  /**
   * SQL実行結果をオブジェクトに変換するパーサ
   */
  private val data = {
    get[Long]("id") ~
      get[String]("name") ~
      get[String]("mail") ~
      get[String]("password") map {
      case id ~ name ~ mail ~ password => User(id, name, mail, password)
    }
  }

  /**
   * 存在しないUserのインスタンス
   */
  val notFound = new User(0, "noName", "xxxx@xxxx.xxxx", "xxxxx")

  /**
   * IDで検索
   * @param id
   * @return
   */
  def findById(id: Long): Option[User] = {
    DB.withConnection { implicit c =>
      val sql = "SELECT * FROM user WHERE id = {id}"
      SQL(sql)
        .on("id" -> id)
        .as(User.data.singleOpt)
    }
  }
}
