package services

import io.lemonlabs.uri.Url
import models.{Product, User}
import repositories.Tables._
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.{ExecutionContext, Future}

class CoupangService(db: Database)(implicit ec: ExecutionContext) {
   Products
  def getProducts(id: Option[Int]): Future[Seq[Product]] = {
    db
      .run(
        id match {
          case Some(value) => Products.filter(product => product.prodid === value).result
          case _ => Products.result
        }
      )
      .map(items => items.map(item => Product(item.prodid, item.name, item.url)))
  }
  def createProduct(name: String, url: String): Future[Any] = {
    val refinedUrl = Url.parse(url).removeQueryString().toString()
    db.run((Products += ProductsRow(-1, name, refinedUrl)).asTry)
      .handleDBResponse()
  }
  def removeProduct(id: Int): Future[Any] = {
    db.run(Products.filter(_.prodid === id).delete.asTry).handleDBResponse()
  }

  // Users
  def getUserByName(name: String): Future[Seq[User]] = {
    db
      .run(Users.filter(user => user.username === name).result)
      .map(infos => infos.map(info => User(info.userid, info.username)))
  }
  def createUser(name: String): Future[Any] = {
    db.run((Users += UsersRow(-1, name)).asTry).handleDBResponse()
  }
  def removeUser(id: Int): Future[Any] = {
    db.run(Users.filter(_.userid === id).delete.asTry).handleDBResponse()
  }

}
