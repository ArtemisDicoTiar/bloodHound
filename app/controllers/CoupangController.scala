package controllers

import models.{Basket, Price, Product, User}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json._
import play.api.mvc._
import services.CoupangService
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile._

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CoupangController @Inject()
  (protected val dbConfigProvider: DatabaseConfigProvider, cc: ControllerComponents)
  (implicit ec: ExecutionContext)
  extends AbstractController(cc) with HasDatabaseConfigProvider[JdbcProfile] {

  private val service = new CoupangService(db)


  implicit val productRowWrites: OWrites[Product] = Json.writes[Product]
  implicit val userRowWrites: OWrites[User] = Json.writes[User]
  implicit val priceRowWrites: OWrites[Price] = Json.writes[Price]
  implicit val basketRowWrites: OWrites[Basket] = Json.writes[Basket]

  def getProduct(id: Option[Int]): Action[AnyContent] = Action.async { implicit request =>
    service.getProducts(id).map(product => Ok(Json.toJson(product)))
  }

  def removeProduct(id: Int): Action[AnyContent] = Action.async { implicit request =>
    service.removeProduct(id).map {
      case int: Int => Accepted(s"Removed product with id: $id")
      case others => NotAcceptable(s"Cannot remove product with id: $id\n*because:\n$others")
    }
  }

  def createProduct(name: String, url: String): Action[AnyContent] = Action.async { implicit request =>
    service.createProduct(name, url).map {
      case int: Int => Created(s"Created product: $name\n($url)")
      case others => NotAcceptable(s"Cannot create Product: $name\n($url)\n*because:\n$others")
    }
  }

  def getUser(name: Option[String], id: Option[Int]): Action[AnyContent] = Action.async { implicit request =>
    name match {
      case Some(n) => service.getUserByName(n).map(user => Ok(Json.toJson(user)))
      case None =>
        id match {
          case Some(i) => service.getUserById(i).map(user => Ok(Json.toJson(user)))
          case None => Future(BadRequest("Parameter Missing. (Either name or id of user should be given)"))
        }
    }
  }

  def createUser(name: String): Action[AnyContent] = Action.async { implicit request =>
    service.createUser(name).map {
      case int: Int => Created(s"Created User: $name")
      case others => NotAcceptable(s"Cannot create user: $name\n*because:\n$others")
    }
  }

  def removeUser(id: Int): Action[AnyContent] = Action.async { implicit request =>
    service.removeProduct(id).map {
      case int: Int => Accepted(s"Removed user with id: $id")
      case others => NotAcceptable(s"Cannot remove user with id: $id\n$others")
    }
  }

  def getPricesByProdId(id: Int): Action[AnyContent] = Action.async { implicit request =>
    service.getPricesByProdId(id).map(price => Ok(Json.toJson(price)))
  }

  def addPriceOfProductByProdId(id: Int, price: Long): Action[AnyContent] = Action.async { implicit request =>
    service.addPriceOfProductByProdId(id, price).map {
      case int: Int => Created(s"Price appended - product: $id, price: $price")
      case others => NotAcceptable(s"Cannot add product price - product: $id\n*because:\n$others")
    }
  }

  def removeDataBeforeTime(date: String): Action[AnyContent] = Action.async {
    service.removeDataBeforeDate(date).map {
      case int: Int => Accepted(s"Removed data before $date")
      case others => NotAcceptable(s"Cannot remove data before $date\n*because:\n$others")
    }
  }

  def getBasketByUserId(id: Int): Action[AnyContent] = Action.async { implicit request =>
    service.getBasketByUserId(id).map(basket => Ok(Json.toJson(basket)))
  }

  def addProductToBasket(prodId: Int, userId: Int): Action[AnyContent] = Action.async { implicit request =>
    service.addProductToBasket(prodId, userId).map {
      case int: Int => Created(s"Product added to basket - product: $prodId, user: $userId")
      case others => NotAcceptable(s"Cannot add product to basket - product: $prodId,, user: $userId\n*because:\n$others")
    }
  }

  def removeProductFromBasket(prodId: Int, userId: Int): Action[AnyContent] = Action.async { implicit request =>
    service.removeProductFromBasket(prodId, userId).map {
      case int: Int => Accepted(s"Removed product from basket - product: $prodId, user: $userId")
      case others => NotAcceptable(s"Cannot remove product from basket - product: $prodId, user: $userId\n*because:\n$others")
    }
  }


}
