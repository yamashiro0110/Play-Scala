package models

import anorm._
import play.api.db._
import play.api.Play.current
import anorm.SqlParser._

case class User(id:Long, name:String, mail:String, password:String) {
  def addData {
    DB.withConnection { implicit c =>
      val id:Int = SQL( """
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
  private val data = {
    get[Long]("id") ~
    get[String]("name") ~
    get[String]("mail") ~
    get[String]("password") map {
      case id ~ name ~ mail ~ password => User(id, name, mail, password)
    }
  }

  val notFound = new User(0, "noName", "xxxx@xxxx.xxxx", "xxxxx")

  /**
   * IDで検索
   * @param id
   * @return
   */
  def findById(id:Long):Option[User] = {
    DB.withConnection { implicit c =>
      val sql = "SELECT * FROM user WHERE id = {id}"
      SQL(sql)
        .on("id" -> id)
        .as(User.data.singleOpt)
    }
  }
}
