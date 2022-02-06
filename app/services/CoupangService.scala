package services

import io.lemonlabs.uri.Url
import models.{Basket, Price, Product, User}
import org.joda.time.DateTime
import repositories.Tables._
import slick.jdbc.MySQLProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime
import scala.concurrent.{ExecutionContext, Future}

class CoupangService(db: Database)(implicit ec: ExecutionContext) {
  // Products
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

  // Prices
  def getPricesByProdId(id: Int): Future[Seq[Price]] = {
    db.run(Prices.filter(price => price.prodid === id).result)
      .map(infos => infos.map(info => Price(info.prodid, info.datetime, info.price)))
  }

  def addPriceOfProductByProdId(id: Int, price: Long): Future[Any] = {
    val now = Timestamp.valueOf(LocalDateTime.now())
    db.run((Prices += PricesRow(id, now, price)).asTry).handleDBResponse()
  }

  def removeDataBeforeDate(date: String): Future[Any] = {
    val filterDateInTimeStamp = Timestamp.valueOf(DateTime.parse(date).toString())
    db.run(Prices.filter(_.datetime < filterDateInTimeStamp).delete.asTry).handleDBResponse()
  }

  // Baskets
  def getBasketByUserId(id: Int): Future[Seq[Basket]] = {
    db.run(Baskets.filter(basket => basket.userid === id).result)
      .map(infos => infos.map(info => Basket(info.userid, info.prodid)))
  }

  def addProductToBasket(prodId: Int, userId: Int): Future[Any] = {
    db.run((Baskets += BasketsRow(userId, prodId)).asTry).handleDBResponse()
  }

  def removeProductFromBasket(prodId: Int, userId: Int): Future[Any] = {
    db.run(Baskets.filter(_.prodid === prodId).delete.asTry).handleDBResponse()
  }

}
